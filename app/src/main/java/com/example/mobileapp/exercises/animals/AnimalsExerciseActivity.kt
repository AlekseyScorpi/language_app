package com.example.mobileapp.exercises.animals

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.mobileapp.BaseActivity
import com.example.mobileapp.LanguageApplication
import com.example.mobileapp.R
import com.example.mobileapp.database.Animal
import com.example.mobileapp.databinding.ActivityAnimalsExerciseBinding
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class AnimalsExerciseActivity : BaseActivity<ActivityAnimalsExerciseBinding>() {

    private val gameManager = AnimalGameManager()

    private var correctAnswer: String = "\n\n\n"

    private lateinit var currentAnimal: Animal

    override val screenBinding: ActivityAnimalsExerciseBinding by lazy {
        ActivityAnimalsExerciseBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        setContentView(screenBinding.root)
    }

    private fun bind() {

        screenBinding.btnCheckAnimal.setOnClickListener {
            if (screenBinding.inputAnimalEditText.text.toString() == "") return@setOnClickListener
            val correctAnswer: Boolean = isCorrectAnswer()
            if (correctAnswer) {
                lifecycleScope.launch {
                    updatePoints()
                }
                setSuccessUI()
            } else {
                setWrongUI()
            }
        }

        screenBinding.ivBack.setOnClickListener {
            finish()
        }

        screenBinding.btnAnimalNext.setOnClickListener {
            setDefaultUI()
        }

        screenBinding.btnAnimalAgain.setOnClickListener {
            setDefaultUI()
        }
    }

    private fun setSuccessUI() {
        screenBinding.ivAnimalPhoto.visibility = View.GONE
        screenBinding.tvInputTitle.visibility = View.GONE
        screenBinding.btnCheckAnimal.visibility = View.GONE
        screenBinding.animalTextInputLayout.visibility = View.GONE
        screenBinding.tvResultIcon.text = getString(R.string.animal_good_answer_icon)
        screenBinding.tvAnimalMessage.text = getString(R.string.animal_exercise_good)
        screenBinding.btnAnimalNext.visibility = View.VISIBLE
        screenBinding.tvResultIcon.visibility = View.VISIBLE
        screenBinding.tvAnimalMessage.visibility = View.VISIBLE
        screenBinding.ivTopBlock.setBackgroundColor(getColor(R.color.green))
    }

    private fun setWrongUI() {
        screenBinding.ivAnimalPhoto.visibility = View.GONE
        screenBinding.tvInputTitle.visibility = View.GONE
        screenBinding.btnCheckAnimal.visibility = View.GONE
        screenBinding.animalTextInputLayout.visibility = View.GONE
        screenBinding.tvResultIcon.text = getString(R.string.animal_bad_answer_icon)
        screenBinding.tvAnimalMessage.text = getString(R.string.animal_exercise_bad)
        screenBinding.btnAnimalNext.visibility = View.VISIBLE
        screenBinding.tvResultIcon.visibility = View.VISIBLE
        screenBinding.tvAnimalMessage.visibility = View.VISIBLE
        screenBinding.ivTopBlock.setBackgroundColor(getColor(R.color.red))
        screenBinding.btnAnimalAgain.visibility = View.VISIBLE
    }

    private fun setDefaultUI() {
        screenBinding.btnAnimalNext.visibility = View.GONE
        screenBinding.tvResultIcon.visibility = View.GONE
        screenBinding.tvAnimalMessage.visibility = View.GONE
        screenBinding.ivTopBlock.setBackgroundColor(getColor(R.color.deep_blue))
        screenBinding.btnAnimalAgain.visibility = View.GONE
        screenBinding.ivAnimalPhoto.visibility = View.VISIBLE
        screenBinding.tvInputTitle.visibility = View.VISIBLE
        screenBinding.btnCheckAnimal.visibility = View.VISIBLE
        screenBinding.inputAnimalEditText.setText("")
        screenBinding.animalTextInputLayout.visibility = View.VISIBLE
    }

    private fun isCorrectAnswer(): Boolean {
        val userAnswer = screenBinding.inputAnimalEditText.text.toString()
        return userAnswer == correctAnswer
    }

    private suspend fun updatePoints() {

    }

    private suspend fun getNewAnimal() {
        currentAnimal = LanguageApplication.supabaseClient.postgrest.from("random_guess_animal").select {
            limit(1)
        }.decodeSingle<Animal>()
    }
}