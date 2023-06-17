package com.tku.usrcare.view.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.tku.usrcare.databinding.FragmentLoginBinding
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.MainActivity


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

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
        val sessionManager = SessionManager(requireContext())

        binding?.devLogin?.setOnClickListener(){
            val intent = Intent(activity, MainActivity::class.java)
            sessionManager.saveUserToken("dev")
            startActivity(intent)
        }
    }
}