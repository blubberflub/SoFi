package com.blub.sofi.imgur.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.blub.sofi.R

class ImageDetailsActivity : FragmentActivity() {
    companion object {
        private const val EXTRA_IMAGE_ID = "IMAGE_ID"
        private const val EXTRA_IS_ALBUM = "IS_ALBUM"

        fun startInstance(context: Context, imageResourceId: String, album: Boolean) {
            val intent = Intent(context, ImageDetailsActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_ID, imageResourceId)
                putExtra(EXTRA_IS_ALBUM, album)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageResourceId = intent.getStringExtra(EXTRA_IMAGE_ID)
        val isAlbum = intent.getBooleanExtra(EXTRA_IS_ALBUM, false)

        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            supportFragmentManager.commit {
                add(R.id.fragment_container, ImageDetailsFragment.getInstance(imageResourceId, isAlbum))
            }
        }
    }
}
