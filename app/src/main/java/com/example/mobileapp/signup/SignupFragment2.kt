package com.example.mobileapp.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobileapp.databinding.FragmentSignup2Binding


class SignupFragment2 : Fragment() {

    private lateinit var binding: FragmentSignup2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignup2Binding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = SignupFragment2()
    }

    public fun getPassword(): String {
        return binding.inputPasswordEditText.text.toString()
    }

    public fun getConfirm(): String {
        return binding.inputConfirmEditText.text.toString()
    }

}