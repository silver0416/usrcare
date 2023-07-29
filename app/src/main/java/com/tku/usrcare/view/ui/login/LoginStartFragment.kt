package com.tku.usrcare.view.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.tku.usrcare.R
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
            val action =
                LoginStartFragmentDirections.actionLoginStartFragmentToSignUpEmailFragment()
            findNavController().navigate(action)
        }
    }
}