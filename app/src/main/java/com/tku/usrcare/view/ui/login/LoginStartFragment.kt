package com.tku.usrcare.view.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.linecorp.linesdk.LineApiResponseCode
import com.linecorp.linesdk.Scope
import com.linecorp.linesdk.auth.LineAuthenticationParams
import com.linecorp.linesdk.auth.LineLoginApi
import com.tku.usrcare.OAuthClientID
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.databinding.FragmentLoginStartBinding
import com.tku.usrcare.repository.ImageSaver
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.MainActivity
import com.tku.usrcare.viewmodel.LoginViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginStartFragment : Fragment() {
    private var _binding: FragmentLoginStartBinding? = null
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var loginViewModel: LoginViewModel
    private val binding get() = _binding
    private var startGoogleSignIn =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val avatarUrl = task.result?.photoUrl.toString()
                CoroutineScope(Dispatchers.Main).launch {
                    val bitmap = ImageSaver().loadImageFromWeb(avatarUrl)
                    bitmap?.let {
                        ImageSaver().saveImageToInternalStorage(
                            bitmap,
                            requireActivity(),
                            "avatar"
                        )
                        loginViewModel.googleId.postValue(task.result.idToken ?: "")
                        loginViewModel.postGoogleOAuthToken(task.result.idToken ?: "")
                    }
                }
            } else {
                binding?.loading?.visibility = View.GONE
                binding?.btnGoogleLogin?.isEnabled = true
                binding?.btnLineLogin?.isEnabled = true
                binding?.loginButton?.isEnabled = true
                binding?.signupButton?.isEnabled = true
            }
        }

    private var startLineSignIn =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = LineLoginApi.getLoginResultFromIntent(result.data)
                when (task.responseCode) {
                    LineApiResponseCode.SUCCESS -> {
                        val accessToken = task.lineCredential?.accessToken?.tokenString
                        val avatarUrl = task.lineProfile?.pictureUrl?.toString()
                        CoroutineScope(Dispatchers.Main).launch {
                            val bitmap = ImageSaver().loadImageFromWeb(avatarUrl ?: "")
                            bitmap?.let {
                                ImageSaver().saveImageToInternalStorage(
                                    bitmap,
                                    requireActivity(),
                                    "avatar"
                                )
                                loginViewModel.lineId.postValue(accessToken ?: "")
                                loginViewModel.postLineOAuthToken(accessToken ?: "")
                            }
                        }
                    }

                    LineApiResponseCode.CANCEL -> {
                        binding?.loading?.visibility = View.GONE
                        binding?.btnGoogleLogin?.isEnabled = true
                        binding?.btnLineLogin?.isEnabled = true
                        binding?.loginButton?.isEnabled = true
                        binding?.signupButton?.isEnabled = true
                    }

                    else -> {
                        Log.d("LoginStartFragment", "onViewCreated: error")
                        binding?.loading?.visibility = View.GONE
                        binding?.btnGoogleLogin?.isEnabled = true
                        binding?.btnLineLogin?.isEnabled = true
                        binding?.loginButton?.isEnabled = true
                        binding?.signupButton?.isEnabled = true
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginStartBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ViewModelFactory(SessionManager(requireActivity()))
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.loginButton.setOnClickListener {
            val action = LoginStartFragmentDirections.actionLoginStartFragmentToLoginFragment()
            findNavController().navigate(action)
        }
        binding!!.signupButton.setOnClickListener {
            val arg = "signup"
            val action =
                LoginStartFragmentDirections.actionLoginStartFragmentToSignUpEmailFragment(arg)
            findNavController().navigate(action)
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(OAuthClientID.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        mGoogleSignInClient.signOut()
        binding?.btnGoogleLogin?.setOnClickListener {
            val signInGoogleIntent = mGoogleSignInClient.signInIntent
            binding?.loading?.visibility = View.VISIBLE
            binding?.btnGoogleLogin?.isEnabled = false
            binding?.btnLineLogin?.isEnabled = false
            binding?.loginButton?.isEnabled = false
            binding?.signupButton?.isEnabled = false
            startGoogleSignIn.launch(signInGoogleIntent)
        }
        loginViewModel.launchGoogleRegister.observe(viewLifecycleOwner) {
            if (it) {
                binding?.loading?.visibility = View.GONE
                val action =
                    LoginStartFragmentDirections.actionLoginStartFragmentToSignUpUserDetailFragment(
                        "",
                        "",
                        loginViewModel.googleId.value.toString(),
                        "google"
                    )
                findNavController().navigate(action)
            }
        }

        val lap = LineAuthenticationParams.Builder()
            .scopes(listOf(Scope.PROFILE))
            .build()
        binding?.btnLineLogin?.setOnClickListener {
            val signInLineIntent = LineLoginApi.getLoginIntent(
                requireActivity(),
                OAuthClientID.LINE_CLIENT_ID,
                lap
            )
            binding?.loading?.visibility = View.VISIBLE
            binding?.btnGoogleLogin?.isEnabled = false
            binding?.btnLineLogin?.isEnabled = false
            binding?.loginButton?.isEnabled = false
            binding?.signupButton?.isEnabled = false
            startLineSignIn.launch(signInLineIntent)
        }

        loginViewModel.launchLineRegister.observe(viewLifecycleOwner) {
            if (it) {
                binding?.loading?.visibility = View.GONE
                val action =
                    LoginStartFragmentDirections.actionLoginStartFragmentToSignUpUserDetailFragment(
                        "",
                        "",
                        loginViewModel.lineId.value.toString(),
                        "line"
                    )
                findNavController().navigate(action)
            }
        }

        loginViewModel.launchLogin.observe(viewLifecycleOwner) {
            if (it) {
                ApiUSR.getCheckInRecord(
                    requireActivity(),
                    SessionManager(requireContext()),
                    onSuccess = {
                        binding?.loading?.isVisible = false
                        val intent =
                            Intent(
                                activity,
                                MainActivity::class.java
                            )
                        startActivity(intent)
                    },
                    onFail = {
                        binding?.loading?.isVisible = false
                    }
                )
            }
        }
    }
}