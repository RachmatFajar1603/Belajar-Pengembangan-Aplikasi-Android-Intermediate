package com.example.mystoryapp.ui

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.mystoryapp.AppPreferences
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appPreferences = AppPreferences(this)

        loginViewModel.message.observe(this) {
            val user = loginViewModel.userlogin.value
            checkLoginUser(it, loginViewModel.isError, user?.loginResult?.token, appPreferences)
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        if (appPreferences.isLoggedIn) {
            val intent = Intent(this, StoryActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            if (binding.edLoginEmail.isValidEmail && binding.edLoginPassword.isValidPass) {
                val email = binding.edLoginEmail.text.toString()
                val pass = binding.edLoginPassword.text.toString()
                loginViewModel.getLoginUser(email, pass)
            }
        }

        binding.signIn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        playAnimation()

    }

    private fun checkLoginUser(message: String, isError: Boolean, token: String?, preference: AppPreferences) {
        if (!isError) {
            Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
            ).show()
            preference.isLoggedIn = true
            if (token != null) preference.authToken = token
            val intent = Intent(this, StoryActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            when (message) {
                "Unauthorized" -> {
                    Toast.makeText(this, getString(R.string.unauthorized), Toast.LENGTH_SHORT)
                        .show()
                    binding.edLoginEmail.apply {
                        setText("")
                        requestFocus()
                    }
                    binding.edLoginPassword.setText("")
                }
                "timeout" -> {
                    Toast.makeText(this, getString(R.string.timeout), Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    Toast.makeText(
                        this,
                        "${getString(R.string.error_message)} $message",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
    ObjectAnimator.ofFloat(binding.imgLogo, View.TRANSLATION_X, -30f, 30f).apply {
        duration = 6000
        repeatCount = ObjectAnimator.INFINITE
        repeatMode = ObjectAnimator.REVERSE
     }.start()
    }
}