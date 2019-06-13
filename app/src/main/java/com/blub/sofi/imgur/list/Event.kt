package com.blub.sofi.imgur.list

import com.blub.sofi.base.BaseEvent

sealed class Event : BaseEvent()

data class SearchQueryChangedEvent(val query: String) : Event()