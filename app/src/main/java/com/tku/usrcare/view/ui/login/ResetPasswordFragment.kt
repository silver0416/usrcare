package com.tku.usrcare.view.ui.login

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.databinding.FragmentResetBinding
import com.tku.usrcare.model.ResetPassword
import com.tku.usrcare.repository.SessionManager

class ResetPasswordFragment : Fragment() {
    private var _binding: FragmentResetBinding? = null
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            SessionManager(requireContext()).clearUserAccountList()
            findNavController().navigateUp()
        }
        binding?.btnBack?.setOnClickListener {
            SessionManager(requireContext()).clearUserAccountList()
            findNavController().navigateUp()
        }

        fun checkIsPasswordValid(): Boolean {
            var pass = true
            if (binding!!.passwordEditText.text.toString().isEmpty()) {
                if (pass) {
                    binding!!.tilUsername.error = null
                }
                binding!!.tilPassword.error = "請輸入密碼"
                pass = false
            }
            if (binding!!.secPasswordEditText.text.toString().isEmpty()) {
                if (pass) {
                    binding!!.tilUsername.error = null
                    binding!!.tilPassword.error = null
                }
                binding!!.tilSecPassword.error = "請再次輸入密碼"
                pass = false
            }
            if (binding!!.passwordEditText.text.toString() != binding!!.secPasswordEditText.text.toString()) {
                if (pass) {
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
                if (pass) {
                    binding!!.tilUsername.error = null
                    binding!!.tilPassword.error = null
                    binding!!.tilSecPassword.error = null
                }
                binding!!.tilPassword.error = "密碼長度不足"
                pass = false
            }
            if (pass){
                binding!!.tilPassword.error = null
                binding!!.tilSecPassword.error = null
            }
            return pass
        }


        val arg = arguments?.getString("arg")
        if (arg == "noPassword") {
            val account = arguments?.getString("account")
            val items = arrayOf(account)
            val invalidToken = arguments?.getString("invalidToken")
            val otp = arguments?.getString("otp")
            binding?.genderEditText?.setText(items[0])
            binding?.genderEditText?.isEnabled = false
            binding?.btnNext?.setOnClickListener {
                if (checkIsPasswordValid()) {
                    val salt = SaltGenerator.generateSalt()
                    val hashedPassword = PasswordHasher.hashPassword(
                        binding!!.passwordEditText.text.toString(),
                        salt
                    )
                    val resetPassword = ResetPassword(
                        otp = otp.toString(),
                        newPassword = hashedPassword,
                        salt = salt
                    )
                    ApiUSR.postResetPassword(
                        activity = requireActivity(),
                        resetPassword = resetPassword,
                        token = invalidToken.toString(),
                        onSuccess = {
                            SessionManager(requireContext()).clearUserAccountList()
                            AlertDialog.Builder(requireContext())
                                .setTitle("密碼重設成功")
                                .setMessage("請重新登入")
                                .setPositiveButton("確定") { _, _ ->
                                }
                                .setOnDismissListener { _ ->
                                    findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
                                }
                                .show()
                        }
                    )
                }
            }
        } else if (arg == "resetPassword") {
            val accountLists = SessionManager(requireContext()).getUserAccountList()
            SessionManager(requireContext()).clearUserAccountList()
            val otp = arguments?.getString("otp")
            val accountAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                accountLists
            )
            binding?.genderEditText?.setAdapter(accountAdapter)
            binding?.btnNext?.setOnClickListener {
                if (checkIsPasswordValid()) {
                    //todo: call api
                }
            }
        }
    }
}