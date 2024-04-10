package com.example.mobileapp.login

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val loginBinding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val registrationBeforeSpan = getString(R.string.login_registration_before_span)
        val registrationSpan = getString(R.string.login_registration_span)

        val spannableRegistration = SpannableString(registrationSpan)

        spannableRegistration.setSpan(ForegroundColorSpan(getColor(R.color.blue)), 0, spannableRegistration.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val combinedRegistrationText = SpannableStringBuilder().append(registrationBeforeSpan).append(" ").append(spannableRegistration)
        loginBinding.tvSignup.text = combinedRegistrationText


        val googleBeforeSpan = getString(R.string.login_google_before_span)
        val googleSpan = getString(R.string.login_google_span)
        val googleAfterSpan = getString(R.string.login_google_after_span)

        val spannableGoogle = SpannableString(googleSpan)

        spannableGoogle.setSpan(ForegroundColorSpan(getColor(R.color.blue)), 0, spannableGoogle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val combinedGoogleText = SpannableStringBuilder().append(googleBeforeSpan).append(" ").append(spannableGoogle).append(" ").append(googleAfterSpan)
        loginBinding.tvGoogle.text = combinedGoogleText
        setContentView(loginBinding.root)

    }
}