package com.tku.usrcare.view.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tku.usrcare.databinding.FragmentMainBinding
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.ClockActivity
import com.tku.usrcare.view.KtvActivity
import com.tku.usrcare.view.LoginActivity
import com.tku.usrcare.view.ScaleActivity
import com.tku.usrcare.view.SettingActivity
import com.tku.usrcare.view.SignSignHappyActivity
import com.tku.usrcare.view.UnityActivity
import com.tku.usrcare.viewmodel.MainFragmentViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding
    private lateinit var mainFragmentViewModel: MainFragmentViewModel
    private lateinit var pointsUpdateReceiver: BroadcastReceiver
    private lateinit var sessionManager: SessionManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(requireContext())
        val viewModelFactory = ViewModelFactory(sessionManager)
        mainFragmentViewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[MainFragmentViewModel::class.java]

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val intent = Intent(activity, LoginActivity::class.java)
        mainFragmentViewModel.getPoints()
        mainFragmentViewModel.points.observe(viewLifecycleOwner) { it ->
            binding?.btnPoints?.text = it.toString()
        }

        binding?.userName?.text = mainFragmentViewModel.userName
        pointsUpdateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val shouldUpdatePoints = intent?.getBooleanExtra("points", false) ?: false
                if (shouldUpdatePoints) {
                    mainFragmentViewModel.getPoints()
                    binding?.btnPoints?.text = mainFragmentViewModel.points.value.toString()
                }
            }
        }

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

        binding?.btnSign?.setOnClickListener {
            intent.setClass(requireContext(), SignSignHappyActivity::class.java)
            startActivity(intent)
        }

        binding?.btnSaturdayKTV?.setOnClickListener {
            intent.setClass(requireContext(), KtvActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("com.tku.usrcare.view.ui.main.MainFragment")
        requireActivity().registerReceiver(pointsUpdateReceiver, intentFilter)
        mainFragmentViewModel.getPoints()
        binding?.btnPoints?.text = mainFragmentViewModel.points.value.toString()
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(pointsUpdateReceiver)
    }

}