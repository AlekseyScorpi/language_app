package com.example.mobileapp.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.mobileapp.BaseActivity
import com.example.mobileapp.LanguageApplication
import com.example.mobileapp.language_select.LanguageSelectActivity
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ActivityOnboardingBinding

class OnboardingActivity : BaseActivity<ActivityOnboardingBinding>() {
    override val screenBinding: ActivityOnboardingBinding by lazy {
        ActivityOnboardingBinding.inflate(layoutInflater)
    }

    private val fragList = listOf(
        Onboarding1.newInstance(),
        Onboarding2.newInstance(),
        Onboarding3.newInstance(),
        )

    private var currentFragment: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val btnText = listOf(
            getString(R.string.onboarding_button_1),
            getString(R.string.onboarding_button_2),
            getString(R.string.onboarding_button_3),
        )

        currentFragment = LanguageApplication.localStorage.getInt("OnboardingFragment")
        setUIById(currentFragment, btnText[currentFragment])
        val adapter = OnboardingVpAdapter(this, fragList)

        screenBinding.vpOnboarding.adapter = adapter
        screenBinding.vpOnboarding.currentItem = currentFragment

        screenBinding.vpOnboarding.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentFragment = position
                LanguageApplication.localStorage.saveInt("OnboardingFragment", currentFragment)
                setUIById(currentFragment, btnText[currentFragment])
            }
        })

        screenBinding.btnOnboardingNext.setOnClickListener {
            if (currentFragment < 2) {
                screenBinding.vpOnboarding.currentItem = ++currentFragment
                LanguageApplication.localStorage.saveInt("OnboardingFragment", currentFragment)
                setUIById(currentFragment, btnText[currentFragment])
            } else {
                LanguageApplication.localStorage.saveInt("OnboardingFragment", -1)
                startActivity(Intent(this, LanguageSelectActivity::class.java))
                finish()
            }
        }

        screenBinding.tvSkip.setOnClickListener {
            currentFragment = -1
            LanguageApplication.localStorage.saveInt("OnboardingFragment", currentFragment)
            startActivity(Intent(this, LanguageSelectActivity::class.java))
            finish()
        }

        setContentView(screenBinding.root)

    }

    private fun setUIById(id: Int, btnText: String) {
        screenBinding.btnOnboardingNext.text = btnText
        when (id) {
            0 -> {
                screenBinding.ivCircle1.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.circle_active
                ))
                screenBinding.ivCircle2.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.circle_inactive
                ))
                screenBinding.ivCircle3.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.circle_inactive
                ))
            }
            1 -> {
                screenBinding.ivCircle1.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.circle_inactive
                ))
                screenBinding.ivCircle2.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.circle_active
                ))
                screenBinding.ivCircle3.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.circle_inactive
                ))
            }
            2 -> {
                screenBinding.ivCircle1.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.circle_inactive
                ))
                screenBinding.ivCircle2.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.circle_inactive
                ))
                screenBinding.ivCircle3.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.circle_active
                ))
            }
        }
    }
}