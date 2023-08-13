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
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.databinding.FragmentSignUpUserDetailBinding
import com.tku.usrcare.model.RegisterAccount
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.MainActivity
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
            findNavController().navigateUp()
        }
        val sessionManager = SessionManager(requireContext())

        val account = SignUpUserDetailFragmentArgs.fromBundle(requireArguments()).account
        val password = SignUpUserDetailFragmentArgs.fromBundle(requireArguments()).password
        val name = binding?.nameEditText?.text.toString()
        val gender = binding?.genderEditText?.text.toString()
        val phone = binding?.phoneEditText?.text.toString()
        var birthday = ""
        val city = binding?.cityEditText?.text.toString()
        val district = binding?.neighbourEditText?.text.toString()
        val address = binding?.addressEditText?.text.toString()
        val salt = SaltGenerator.generateSalt()
        val email = sessionManager.getUserEmail().toString()
        val e_name = binding?.emerContactEditText?.text.toString()
        val e_phone = binding?.emerPhoneEditText?.text.toString()
        val e_relation = binding?.emerRelationText?.text.toString()

        binding?.birthdayButton?.setOnClickListener {
            //跳出系統日期選擇器
            val datePicker = DatePickerDialog(requireContext(),
//                R.style.CustomDatePickerDialogTheme,
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

        val registerAccount: RegisterAccount = RegisterAccount(
            account,
            password,
            salt,
            email,
            name,
            gender,
            phone,
            birthday,
            city,
            district,
            address,
            e_name,
            e_phone,
            e_relation
        )
        binding?.btnNext?.setOnClickListener{
            
        }
    }
}

class SaltGenerator {
    companion object {
        fun generateSalt(): String {
            val salt = StringBuilder()
            val random = Random()
            for (i in 0..31) {
                when (random.nextInt()) {
                    0 -> salt.append((random.nextInt(26) + 65).toChar())
                    1 -> salt.append((random.nextInt(26) + 97).toChar())
                    2 -> salt.append((random.nextInt(10) + 48).toChar())
                }
            }
            return salt.toString()
        }
    }
}