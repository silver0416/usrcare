package com.tku.usrcare.view.ui.login

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
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
import com.tku.usrcare.databinding.FragmentSignUpUserDetailBinding
import com.tku.usrcare.model.RegisterAccount
import com.tku.usrcare.repository.ImageSaver
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.MainActivity
import com.tku.usrcare.view.ui.login.PasswordHasher.Companion.hashPassword
import java.security.MessageDigest
import java.util.Random

class SignUpUserDetailFragment : Fragment() {
    private var _binding: FragmentSignUpUserDetailBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpUserDetailBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            ImageSaver().deleteImageFromInternalStorage(
                "avatar",
                requireContext()
            )
            findNavController().navigateUp()
        }

        binding?.btnBack?.setOnClickListener {
            ImageSaver().deleteImageFromInternalStorage(
                "avatar",
                requireContext()
            )
            findNavController().navigateUp()
        }

        val sessionManager = SessionManager(requireContext())

        val account = SignUpUserDetailFragmentArgs.fromBundle(requireArguments()).account
        val salt = SaltGenerator.generateSalt()
        val password =
            hashPassword(SignUpUserDetailFragmentArgs.fromBundle(requireArguments()).password, salt)
        var birthday = ""
        val email = sessionManager.getUserEmail().toString()
        val idToken = SignUpUserDetailFragmentArgs.fromBundle(requireArguments()).idToken
        val arg = SignUpUserDetailFragmentArgs.fromBundle(requireArguments()).arg


        binding?.birthdayButton?.setOnClickListener {
            //跳出系統日期選擇器
            val datePicker = DatePickerDialog(
                requireContext(),
                R.style.CustomDatePickerDialogTheme,
                { _, year, month, dayOfMonth ->
                    birthday = "$year-${month + 1}-$dayOfMonth"
                    binding?.birthdayButton?.text = birthday
                }, 2023, 8, 7
            )
            datePicker.show()
        }
        val genderOpt = arrayOf("男", "女")
        val adapterGender = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            genderOpt
        )
        binding?.genderEditText?.setAdapter(adapterGender)
        val cityOpt = arrayOf(
            "台北市",
            "新北市",
            "基隆市",
            "桃園市",
            "新竹市",
            "新竹縣",
            "苗栗縣",
            "台中市",
            "彰化縣",
            "南投縣",
            "雲林縣",
            "嘉義市",
            "嘉義縣",
            "台南市",
            "高雄市",
            "屏東縣",
            "宜蘭縣",
            "花蓮縣",
            "台東縣",
            "澎湖縣"
        )
        val adapterCity = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            cityOpt
        )
        binding?.cityEditText?.setAdapter(adapterCity)


        binding?.btnNext?.setOnClickListener {
            val name = binding?.nameEditText?.text.toString()
            val gender = if (binding?.genderEditText?.text.toString() == "男") {
                "male"
            } else {
                "female"
            }
            val phone = binding?.phoneEditText?.text.toString()
            val city = binding?.cityEditText?.text.toString()
            val neighbor = binding?.neighborEditText?.text.toString()
            val district = binding?.districtEditText?.text.toString()
            val address = binding?.addressEditText?.text.toString()
            val eName = binding?.emerContactEditText?.text.toString()
            val ePhone = binding?.emerPhoneEditText?.text.toString()
            val eRelation = binding?.emerRelationText?.text.toString()

            //檢查name、gender、phone、city、neighbor、district、address、eName、ePhone、eRelation是否為空
            var pass = validateInputs(
                name,
                gender,
                phone,
                city,
                neighbor,
                district,
                address,
                eName,
                ePhone,
                eRelation
            )
            //檢查生日是否為空
            if (birthday == "") {
                pass = false
                AlertDialog.Builder(requireContext())
                    .setTitle("註冊失敗")
                    .setMessage("生日不可為空")
                    .setPositiveButton("確定", null)
                    .show()
            }

            if (pass) {
                if (arg == "normal") {
                    val registerAccount = RegisterAccount(
                        account,
                        password,
                        salt,
                        email,
                        name,
                        gender,
                        birthday,
                        phone,
                        city,
                        district,
                        neighbor,
                        address,
                        eName,
                        ePhone,
                        eRelation,
                        null
                    )
                    ApiUSR.postRegisterAccount(
                        requireActivity(),
                        registerAccount,
                        binding!!,
                        onSuccess = {
                            sessionManager.saveUserToken(it)
                            sessionManager.saveUserName(name)
                            sessionManager.saveUserPassword(password)
                            sessionManager.saveUserPhone(phone)
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                        },
                        onError = {
                            AlertDialog.Builder(requireContext())
                                .setTitle("註冊失敗")
                                .setMessage(it)
                                .setPositiveButton("確定", null)
                                .show()
                        }
                    )
                } else if (arg == "google") {
                    val registerAccount = RegisterAccount(
                        account,
                        password,
                        salt,
                        email,
                        name,
                        gender,
                        birthday,
                        phone,
                        city,
                        district,
                        neighbor,
                        address,
                        eName,
                        ePhone,
                        eRelation,
                        idToken
                    )
                    ApiUSR.postGoogleOAuthRegisterAccount(
                        requireActivity(),
                        registerAccount,
                        binding!!,
                        onSuccess = {
                            sessionManager.saveUserToken(it)
                            sessionManager.saveUserName(name)
                            sessionManager.saveUserPassword(password)
                            sessionManager.saveUserPhone(phone)
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                        },
                        onError = {
                            AlertDialog.Builder(requireContext())
                                .setTitle("註冊失敗")
                                .setMessage(it)
                                .setPositiveButton("確定", null)
                                .show()
                        }
                    )
                } else if (arg == "line") {
                    val registerAccount = RegisterAccount(
                        account,
                        password,
                        salt,
                        email,
                        name,
                        gender,
                        birthday,
                        phone,
                        city,
                        district,
                        neighbor,
                        address,
                        eName,
                        ePhone,
                        eRelation,
                        idToken
                    )
                    ApiUSR.postLineOAuthRegisterAccount(
                        requireActivity(),
                        registerAccount,
                        binding!!,
                        onSuccess = {
                            sessionManager.saveUserToken(it)
                            sessionManager.saveUserName(name)
                            sessionManager.saveUserPassword(password)
                            sessionManager.saveUserPhone(phone)
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                        },
                        onError = {
                            AlertDialog.Builder(requireContext())
                                .setTitle("註冊失敗")
                                .setMessage(it)
                                .setPositiveButton("確定", null)
                                .show()
                        }
                    )
                }
            }
        }
    }

    private fun validateInputs(
        name: String?, gender: String?, phone: String?, city: String?,
        neighbor: String?, district: String?, address: String?,
        eName: String?, ePhone: String?, eRelation: String?
    ): Boolean {
        //如果為空則在其edittext提示
        if (name.isNullOrEmpty()) binding?.tilName?.error = "不可為空"
        if (gender.isNullOrEmpty()) binding?.tilGender?.error = "不可為空"
        if (city.isNullOrEmpty()) binding?.tilCity?.error = "不可為空"
        if (neighbor.isNullOrEmpty()) binding?.tilNeighbor?.error = "不可為空"
        if (district.isNullOrEmpty()) binding?.tilDistrict?.error = "不可為空"
        // 檢查是否為空或空字串
        return !(
                name.isNullOrEmpty() || gender.isNullOrEmpty() ||
                        city.isNullOrEmpty() || neighbor.isNullOrEmpty() || district.isNullOrEmpty()
                )
    }
}

class PasswordHasher {
    companion object {
        fun hashPassword(password: String, salt: String): String {
            // 將密碼與salt結合
            val combinedPassword = password + salt

            // 使用SHA-256進行雜湊
            val bytes = MessageDigest.getInstance("SHA-256").digest(combinedPassword.toByteArray())

            // 將雜湊後的bytes轉換為十六進制的字符串
            return bytes.joinToString("") { "%02x".format(it) }
        }
    }
}

class SaltGenerator {
    companion object {
        fun generateSalt(): String {
            val salt = StringBuilder()
            val random = Random()
            for (i in 0..31) {
                when (random.nextInt(3)) {
                    0 -> salt.append((random.nextInt(26) + 65).toChar())
                    1 -> salt.append((random.nextInt(26) + 97).toChar())
                    2 -> salt.append((random.nextInt(10) + 48).toChar())
                }
            }
            return salt.toString()
        }
    }
}