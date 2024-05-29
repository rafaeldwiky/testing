package com.dicoding.picodiploma.loginwithanimation.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.custom.Button
import com.dicoding.picodiploma.loginwithanimation.view.custom.ButtonSignUp
import com.dicoding.picodiploma.loginwithanimation.view.custom.Password
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.dicoding.picodiploma.loginwithanimation.data.response.Result

class LoginActivity : AppCompatActivity() {

    private lateinit var customButton: Button
    private lateinit var customPassword: Password

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        customButton = binding.loginButton
        customPassword = binding.passwordEditText

        val helperError = binding.passwordEditTextLayout

        customPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length > 7) {
                    enableButton()
                    helperError.isHelperTextEnabled = false
                    helperError.isErrorEnabled = false
                    helperError.isPasswordVisibilityToggleEnabled  = true
                } else {
                    customButton.isEnabled = false
                    helperError.helperText = "Password at least 8 characters"
                    helperError.isErrorEnabled = true
                    helperError.isPasswordVisibilityToggleEnabled  = false
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        setupView()
        playAnimation()
        login()
    }

    private fun login() {
        with(binding) {
            loginButton.setOnClickListener {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                viewModel.login(email, password).observe(this@LoginActivity) {
                    when (it) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Error -> {
                            showLoading(false)
                            setupFail(it.error)
                            Log.e("loginError", it.error)
                        }

                        is Result.Success -> {
                            showLoading(false)
                            it.data.loginResult.token.let { it1 ->
                                setupAction(
                                    it.data.message,
                                    it1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction(message: String, token: String) {

        val email = binding.emailEditText.text.toString()
        viewModel.saveSession(UserModel(email, token))

        AlertDialog.Builder(this).apply {
            setTitle("Login Berhasil")
            setMessage(message)
            setPositiveButton("Lanjut") { _, _ ->
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun setupFail(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Login Gagal")
            setMessage(message)
            setPositiveButton("Lanjut") { _, _ ->
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            create()
            show()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }
    private fun enableButton() {
        val result = customPassword.text
        customButton.isEnabled = result != null && result.toString().isNotEmpty()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}