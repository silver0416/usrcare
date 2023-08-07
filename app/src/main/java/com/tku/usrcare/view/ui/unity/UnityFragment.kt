package com.tku.usrcare.view.ui.unity


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.tku.usrcare.databinding.FragmentUnityBinding
import com.tku.usrcare.repository.SessionManager

import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerForActivityOrService
import com.unity3d.player.a.a
import kotlin.system.exitProcess


class UnityFragment : Fragment() {
    protected var mUnityPlayer: UnityPlayer? = null
    private var _binding: FragmentUnityBinding? = null
    private val binding get() = _binding

    private fun sendApiTokenToUnity(value: String) {
        Log.d("UnityFragment", "sendApiTokenToUnity: $value")
        UnityPlayer.UnitySendMessage("Receiver", "SetApiToken", value)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUnityBinding.inflate(inflater, container, false)
        val sessionManager = SessionManager(requireContext())

        //embedding unity view
        mUnityPlayer = UnityPlayerForActivityOrService(activity)
        val view = mUnityPlayer!!.frameLayout
        binding?.fragmentUnity?.addView(view)
        mUnityPlayer!!.frameLayout.requestFocus()
        mUnityPlayer!!.windowFocusChanged(true)



        sessionManager.getUserToken()?.let { sendApiTokenToUnity(it) }

        binding?.btnExit?.setOnClickListener {
            val alertDialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            alertDialog.setTitle("Exit")
            alertDialog.setMessage("確定要退出遊戲嗎")
            alertDialog.setPositiveButton("是的") { _, _ ->
                exitProcess(0) // TODO : 待修正未呼叫主程式而閃退
            }
            alertDialog.setNegativeButton("取消") { _, _ ->
            }
            alertDialog.show()
        }
        return binding!!.root
    }

    override fun onDestroy() {
        mUnityPlayer!!.quit()
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mUnityPlayer!!.pause()
    }

    override fun onResume() {
        super.onResume()
        mUnityPlayer!!.resume()
    }
}