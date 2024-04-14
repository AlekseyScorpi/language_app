package com.example.mobileapp.user_profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mobileapp.BaseActivity
import com.example.mobileapp.databinding.ActivityChangeImageBinding
import com.oginotihiro.cropview.CropView

class ChangeImageActivity : BaseActivity<ActivityChangeImageBinding>() {

    private val cropView by lazy { findViewById<View>(com.example.mobileapp.R.id.cropView) as CropView }

    override val screenBinding: ActivityChangeImageBinding by lazy {
        ActivityChangeImageBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenBinding.btnSetPhoto.setOnClickListener {
            val croppedImage = cropView.output

        }

        setContentView(screenBinding.root)

        startImagePicker()
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImageUri = result.data?.data
            selectedImageUri?.let { uri ->
                // Используйте uri здесь для передачи в cropView
                cropView.of(uri)
                    .withAspect(0, 0)
                    .withOutputSize(100, 100)
                    .initialize(this)
            }
        }
    }

    private fun startImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }
}