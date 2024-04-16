package com.example.mobileapp

import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.viewbinding.ViewBinding
import com.example.mobileapp.databinding.ActivityNoConnectionBinding

abstract class BaseActivity<T: ViewBinding> : AppCompatActivity(), NetworkStateListener {

    private val networkChangeReceiver = NetworkChangeReceiver()

    private var isInternetAvailable = false

    private val noConnectionBinding: ActivityNoConnectionBinding by lazy {
        ActivityNoConnectionBinding.inflate(layoutInflater)
    }

    protected var isShouldStart = true


    protected abstract val screenBinding:T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))

        networkChangeReceiver.addListener(this)
        registerReceiver(networkChangeReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        noConnectionBinding.btnCheckConnection.setOnClickListener {
            if (isInternetAvailable) setContentView(screenBinding.root)
        }
    }

    override fun onStart() {
        super.onStart()

        ViewCompat.setOnApplyWindowInsetsListener(screenBinding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Apply the insets as a margin to the view. This solution sets
            // only the bottom, left, and right dimensions, but you can apply whichever
            // insets are appropriate to your layout. You can also update the view padding
            // if that's more appropriate.
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
            }

            noConnectionBinding.root.layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.MATCH_PARENT
            ).apply {
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
            }

            // Return CONSUMED if you don't want want the window insets to keep passing
            // down to descendant views.
            WindowInsetsCompat.CONSUMED
        }

        isShouldStart = isInternetAvailable
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }

    override fun onNetworkConnected() {
        isInternetAvailable = true
        isShouldStart = true
        onStart()
    }

    override fun onNetworkDisconnected() {
        isInternetAvailable = false
        isShouldStart = false
        setContentView(noConnectionBinding.root)
    }
}