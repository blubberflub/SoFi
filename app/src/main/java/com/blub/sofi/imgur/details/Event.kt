package com.blub.sofi.imgur.details

import com.blub.sofi.base.BaseEvent

sealed class Event : BaseEvent()

data class InitEvent(val imageResourceId: String) : Event()