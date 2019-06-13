package com.blub.sofi.imgur.details

import com.blub.sofi.base.BaseViewState
import com.blub.sofi.data.imgur.model.Image

data class ViewState(val image: Image?) : BaseViewState() {
    companion object {
        fun idle(): ViewState {
            return ViewState(image = null)
        }
    }
}