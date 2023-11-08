package com.tku.usrcare.view.ui.login

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.databinding.FragmentResetBinding
import com.tku.usrcare.model.AccountOtp
import com.tku.usrcare.model.ResetPassword
import com.tku.usrcare.repository.SessionManager

class ResetPasswordFragment : Fragment() {
    private var _binding: FragmentResetBinding? = null
    private val binding get() = _binding
    private var timer: CountDownTimer? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            SessionManager(requireContext()).clearUserAccountNameList()
            SessionManager(requireContext()).clearUserAccountTokenList()
            findNavController().navigateUp()
        }
        binding?.btnBack?.setOnClickListener {
            SessionManager(requireContext()).clearUserAccountNameList()
            SessionManager(requireContext()).clearUserAccountTokenList()
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
            if (!(binding!!.passwordEditText.text.toString().isPasswordLongEnough())) {
                if (pass) {
                    binding!!.tilUsername.error = null
                    binding!!.tilPassword.error = null
                    binding!!.tilSecPassword.error = null
                }
                binding!!.tilPassword.error = "密碼長度不足"
                pass = false
            }

            if (!(binding!!.secPasswordEditText.text.toString().isPasswordLongEnough())) {
                if (pass) {
                    binding!!.tilUsername.error = null
                    binding!!.tilPassword.error = null
                    binding!!.tilSecPassword.error = null
                }
                binding!!.tilSecPassword.error = "密碼長度不足"
                pass = false
            }
            //檢查密碼是否包含英文或數字以外的字元
            if (!(binding!!.passwordEditText.text.toString().engNumOnly())) {
                if (pass) {
                    binding!!.tilUsername.error = null
                    binding!!.tilPassword.error = null
                    binding!!.tilSecPassword.error = null
                }
                binding!!.tilPassword.error = "密碼只能包含英文或數字"
                pass = false
            }

            if (!(binding!!.passwordEditText.text.toString().engNumOnly())){
                if (pass) {
                    binding!!.tilUsername.error = null
                    binding!!.tilPassword.error = null
                    binding!!.tilSecPassword.error = null
                }
                binding!!.tilPassword.error = "密碼只能包含英文或數字"
                pass = false
            }

            //檢查密碼是否包含空白
            if (binding!!.passwordEditText.text.toString().containsWhitespace()) {
                if (pass) {
                    binding!!.tilUsername.error = null
                    binding!!.tilPassword.error = null
                    binding!!.tilSecPassword.error = null
                }
                binding!!.tilPassword.error = "密碼不可包含空白"
                binding!!.passwordEditText.setText("")
                pass = false
            }

            if (binding!!.secPasswordEditText.text.toString().containsWhitespace()) {
                if (pass) {
                    binding!!.tilUsername.error = null
                    binding!!.tilPassword.error = null
                    binding!!.tilSecPassword.error = null
                }
                binding!!.tilSecPassword.error = "密碼不可包含空白"
                binding!!.secPasswordEditText.setText("")
                pass = false
            }

            if(binding!!.tilOtp.isVisible){
                if (binding!!.otpEditText.text.toString().isEmpty()) {
                    if (pass) {
                        binding!!.tilUsername.error = null
                        binding!!.tilPassword.error = null
                        binding!!.tilSecPassword.error = null
                    }
                    binding!!.tilOtp.error = "請輸入驗證碼"
                    pass = false
                }
                if (binding!!.otpEditText.text.toString().length != 6) {
                    if (pass) {
                        binding!!.tilUsername.error = null
                        binding!!.tilPassword.error = null
                        binding!!.tilSecPassword.error = null
                    }
                    binding!!.tilOtp.error = "驗證碼長度不足"
                    pass = false
                }
            }
            if (pass) {
                binding!!.tilPassword.error = null
                binding!!.tilSecPassword.error = null
                binding!!.tilOtp.error = null
            }
            return pass
        }


        val arg = arguments?.getString("arg")
        if (arg == "noPassword") {
            val account = arguments?.getString("account")
            val items = arrayOf(account)
            val invalidToken = arguments?.getString("invalidToken")
            val otp = arguments?.getString("otp")
            binding?.btnResendOtp?.isVisible = false
            binding?.tvOtp?.isVisible = false
            binding?.tilOtp?.isVisible = false
            binding?.accountSelectEditText?.setText(items[0])
            binding?.accountSelectEditText?.isEnabled = false
            binding?.btnNext?.setOnClickListener {
                if (checkIsPasswordValid()) {
                    binding?.loading?.isVisible = true
                    val salt = SaltGenerator.generateSalt()
                    val hashedPassword = PasswordHasher.hashPassword(
                        binding!!.passwordEditText.text.toString(), salt
                    )
                    val resetPassword = ResetPassword(
                        otp = otp.toString(), newPassword = hashedPassword, salt = salt
                    )
                    ApiUSR.postResetPassword(activity = requireActivity(),
                        resetPassword = resetPassword,
                        token = invalidToken.toString(),
                        onSuccess = {
                            binding?.loading?.isVisible = false
                            SessionManager(requireContext()).clearUserAccountNameList()
                            SessionManager(requireContext()).clearUserAccountTokenList()
                            AlertDialog.Builder(requireContext()).setTitle("密碼重設成功")
                                .setMessage("請重新登入").setPositiveButton("確定") { _, _ ->
                                }.setOnDismissListener { _ ->
                                    findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
                                }.show()
                        },
                        onFail = {
                            binding?.loading?.isVisible = false
                            AlertDialog.Builder(requireContext()).setTitle("密碼重設失敗")
                                .setMessage("請重新操作").setPositiveButton("確定") { _, _ ->
                                }.show()
                        }
                    )
                }
            }
        } else if (arg == "resetPassword") {
            binding?.tvNewPassword?.isVisible = false
            binding?.tilPassword?.isVisible = false
            binding?.tilSecPassword?.isVisible = false
            binding?.btnNext?.text = "取得驗證碼"
            binding?.tilOtp?.isVisible = false
            binding?.btnResendOtp?.isVisible = false
            binding?.tvOtp?.isVisible = false
            val accountNameLists = SessionManager(requireContext()).getUserAccountNameList()
            val accountTokenLists = SessionManager(requireContext()).getUserAccountTokenList()
            SessionManager(requireContext()).clearUserAccountNameList()
            SessionManager(requireContext()).clearUserAccountTokenList()
            val accountAdapter = ArrayAdapter(
                requireContext(), android.R.layout.simple_dropdown_item_1line, accountNameLists
            )
            binding?.accountSelectEditText?.setAdapter(accountAdapter)
            var selectedAccountToken =
                accountTokenLists[accountNameLists.indexOf(accountNameLists[0])]
            binding?.accountSelectEditText?.setOnItemClickListener { _, _, position, _ ->
                binding?.accountSelectEditText?.setText(accountNameLists[position])
                selectedAccountToken = accountTokenLists[position]
                Log.d("ResetPasswordFragment", "onViewCreated: $selectedAccountToken")
            }


            binding?.btnNext?.setOnClickListener {
                if (binding?.accountSelectEditText?.text.toString().isEmpty()) {
                    binding?.tilUsername?.error = "請選擇帳號"
                    return@setOnClickListener
                }
                binding?.tilUsername?.error = null
                binding?.btnNext?.isEnabled = false
                binding?.loading?.isVisible = true
                val accountOtp = AccountOtp(
                    selectedAccountToken
                )
                ApiUSR.postAccountOtpCheck(
                    activity = requireActivity(),
                    accountOtp = accountOtp,
                    onSuccess = {
                        binding?.loading?.isVisible = false
                        binding?.btnNext?.isEnabled = true
                        binding?.accountSelectEditText?.isEnabled = false
                        binding?.tilOtp?.isVisible = true
                        binding?.btnNext?.text = "下一步"
                        binding?.tilPassword?.isVisible = true
                        binding?.tilSecPassword?.isVisible = true
                        binding?.btnResendOtp?.isVisible = true
                        binding?.tvOtp?.isVisible = true

                        //倒數計時60秒以後才能再次取得驗證碼
                        binding?.btnResendOtp?.isEnabled = false
                        binding?.btnResendOtp?.backgroundTintList =
                            ColorStateList(
                                arrayOf(intArrayOf(0)),
                                intArrayOf(resources.getColor(R.color.gray)))
                        binding?.btnResendOtp?.setTextColor(resources.getColor(R.color.white))
                        timer = object : CountDownTimer(60000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                binding?.btnResendOtp?.text =
                                    "重寄(" + millisUntilFinished / 1000 + "s)"
                            }

                            override fun onFinish() {
                                binding?.btnResendOtp?.backgroundTintList =
                                    ColorStateList(
                                        arrayOf(intArrayOf(0)),
                                        intArrayOf(resources.getColor(R.color.SecondaryButtonColor)))
                                binding?.btnResendOtp?.setTextColor(resources.getColor(R.color.SecondaryButtonTextColor))
                                binding?.btnResendOtp?.text = getText(R.string.resent_otp).toString()
                                binding?.btnResendOtp?.isEnabled = true
                            }
                        }.start()

                        binding?.btnResendOtp?.setOnClickListener {
                            ApiUSR.postAccountOtpCheck(
                                activity = requireActivity(),
                                accountOtp = accountOtp,
                                onSuccess = {
                                    //倒數計時60秒以後才能再次取得驗證碼
                                    binding?.btnResendOtp?.isEnabled = false
                                    binding?.btnResendOtp?.backgroundTintList =
                                        ColorStateList(
                                            arrayOf(intArrayOf(0)),
                                            intArrayOf(resources.getColor(R.color.gray)))
                                    binding?.btnResendOtp?.setTextColor(resources.getColor(R.color.white))
                                    timer = object : CountDownTimer(60000, 1000) {
                                        override fun onTick(millisUntilFinished: Long) {
                                            binding?.btnResendOtp?.text =
                                                "重寄(" + millisUntilFinished / 1000 + "s)"
                                        }

                                        override fun onFinish() {
                                            binding?.btnResendOtp?.backgroundTintList =
                                                ColorStateList(
                                                    arrayOf(intArrayOf(0)),
                                                    intArrayOf(resources.getColor(R.color.SecondaryButtonColor)))
                                            binding?.btnResendOtp?.setTextColor(resources.getColor(R.color.SecondaryButtonTextColor))
                                            binding?.btnResendOtp?.text = getText(R.string.resent_otp).toString()
                                            binding?.btnResendOtp?.isEnabled = true
                                        }
                                    }.start()
                                },
                                onFail = {
                                    AlertDialog.Builder(requireContext())
                                        .setTitle("驗證碼取得失敗")
                                        .setMessage("請重新操作")
                                        .setPositiveButton("確定") { _, _ ->
                                        }.show()
                                }
                            )
                        }
                        binding?.btnNext?.setOnClickListener {
                            if (checkIsPasswordValid()) {
                                binding?.loading?.isVisible = true
                                val salt = SaltGenerator.generateSalt()
                                val hsahedPassword = PasswordHasher.hashPassword(
                                    binding!!.passwordEditText.text.toString(),
                                    salt
                                )
                                val resetPassword = ResetPassword(
                                    otp = binding?.otpEditText?.text.toString(),
                                    newPassword = hsahedPassword,
                                    salt = salt
                                )
                                ApiUSR.postResetPassword(
                                    activity = requireActivity(),
                                    resetPassword = resetPassword,
                                    token = selectedAccountToken,
                                    onSuccess = {
                                        binding?.loading?.isVisible = false
                                        SessionManager(requireContext()).clearUserAccountNameList()
                                        SessionManager(requireContext()).clearUserAccountTokenList()
                                        AlertDialog.Builder(requireContext())
                                            .setTitle("密碼重設成功")
                                            .setMessage("請重新登入")
                                            .setPositiveButton("確定") { _, _ ->
                                            }.setOnDismissListener { _ ->
                                                findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
                                            }.show()
                                    },
                                    onFail = {
                                        binding?.loading?.isVisible = false
                                    }
                                )
                            }
                        }
                    },
                    onFail = {
                        binding?.loading?.isVisible = false
                        binding?.btnNext?.isEnabled = true
                        AlertDialog.Builder(requireContext()).setTitle("驗證碼取得失敗")
                            .setMessage("請重新操作").setPositiveButton("確定") { _, _ ->
                            }.show()
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer = null
    }
}