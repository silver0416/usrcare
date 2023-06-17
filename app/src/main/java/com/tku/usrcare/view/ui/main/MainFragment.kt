package com.tku.usrcare.view.ui.main

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
import com.tku.usrcare.databinding.FragmentMainBinding
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.LoginActivity

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentMainBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sessionManager = SessionManager(requireContext())
        val intent = requireActivity().intent
        binding?.devLogout?.setOnClickListener(){
            sessionManager.clearUserToken()
            intent.setClass(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

    }

}