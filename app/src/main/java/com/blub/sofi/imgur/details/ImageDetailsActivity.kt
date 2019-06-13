package com.blub.sofi.imgur.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.blub.sofi.R

class ImageDetailsActivity : FragmentActivity() {
    companion object {
        const val EXTRA_IMAGE_ID = "IMAGE_ID"

        fun startInstance(context: Context, imageResourceId: String) {
            val intent = Intent(context, this::class.java).apply {
                putExtra(EXTRA_IMAGE_ID, imageResourceId)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageResourceId = intent.getStringExtra(EXTRA_IMAGE_ID)

        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, ImageDetailsFragment(imageResourceId))
        }
    }
}
