package com.tku.usrcare.view.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tku.usrcare.databinding.FragmentMainBinding
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.viewmodel.MainViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var pointsUpdateReceiver: BroadcastReceiver
    private lateinit var sessionManager: SessionManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(requireContext())
        val viewModelFactory = ViewModelFactory(sessionManager)
        mainViewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[MainViewModel::class.java]

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getPoints()
        val mainComposeView = binding?.mainComposeView
        mainComposeView?.setContent {
            MainPage()
        }

        pointsUpdateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val shouldUpdatePoints = intent?.getBooleanExtra("points", false) ?: false
                if (shouldUpdatePoints) {
                    mainViewModel.checkPoints()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter().apply {
            addAction("com.tku.usrcare.view.ui.main.MainFragment")
        }
        val receiverFlags = ContextCompat.RECEIVER_EXPORTED
        ContextCompat.registerReceiver(requireContext(), pointsUpdateReceiver, intentFilter, receiverFlags)

        mainViewModel.getPoints()
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(pointsUpdateReceiver)
    }

}