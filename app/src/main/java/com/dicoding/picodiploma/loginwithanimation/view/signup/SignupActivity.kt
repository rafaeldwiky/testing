package com.dicoding.picodiploma.loginwithanimation.view.signup

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
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.custom.ButtonSignUp
import com.dicoding.picodiploma.loginwithanimation.view.custom.Password
import com.dicoding.picodiploma.loginwithanimation.data.response.Result
import com.dicoding.picodiploma.loginwithanimation.view.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var customButtonSignUp: ButtonSignUp
    private lateinit var customPassword: Password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customButtonSignUp = binding.signupButton
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
                    customButtonSignUp.isEnabled = false
                    helperError.helperText = "Password tidak boleh kurang dari 8 karakter"
                    helperError.isErrorEnabled = true
                    helperError.isPasswordVisibilityToggleEnabled  = false
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        setupView()
        playAnimation()
        signup()
    }

    private fun signup() {
        with(binding) {
            signupButton.setOnClickListener {
                val name = nameEditText.text.toString()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                viewModel.register(name, email, password).observe(this@SignupActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            setupAction(result.data.message)
                            Log.d("registerBerhasil", result.data.message)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            setupFail(result.error)
                            Log.d("registerGagal", result.error)
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

    private fun setupAction(message: String) {
        binding.signupButton.setOnClickListener {

            AlertDialog.Builder(this).apply {
                setTitle("Yessss!")
                setMessage(message)
                setPositiveButton("Lanjut") { _, _ ->
                    startActivity(Intent(context, LoginActivity::class.java))
                    finish()
                }
                create()
                show()
            }
        }
    }

    private fun setupFail(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Gagal")
            setMessage(message)
            setPositiveButton("Lanjut") { _, _ ->
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
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            start()
        }
    }
    private fun enableButton() {
        val result = customPassword.text
        customButtonSignUp.isEnabled = result != null && result.toString().isNotEmpty()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

