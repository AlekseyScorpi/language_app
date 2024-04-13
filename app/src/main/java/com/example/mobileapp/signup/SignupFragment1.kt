package com.example.mobileapp.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobileapp.databinding.FragmentSignup1Binding


class SignupFragment1 : Fragment() {

    private lateinit var binding: FragmentSignup1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignup1Binding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = SignupFragment1()
    }

    public fun getFirstName(): String {
        return binding.inputFirstNameEditText.text.toString()
    }

    public fun getSecondName(): String {
        return binding.inputSecondNameEditText.text.toString()
    }

    public fun getEmail(): String {
        return binding.inputEmailEditText.text.toString()
    }
}