package com.tku.usrcare.view.ui.login

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.databinding.FragmentLoginBinding
import com.tku.usrcare.model.Login
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.MainActivity


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

    private fun emailChecker(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentLoginBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        binding?.loginButton?.setOnClickListener() {
            val login = Login(
                binding?.accountEditText?.text.toString()
            )
            val sessionManager = SessionManager(requireContext())
            sessionManager.saveUserAccount(binding?.accountEditText?.text.toString())
//            val action = LoginFragmentDirections.actionLoginFragmentToLoginVerifyFragment()
//            ApiUSR.postLogin(login, requireActivity(), binding!!, action, this)
        }
        binding?.btnBack?.setOnClickListener() {
            findNavController().navigateUp()
        }
    }
}