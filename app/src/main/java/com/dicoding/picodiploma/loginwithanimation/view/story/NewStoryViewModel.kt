package com.dicoding.picodiploma.loginwithanimation.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class NewStoryViewModel (private val repository: UserRepository): ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun postStory(multipart: MultipartBody.Part, desc: RequestBody, token: String) =
        repository.addStory(multipart, desc, token)
}