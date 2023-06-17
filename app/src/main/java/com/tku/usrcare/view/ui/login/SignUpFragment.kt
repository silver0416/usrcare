package com.tku.usrcare.view.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tku.usrcare.databinding.FragmentLoginSignUpBinding

class SignUpFragment : Fragment() {
    private var _binding: FragmentLoginSignUpBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentLoginSignUpBinding.inflate(inflater, container, false)
        return binding!!.root
    }
}