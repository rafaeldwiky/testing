package com.dicoding.picodiploma.loginwithanimation.view.main

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private lateinit var bind: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val detail = intent.getParcelableExtra<ListStoryItem>(DETAIL_STORY) as ListStoryItem
        setupAction(detail)

        supportActionBar?.show()
        supportActionBar?.title = "Detail Story"
    }

    private fun setupAction(detail: ListStoryItem){
        Glide.with(applicationContext)
            .load(detail.photoUrl)
            .into(bind.ivPicture)
        bind.tvName.text = detail.name
        bind.tvDescription.text = detail.description
        bind.progressBar.visibility = View.GONE
    }

    companion object {
        const val DETAIL_STORY = "detail_story"
    }
}