package com.tku.usrcare.view.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.fragment.app.Fragment
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.databinding.FragmentMainBinding
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.ClockActivity
import com.tku.usrcare.view.KtvActivity
import com.tku.usrcare.view.LoginActivity
import com.tku.usrcare.view.ScaleActivity
import com.tku.usrcare.view.SettingActivity
import com.tku.usrcare.view.SignSignHappyActivity
import com.tku.usrcare.view.UnityActivity


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding
    private lateinit var pointsUpdateReceiver: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentMainBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sessionManager = SessionManager(requireContext())
        val intent = Intent(activity, LoginActivity::class.java)

        binding?.userName?.text = sessionManager.getUserName()
        ApiUSR.getPoints(requireActivity(), onSuccess = {
            binding?.btnPoints?.text = it.toString()
        })
        pointsUpdateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val shouldUpdatePoints = intent?.getBooleanExtra("points", false) ?: false
                Log.d("MainFragment", "shouldUpdatePoints: $shouldUpdatePoints")
                if (shouldUpdatePoints) {
                    ApiUSR.getPoints(requireActivity(), onSuccess = {
                        binding?.btnPoints?.text = it.toString()
                    })
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

        binding?.btnSign?.setOnClickListener{
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
        ApiUSR.getPoints(requireActivity(), onSuccess = {
            binding?.btnPoints?.text = it.toString()
        })
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(pointsUpdateReceiver)
    }

}