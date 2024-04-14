package com.example.mobileapp.exercises.animals

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.mobileapp.BaseActivity
import com.example.mobileapp.LanguageApplication
import com.example.mobileapp.R
import com.example.mobileapp.database.Animal
import com.example.mobileapp.database.UserInfo
import com.example.mobileapp.databinding.ActivityAnimalsExerciseBinding
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class AnimalsExerciseActivity : BaseActivity<ActivityAnimalsExerciseBinding>() {

    private val gameManager = AnimalGameManager()

    private lateinit var currentAnimal: Animal

    override val screenBinding: ActivityAnimalsExerciseBinding by lazy {
        ActivityAnimalsExerciseBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        lifecycleScope.launch {
            currentAnimal = getNewAnimal()
            setDefaultUI()
            setContentView(screenBinding.root)
        }
    }

    private fun bind() {

        screenBinding.btnCheckAnimal.setOnClickListener {
            if (screenBinding.inputAnimalEditText.text.toString() == "") return@setOnClickListener
            val correctAnswer: Boolean = isCorrectAnswer()
            if (correctAnswer) {
                lifecycleScope.launch {
                    updatePoints()
                    setSuccessUI()
                }
            } else {
                gameManager.resetStreak()
                setWrongUI()
            }
        }

        screenBinding.ivBack.setOnClickListener {
            finish()
        }

        screenBinding.btnAnimalNext.setOnClickListener {
            lifecycleScope.launch {
                currentAnimal = getNewAnimal()
                setDefaultUI()
            }
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
        screenBinding.tvAnimalMessage.text = getString(R.string.animal_exercise_bad, currentAnimal.correctAnswer)
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
        screenBinding.ivAnimalPhoto.load(currentAnimal.imageUrl) {
            fallback(R.drawable.default_user_photo)
            transformations(RoundedCornersTransformation(20f))
        }
    }

    private fun isCorrectAnswer(): Boolean {
        val userAnswer = screenBinding.inputAnimalEditText.text.toString()
        return userAnswer == currentAnimal.correctAnswer
    }

    private suspend fun updatePoints() {
        val user = LanguageApplication.supabaseClient.auth.currentUserOrNull()
        val userInfo = LanguageApplication.supabaseClient.postgrest.from("user_info").select {
            filter { eq("id", user?.id ?: "") }
        }.decodeSingle<UserInfo>()

        val newPoints: Double = userInfo.points + gameManager.getPoints()

        LanguageApplication.supabaseClient.from("user_info").update(
            {
                set("points", newPoints)
            }
        ) {
            filter {
                eq("id", userInfo.id)
            }
        }


    }

    private suspend fun getNewAnimal(): Animal {
        val animal: Animal = try {
            LanguageApplication.supabaseClient.postgrest.from("random_guess_animal").select {
                limit(1)
            }.decodeSingle<Animal>()
        } catch (e: Exception) {
            println(e)
            Animal(0, "", "")
        }
        return animal
    }
}