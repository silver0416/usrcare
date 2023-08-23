package com.tku.usrcare.view.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tku.usrcare.databinding.FragmentMainBinding
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.ClockActivity
import com.tku.usrcare.view.LoginActivity
import com.tku.usrcare.view.ScaleActivity
import com.tku.usrcare.view.SettingActivity
import com.tku.usrcare.view.UnityActivity


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
        val intent = Intent(activity, LoginActivity::class.java)

        binding?.userName?.text = sessionManager.getUserName()

        binding?.btnSetting?.setOnClickListener {
            intent.setClass(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }

        binding?.btnBrainGame?.setOnClickListener {
            intent.setClass(requireContext(), UnityActivity::class.java)
            startActivity(intent)
        }

        binding?.btnClockReminder?.setOnClickListener {
            intent.setClass(requireContext(), ClockActivity::class.java)
            startActivity(intent)
        }

        binding?.btnMoodScale?.setOnClickListener {
            intent.setClass(requireContext(), ScaleActivity::class.java)
            startActivity(intent)
        }

    }

}