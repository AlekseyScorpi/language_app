package com.example.mobileapp.exercises.audition

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.mobileapp.BaseActivity
import com.example.mobileapp.LanguageApplication
import com.example.mobileapp.R
import com.example.mobileapp.database.AuditionExercise
import com.example.mobileapp.database.UserInfo
import com.example.mobileapp.databinding.ActivityAuditionExerciseBinding
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class AuditionExerciseActivity : BaseActivity<ActivityAuditionExerciseBinding>() {

    private val gameManager: AuditionGameManager = AuditionGameManager()

    override val screenBinding: ActivityAuditionExerciseBinding by lazy {
        ActivityAuditionExerciseBinding.inflate(layoutInflater)
    }

    private var isNext: Boolean = false
    private var isSpeaking: Boolean = false
    private var word: String = ""
    private var userResult = ""

    private lateinit var speechIntent: Intent

    private lateinit var speechRecognizer: SpeechRecognizer

    private lateinit var pulseAnimation: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        requestMicrophonePermission()
        setContentView(screenBinding.root)
        lifecycleScope.launch {
            val exercise: AuditionExercise = getNewAudition()
            screenBinding.tvWord.text = exercise.word
            screenBinding.tvTranscription.text = exercise.transcription
            screenBinding.tvResultMessage.text = ""
            word = exercise.word
            screenBinding.btnAction.setText(R.string.audition_exercise_button_check)
        }
        pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_animation)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")

        val recognitionListener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
            }

            override fun onBeginningOfSpeech() {
            }

            override fun onRmsChanged(rmsdB: Float) {
            }

            override fun onBufferReceived(buffer: ByteArray?) {
            }

            override fun onEndOfSpeech() {
                screenBinding.btnAction.clearAnimation()
                screenBinding.btnAction.setText(R.string.audition_exercise_start_recognition)
            }

            override fun onError(error: Int) {
                screenBinding.btnAction.clearAnimation()
                screenBinding.btnAction.setText(R.string.audition_exercise_start_recognition)
                onResults(results = null)
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    userResult = matches[0]
                } else {
                    userResult = ""
                }
                screenBinding.btnAction.clearAnimation()
                isSpeaking = false
                isNext = true
                screenBinding.tvResultMessage.text =
                    getString(R.string.audition_exercise_result_title, userResult)
                lifecycleScope.launch {
                    if (userResult == word) {
                        updatePoints()
                        screenBinding.btnAction.setText(R.string.audition_exercise_button_correct)
                    } else {
                        gameManager.resetStreak()
                        screenBinding.btnAction.setText(R.string.audition_exercise_button_wrong)
                    }
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
            }
        }

        speechRecognizer.setRecognitionListener(recognitionListener)
    }

    private fun bind() {
        screenBinding.btnAction.setOnClickListener {
            lifecycleScope.launch {
                if (isNext) {
                    val exercise: AuditionExercise = getNewAudition()
                    screenBinding.tvWord.text = exercise.word
                    screenBinding.tvTranscription.text = exercise.transcription
                    screenBinding.tvResultMessage.text = ""
                    word = exercise.word
                    userResult = ""
                    screenBinding.btnAction.setText(R.string.audition_exercise_button_check)
                    isNext = false
                } else {
                    if (!isSpeaking) {
                        screenBinding.btnAction.startAnimation(pulseAnimation)
                        screenBinding.btnAction.setText(R.string.audition_exercise_start_listening)
                        speechRecognizer.startListening(speechIntent)
                        isSpeaking = true
                    }
                }
            }
        }

        screenBinding.ivBack.setOnClickListener {
            finish()
        }
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

    private suspend fun getNewAudition(): AuditionExercise {
        val exercise: AuditionExercise = try {
            LanguageApplication.supabaseClient.postgrest.from("random_guess_audition").select {
                limit(1)
            }.decodeSingle<AuditionExercise>()
        } catch (e: Exception) {
            println(e)
            AuditionExercise(0, "", "")
        }
        return exercise
    }

    private fun requestMicrophonePermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {

            }
            else -> {
                microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private val microphonePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {

        } else {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }
}