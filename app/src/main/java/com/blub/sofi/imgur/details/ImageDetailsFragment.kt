package com.blub.sofi.imgur.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.blub.sofi.R

class ImageDetailsFragment : Fragment(R.layout.image_details_frag) {
    companion object {
        private const val EXTRA_IMAGE_ID = "IMAGE_ID"

        operator fun invoke(imageId: String): ImageDetailsFragment {
            return ImageDetailsFragment().apply {
                arguments!!.putString(EXTRA_IMAGE_ID, imageId)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageResourceId = arguments!!.getString(EXTRA_IMAGE_ID)

    }
}