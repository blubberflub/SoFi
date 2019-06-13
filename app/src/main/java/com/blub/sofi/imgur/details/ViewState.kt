package com.blub.sofi.imgur.details

import com.blub.sofi.base.BaseViewState

data class ViewState(
    val imageLink: String,
    val progressShowing: Boolean,
    val errorShown: Boolean
) : BaseViewState() {
    companion object {
        fun idle(): ViewState = ViewState(
            imageLink = "",
            progressShowing = false,
            errorShown = false
        )
    }
}