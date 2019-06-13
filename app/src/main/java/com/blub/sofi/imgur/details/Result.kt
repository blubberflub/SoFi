package com.blub.sofi.imgur.details

import com.blub.sofi.base.BaseResult

sealed class Result : BaseResult()

object Error : Result()

data class GetImageSuccess(val link: String) : Result()

object InFlight : Result()

object Idle : Result()