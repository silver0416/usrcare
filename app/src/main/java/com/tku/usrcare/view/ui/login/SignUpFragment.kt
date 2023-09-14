package com.tku.usrcare.view.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        binding!!.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding!!.btnNext.setOnClickListener {
            var pass = true
            if (binding!!.accountEditText.text.toString().isEmpty()) {
                binding!!.tilUsername.error = "請輸入帳號"
                pass = false
            }
            if (binding!!.passwordEditText.text.toString().isEmpty()) {
                if (pass){
                    binding!!.tilUsername.error = null
                }
                binding!!.tilPassword.error = "請輸入密碼"
                pass = false
            }
            if (binding!!.secPasswordEditText.text.toString().isEmpty()) {
                if (pass){
                    binding!!.tilUsername.error = null
                    binding!!.tilPassword.error = null
                }
                binding!!.tilSecPassword.error = "請再次輸入密碼"
                pass = false
            }
            if (binding!!.passwordEditText.text.toString() != binding!!.secPasswordEditText.text.toString()) {
                if (pass){
                    binding!!.tilUsername.error = null
                    binding!!.tilPassword.error = null
                    binding!!.tilSecPassword.error = null
                }
                binding!!.tilPassword.error = "密碼不一致"
                binding!!.tilSecPassword.error = "密碼不一致"
                pass = false
            }
            //檢查密碼是否大於8碼
            if (binding!!.passwordEditText.text.toString().length < 8) {
                if (pass){
                    binding!!.tilUsername.error = null
                    binding!!.tilPassword.error = null
                    binding!!.tilSecPassword.error = null
                }
                binding!!.tilPassword.error = "密碼長度不足"
                pass = false
            }
            if (pass) {
                ApiUSR.getUserNameCheck(activity = requireActivity(), username = binding!!.accountEditText.text.toString()) {
                    if (it.exist) {
                        binding!!.tilPassword.error = null
                        binding!!.tilSecPassword.error = null
                        binding!!.tilUsername.error = "帳號已存在"
                        pass = false
                    }
                    if (pass) {
                        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignUpUserDetailFragment(
                            binding!!.accountEditText.text.toString(),
                            binding!!.passwordEditText.text.toString()
                        ))
                    }
                }
            }
        }
    }
}