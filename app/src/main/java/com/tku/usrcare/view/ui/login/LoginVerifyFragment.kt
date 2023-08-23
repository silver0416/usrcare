package com.tku.usrcare.view.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.databinding.FragmentLoginVerifyBinding
import com.tku.usrcare.model.EmailVerify
import com.tku.usrcare.repository.SessionManager


class LoginVerifyFragment : Fragment() {
    private var _binding: FragmentLoginVerifyBinding? = null
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginVerifyBinding.inflate(inflater, container, false)
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

        val sessionManager = SessionManager(requireContext())

        val et1 = binding!!.etVerifyCode1
        val et2 = binding!!.etVerifyCode2
        val et3 = binding!!.etVerifyCode3
        val et4 = binding!!.etVerifyCode4
        val et5 = binding!!.etVerifyCode5
        val et6 = binding!!.etVerifyCode6

        et1.requestFocus()
        et2.isEnabled = false
        et3.isEnabled = false
        et4.isEnabled = false
        et5.isEnabled = false
        et6.isEnabled = false

        val imm = getSystemService(requireContext(), InputMethodManager::class.java)
        val etList = listOf(et1, et2, et3, et4, et5, et6)
        for (et in etList) {
            imm?.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
            //when paste OTP code
            et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s!!.length == 1) {
                        if (et != et6) {
                            etList[etList.indexOf(et) + 1].isEnabled = true
                            etList[etList.indexOf(et) + 1].requestFocus()
                            et.isEnabled = false
                        }
                    }
                    if (et1.text!!.isNotEmpty() && et2.text!!.isNotEmpty() && et3.text!!.isNotEmpty() && et4.text!!.isNotEmpty() && et5.text!!.isNotEmpty() && et6.text!!.isNotEmpty()) {
                        //開始驗證
                        et4.isEnabled = false
                        val enteredCode = et1.text.toString() + et2.text.toString() + et3.text.toString() + et4.text.toString() + et5.text.toString() + et6.text.toString()
                        val emailVerify = EmailVerify(sessionManager.getUserEmail().toString(),enteredCode)
                        val action = LoginVerifyFragmentDirections.actionLoginVerifyFragmentToSignUpFragment()
                        ApiUSR.postEmailVerify(requireActivity(),emailVerify , binding!! ,action,this@LoginVerifyFragment)

//                        if (sessionManager.getUserStatus() == "new") {
//                            if (enteredCode == sessionManager.getOTP()) {
//                                val action =
//                                    LoginVerifyFragmentDirections.actionLoginVerifyFragmentToSignUpFragment()
//                                findNavController().navigate(action)
//                            } else {
//                                //驗證失敗
//                                et1.text = null
//                                et1.isEnabled = true
//                                et2.text = null
//                                et2.isEnabled = false
//                                et3.text = null
//                                et3.isEnabled = false
//                                et4.text = null
//                                et4.isEnabled = false
//                                et5.text = null
//                                et5.isEnabled = false
//                                et6.text = null
//                                et6.isEnabled = false
//                                et1.requestFocus()
//                                imm?.showSoftInput(et1, InputMethodManager.SHOW_IMPLICIT)
//                            }
//                        }
//                        else{
//                            val authorization = Authorization(sessionManager.getUserPhone().toString(),enteredCode)
//                            if (authorization != null) {
//                                Log.d("authorization",authorization.toString())
//                                ApiUSR.postAuthorization(authorization,requireActivity(), binding!!)
//                            }
//                        }
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            //刪除鍵
            et.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    if (et != et1) {
                        et.text = null
                        etList[etList.indexOf(et) - 1].text = null
                        etList[etList.indexOf(et) - 1].isEnabled = true
                        etList[etList.indexOf(et) - 1].requestFocus()
                        et.isEnabled = false
                    }
                }
                false
            })
        }
    }
    private fun convertVerifyCode(verifyCode:String): ArrayList<String> {
        //將驗證碼轉換為六個數字
        val number = verifyCode.toInt()

        val digit1 = '0' + (number shr 20 and 0xF)
        val digit2 = '0' + (number shr 16 and 0xF)
        val digit3 = '0' + (number shr 12 and 0xF)
        val digit4 = '0' + (number shr 8 and 0xF)
        val digit5 = '0' + (number shr 4 and 0xF)
        val digit6 = '0' + (number and 0xF)

        return arrayListOf(digit1.toString(), digit2.toString(), digit3.toString(), digit4.toString(), digit5.toString(), digit6.toString())
    }
}