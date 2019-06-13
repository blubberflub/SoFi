package com.blub.sofi.imgur.details

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.blub.sofi.R
import com.blub.sofi.ViewModelFactory
import com.blub.sofi.base.BaseFragment
import com.blub.sofi.base.BaseViewModel
import com.blub.sofi.dagger.AppComponent
import com.blub.sofi.extensions.showToast
import com.blub.sofi.utils.GlideApp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.image_details_frag.*
import javax.inject.Inject

class ImageDetailsFragment : BaseFragment<Event, Result, ViewState>(R.layout.image_details_frag) {
    companion object {
        private const val EXTRA_IMAGE_ID = "IMAGE_ID"
        private const val EXTRA_IS_ALBUM = "IS_ALBUM"

        fun getInstance(imageId: String, isAlbum: Boolean): ImageDetailsFragment {
            return ImageDetailsFragment().apply {
                arguments = bundleOf(EXTRA_IMAGE_ID to imageId, EXTRA_IS_ALBUM to isAlbum)
            }
        }
    }

    override fun inject(component: AppComponent) {
        component.inject(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<ImageDetailsViewModel> { viewModelFactory }
    private val events get() = viewModel.subject

    override fun render(viewState: ViewState) {
        val context = requireContext()

        with(viewState) {
            GlideApp
                .with(context)
                .load(viewState.imageLink)
                .centerCrop()
                .error(R.color.colorPrimary)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image_container)

            if (errorShown) {
                showToast(R.string.error)
            }
        }
    }

    override fun getViewModel(): BaseViewModel<Event, Result, ViewState> = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageResourceId = arguments!!.getString(EXTRA_IMAGE_ID)
        val isAlbum = arguments!!.getBoolean(EXTRA_IS_ALBUM)

        events.onNext(InitEvent(imageResourceId!!))
    }
}