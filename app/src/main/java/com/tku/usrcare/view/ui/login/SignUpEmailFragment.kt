package com.tku.usrcare.view.ui.login
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.tku.usrcare.R
import com.tku.usrcare.databinding.FragmentSignUpBinding
import com.tku.usrcare.databinding.FragmentSignUpEmailBinding
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.MainActivity

class SignUpEmailFragment : Fragment() {
    private var _binding: FragmentSignUpEmailBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentSignUpEmailBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        binding?.btnNext?.setOnClickListener {
            val action =
                SignUpEmailFragmentDirections.actionSignUpEmailFragmentToLoginVerifyFragment()
//            findNavController(it).navigate(action)
            SessionManager(requireContext()).saveUserToken("Test")
            activity?.startActivity(Intent(activity, MainActivity::class.java))
        }
        binding?.btnBack?.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}