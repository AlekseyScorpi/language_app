package com.example.mobileapp.signup

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.mobileapp.BaseActivity
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ActivitySignupBinding
import com.example.mobileapp.login.LoginActivity

class SignupActivity : BaseActivity<ActivitySignupBinding>() {

    private val fragList = listOf(
        SignupFragment1.newInstance(),
        SignupFragment2.newInstance(),
    )

    private var currentFragment: Int = 0

    override val screenBinding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val btnText = listOf(
            getString(R.string.signup_continue),
            getString(R.string.signup_complete),
        )

        val adapter = SignupVpAdapter(this, fragList)

        screenBinding.ivBack.setOnClickListener {
            currentFragment--
            if (currentFragment < 0) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                screenBinding.vpSignup.currentItem = currentFragment
            }
        }

        screenBinding.btnSignup.setOnClickListener {
            currentFragment++
            if (currentFragment > 1) {
                currentFragment = 1
            } else {
                screenBinding.vpSignup.currentItem = currentFragment
            }
        }

        screenBinding.vpSignup.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentFragment = position
                screenBinding.btnSignup.text = btnText[currentFragment]
            }
        })

        screenBinding.vpSignup.adapter = adapter
        screenBinding.vpSignup.currentItem = currentFragment

        val loginBeforeSpan = getString(R.string.signup_before_span)
        val loginSpan = getString(R.string.signup_span)

        val spannableLogin = SpannableString(loginSpan)

        val clickableSpanLogin = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                finish()
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@SignupActivity, R.color.blue)
                ds.isUnderlineText = false
            }
        }

        spannableLogin.setSpan(clickableSpanLogin,0, spannableLogin.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val combinedLoginText = SpannableStringBuilder().append(loginBeforeSpan).append(" ").append(spannableLogin)
        screenBinding.tvSignupToLogin.text = combinedLoginText
        screenBinding.tvSignupToLogin.movementMethod = LinkMovementMethod.getInstance()

        setContentView(screenBinding.root)
    }
}