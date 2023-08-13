package com.tku.usrcare.view.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.color.utilities.MaterialDynamicColors.onError
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.databinding.FragmentSignUpEmailBinding
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.MainActivity

class SignUpEmailFragment : Fragment() {
    private var _binding: FragmentSignUpEmailBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpEmailBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sessionManager = SessionManager(requireContext())
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        val view = binding?.root
        binding?.btnNext?.setOnClickListener {
            if (!emailChecker(
                    binding?.emailEditText?.text.toString()
                )) {
                binding?.emailEditText?.error = "請輸入電子郵件"
                return@setOnClickListener
            }
            sessionManager.saveUserEmail(binding?.emailEditText?.text.toString())
            ApiUSR.getEmailCheck(requireActivity(),
                binding?.emailEditText?.text.toString(),
                binding!!,
                onSuccess = {
                    //未註冊過
                    if (!it.exist) {
                        val action =
                            SignUpEmailFragmentDirections.actionSignUpEmailFragmentToLoginVerifyFragment()
                        findNavController(view = view!!).navigate(action)
                    }
                    //已註冊過
                    else {
                        val action =
                            SignUpEmailFragmentDirections.actionSignUpEmailFragmentToSignUpFragment()
                        findNavController(view = view!!).navigate(action)
                    }
                },
                onError = {
                    Log.d("SignUpEmailFragment", "onViewCreated: $it")
                }
            )
        }
        binding?.btnBack?.setOnClickListener() {
            findNavController().navigateUp()
        }
    }
    private fun emailChecker(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}