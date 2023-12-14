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