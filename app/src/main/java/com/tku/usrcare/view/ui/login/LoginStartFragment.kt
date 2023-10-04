package com.tku.usrcare.view.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tku.usrcare.databinding.FragmentLoginStartBinding


class LoginStartFragment : Fragment() {
    private var _binding: FragmentLoginStartBinding? = null
    private val binding get() = _binding

    companion object {
        fun newInstance() = LoginStartFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginStartBinding.inflate(inflater, container, false)

        return binding!!.root
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
    }
}