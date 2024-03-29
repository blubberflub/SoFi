package com.blub.sofi.imgur.list.view

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.blub.sofi.R
import com.blub.sofi.ViewModelFactory
import com.blub.sofi.base.BaseFragment
import com.blub.sofi.base.BaseViewModel
import com.blub.sofi.dagger.AppComponent
import com.blub.sofi.data.imgur.model.ImageResource
import com.blub.sofi.extensions.showToast
import com.blub.sofi.imgur.details.ImageDetailsActivity
import com.blub.sofi.imgur.list.*
import com.blub.sofi.utils.GlideApp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import kotlinx.android.synthetic.main.imgur_list_fragment.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ImageListFragment : BaseFragment<Event, Result, ViewState>(R.layout.imgur_list_fragment) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<ImageListViewModel> { viewModelFactory }
    private val events get() = viewModel.subject
    private lateinit var imageListAdapter: ImgurAdapter

    override fun getViewModel(): BaseViewModel<Event, Result, ViewState> = viewModel

    override fun inject(component: AppComponent) {
        component.inject(this)
    }

    override fun render(viewState: ViewState) {
        imageListAdapter.submitList(viewState.imageList)

        if (viewState.errorShown) {
            showToast(R.string.error)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.imgur_list_menu, menu)
        val search = menu.findItem(R.id.search)

        val searchView = search.actionView as SearchView
        searchView.isFocusable = false
        searchView.queryHint = "Search"

        disposables.add(RxSearchView
            .queryTextChanges(searchView)
            .skipInitialValue()
            .debounce(250, TimeUnit.MILLISECONDS)
            .subscribe {
                sendEvent(SearchQueryChangedEvent(it.toString()))
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()
        imageListAdapter = ImgurAdapter(context) { imageResourceId, isAlbum ->
            ImageDetailsActivity.startInstance(context, imageResourceId, isAlbum)
        }

        with(imgur_list) {
            adapter = imageListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    class ImgurAdapter(
        val context: Context,
        val itemClickListener: (String, Boolean) -> Unit
    ) : ListAdapter<ImageResource, ImgurAdapter.ImageViewHolder>(DiffCallback()) {
        companion object {
            private const val IMAGE_VIEW_TYPE = 1
            private const val ALBUM_VIEW_TYPE = 2
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.imgur_list_item, parent, false)

            return ImageViewHolder(view)
        }

        override fun getItemViewType(position: Int): Int {
            val item = getItem(position)

            val type = if (item.isAlbum) {
                ALBUM_VIEW_TYPE
            } else {
                IMAGE_VIEW_TYPE
            }

            return type
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val imageResource = getItem(position)
            val type = getItemViewType(position)

            when (type) {
                ALBUM_VIEW_TYPE -> onBindAlbum(holder, imageResource)
                IMAGE_VIEW_TYPE -> onBindImage(holder, imageResource)
            }
        }

        private fun onBindImage(
            holder: ImageViewHolder,
            imageResource: ImageResource
        ) {
            with(holder) {
                isAlbum.visibility = View.INVISIBLE

                GlideApp
                    .with(context)
                    .load(imageResource.link)
                    .override(albumCover.width, albumCover.height)
                    .placeholder(progressDrawable())
                    .centerCrop()
                    .error(R.color.colorPrimary)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(albumCover)
                title.text = imageResource.title

                parent.setOnClickListener {
                    itemClickListener(imageResource.id, imageResource.isAlbum)
                }
            }
        }

        private fun onBindAlbum(
            holder: ImageViewHolder,
            imageResource: ImageResource
        ) {
            val coverId = imageResource.cover
            val coverImage = imageResource.images.find { it.id == coverId }

            with(holder) {
                isAlbum.visibility = View.VISIBLE

                GlideApp
                    .with(context)
                    .load(coverImage!!.link)
                    .override(albumCover.width, albumCover.height)
                    .placeholder(progressDrawable())
                    .centerCrop()
                    .error(R.color.colorPrimary)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(albumCover)
                title.text = imageResource.title

                parent.setOnClickListener {
                    itemClickListener(imageResource.id, imageResource.isAlbum)
                }
            }
        }

        class ImageViewHolder(val parent: View) : RecyclerView.ViewHolder(parent) {
            val albumCover: ImageView = parent.findViewById(R.id.image)
            val title: TextView = parent.findViewById(R.id.title)
            val isAlbum: ImageView = parent.findViewById(R.id.is_album)
        }

        fun progressDrawable(): CircularProgressDrawable {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            return circularProgressDrawable
        }

        class DiffCallback : DiffUtil.ItemCallback<ImageResource>() {
            override fun areItemsTheSame(oldItem: ImageResource, newItem: ImageResource): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ImageResource, newItem: ImageResource): Boolean =
                oldItem == newItem
        }
    }
}