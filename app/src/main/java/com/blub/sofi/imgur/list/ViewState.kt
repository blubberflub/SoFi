package com.blub.sofi.imgur.list

import com.blub.sofi.base.BaseViewState
import com.blub.sofi.data.imgur.model.ImageResource

data class ViewState(
    val imageList: List<ImageResource>,
    val progressShowing: Boolean,
    val errorShown: Boolean
) : BaseViewState() {
    companion object {
        fun idle(): ViewState = ViewState(
            imageList = emptyList(),
            progressShowing = false,
            errorShown = false
        )
    }
}