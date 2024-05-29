package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.Result

class SignupViewModel (private val repository: UserRepository): ViewModel() {

    fun register(name: String, email: String, pass: String): LiveData<Result<RegisterResponse>> =
        repository.register(name, email, pass)
}