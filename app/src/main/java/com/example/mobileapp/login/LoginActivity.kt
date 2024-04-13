package com.example.mobileapp.login

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.mobileapp.BaseActivity
import com.example.mobileapp.LanguageApplication
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ActivityLoginBinding
import com.example.mobileapp.isEmailValid
import com.example.mobileapp.isPasswordValid
import com.example.mobileapp.language_select.LanguageSelectActivity
import com.example.mobileapp.main.MainActivity
import com.example.mobileapp.showInvalidDataDialog
import com.example.mobileapp.showNoSignInDialog
import com.example.mobileapp.signup.SignupActivity
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override val screenBinding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var email: String
    private lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenBinding.ivBack.setOnClickListener {
            startActivity(Intent(this, LanguageSelectActivity::class.java))
            finish()
        }


        val registrationBeforeSpan = getString(R.string.login_registration_before_span)
        val registrationSpan = getString(R.string.login_registration_span)

        val spannableRegistration = SpannableString(registrationSpan)

        val clickableSpanRegistration = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
                finish()
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@LoginActivity, R.color.blue)
                ds.isUnderlineText = false
            }
        }

        spannableRegistration.setSpan(clickableSpanRegistration,0, spannableRegistration.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val combinedRegistrationText = SpannableStringBuilder().append(registrationBeforeSpan).append(" ").append(spannableRegistration)
        screenBinding.tvSignup.text = combinedRegistrationText
        screenBinding.tvSignup.movementMethod = LinkMovementMethod.getInstance()


        val googleBeforeSpan = getString(R.string.login_google_before_span)
        val googleSpan = getString(R.string.login_google_span)
        val googleAfterSpan = getString(R.string.login_google_after_span)

        val spannableGoogle = SpannableString(googleSpan)

        spannableGoogle.setSpan(ForegroundColorSpan(getColor(R.color.blue)), 0, spannableGoogle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val combinedGoogleText = SpannableStringBuilder().append(googleBeforeSpan).append(" ").append(spannableGoogle).append(" ").append(googleAfterSpan)
        screenBinding.tvGoogle.text = combinedGoogleText
        setContentView(screenBinding.root)

        screenBinding.btnLogin.setOnClickListener {
            email = screenBinding.inputEmailEditText.text.toString()
            password = screenBinding.inputPasswordEditText.text.toString()

            if (!isEmailValid(email) or !isPasswordValid(password)) {
                showInvalidDataDialog(this)
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    LanguageApplication.supabaseClient.auth.signInWith(Email) {
                        email = screenBinding.inputEmailEditText.text.toString()
                        password = screenBinding.inputPasswordEditText.text.toString()
                    }
                    // good sign in
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()

                } catch (e: Exception) {
                    showNoSignInDialog(this@LoginActivity)
                }
            }
        }

    }
}