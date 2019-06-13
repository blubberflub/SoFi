package com.blub.sofi.imgur.list

import com.blub.sofi.base.BaseResult
import com.blub.sofi.data.imgur.model.ImageResource

sealed class Result : BaseResult()

object Error : Result()

data class GetListSuccess(val imageList: List<ImageResource>) : Result()

object InFlight : Result()

object Idle : Result()

