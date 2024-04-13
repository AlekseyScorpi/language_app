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
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.mobileapp.BaseActivity
import com.example.mobileapp.LanguageApplication
import com.example.mobileapp.R
import com.example.mobileapp.database.UserInfo
import com.example.mobileapp.databinding.ActivitySignupBinding
import com.example.mobileapp.isEmailValid
import com.example.mobileapp.isNameValid
import com.example.mobileapp.isPasswordValid
import com.example.mobileapp.login.LoginActivity
import com.example.mobileapp.main.MainActivity
import com.example.mobileapp.showEmailIsBusy
import com.example.mobileapp.showInvalidDataDialog
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonArray

class SignupActivity : BaseActivity<ActivitySignupBinding>() {

    private val fragList = listOf(
        SignupFragment1.newInstance(),
        SignupFragment2.newInstance(),
    )

    private var firstName = ""
    private var secondName = ""
    private var email = ""
    private var password = ""
    private var confirm = ""

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
            lifecycleScope.launch {
                val fragment1 = adapter.getFragmentByPosition(0) as SignupFragment1
                val fragment2 = adapter.getFragmentByPosition(1) as SignupFragment2
                if (currentFragment == 0) {
                    firstName = fragment1.getFirstName()
                    secondName = fragment1.getSecondName()
                    email = fragment1.getEmail()
                    if (!isEmailValid(email) or !isNameValid(firstName) or !isNameValid(secondName)) {
                        showInvalidDataDialog(this@SignupActivity)
                        return@launch
                    }

                    val emailCounts =
                        LanguageApplication.supabaseClient.postgrest.from("user_info").select {
                            filter { eq("user_email", email) }
                        }.decodeAs<JsonArray>().size

                    if (emailCounts > 0) {
                        showEmailIsBusy(this@SignupActivity)
                        return@launch
                    }

                } else {
                    password = fragment2.getPassword()
                    confirm = fragment2.getConfirm()
                    if (!isPasswordValid(password) or !isPasswordValid(confirm) or (password != confirm)) {
                        showInvalidDataDialog(this@SignupActivity)
                        return@launch
                    }
                }
                currentFragment++
                if (currentFragment > 1) {

                    try {

                        LanguageApplication.supabaseClient.auth.clearSession()

                        val emailResult = LanguageApplication.supabaseClient.auth.signUpWith(Email) {
                            email = this@SignupActivity.email
                            password = this@SignupActivity.password
                        }

                        val user = LanguageApplication.supabaseClient.auth.retrieveUserForCurrentSession()
                        
                        val userInfo = UserInfo(user.id, firstName, secondName, email)

                        LanguageApplication.supabaseClient.postgrest.from("user_info").insert(userInfo)

                        startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                        finish()

                    } catch (e: Exception) {
                        AlertDialog.Builder(this@SignupActivity)
                            .setTitle("Ошибка")
                            .setMessage(e.message)
                            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                            .show()
                        //showNoSignInDialog(this@SignupActivity)
                    }
                } else {
                    screenBinding.vpSignup.currentItem = currentFragment
                }
            }
        }

        screenBinding.vpSignup.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentFragment = position
                screenBinding.btnSignup.text = btnText[currentFragment]
            }
        })

        // forbid flipping
        screenBinding.vpSignup.isUserInputEnabled = false

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