package com.tku.usrcare.view.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.databinding.FragmentSignUpEmailBinding
import com.tku.usrcare.repository.SessionManager

class SignUpEmailFragment : Fragment() {
    private var _binding: FragmentSignUpEmailBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpEmailBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sessionManager = SessionManager(requireContext())
        val arg = arguments?.getString("arg")

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        val view = binding?.root
        binding?.btnNext?.setOnClickListener {
            if (!emailChecker(
                    binding?.emailEditText?.text.toString()
                )
            ) {
                binding?.tilEmail?.error = "請輸入電子郵件"
                return@setOnClickListener
            }
            binding?.btnNext?.isEnabled = false
            sessionManager.saveUserEmail(binding?.emailEditText?.text.toString())
            ApiUSR.getEmailCheck(requireActivity(),
                binding?.emailEditText?.text.toString(),
                binding!!,
                onSuccess = {
                    //未註冊過
                    if (!it.exist) {
                        val action =
                            SignUpEmailFragmentDirections.actionSignUpEmailFragmentToLoginVerifyFragment(
                                "signup"
                            )
                        findNavController(view = view!!).navigate(action)
                    }
                    //已註冊過
                    else {
                        if (arg == "signup") {
                            val action =
                                SignUpEmailFragmentDirections.actionSignUpEmailFragmentToSignUpFragment()
                            findNavController(view = view!!).navigate(action)
                        }
                        if (arg == "resetPassword") {
                            ApiUSR.getEmailAccountList(
                                requireActivity(),
                                binding?.emailEditText?.text.toString(),
                                onSuccess = { emailAccountList, emailUserIdList ->
                                    sessionManager.saveUserAccountNameList(emailAccountList.toMutableList())
                                    sessionManager.saveUserAccountTokenList(emailUserIdList.toMutableList())
                                    val action =
                                        SignUpEmailFragmentDirections.actionSignUpEmailFragmentToResetPasswordFragment(
                                            "resetPassword",
                                        )
                                    findNavController(view = view!!).navigate(action)
                                },
                            )
                        }
                    }
                },
                onError = {
                    Log.d("SignUpEmailFragment", "onViewCreated: $it")
                })
        }
        binding?.btnBack?.setOnClickListener() {
            findNavController().navigateUp()
        }
    }

    private fun emailChecker(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}