package com.blub.sofi.imgur.list.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.blub.sofi.R

class ImageListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tag = ImageListFragment::class.java.name

        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            supportFragmentManager.commit {
                add(R.id.fragment_container, ImageListFragment(), tag)
            }
        }
    }
}
