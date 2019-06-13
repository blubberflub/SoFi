package com.blub.sofi.data.imgur

import com.blub.sofi.data.imgur.model.Image
import com.blub.sofi.data.imgur.model.ImageResource
import com.blub.sofi.data.imgur.model.ImgurEnvelope
import io.reactivex.Scheduler
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

interface ImgurApi {
    @GET("/3/gallery/search/time/{page_no}")
    fun searchImages(@Path("page_no") pageNumber: Int,
                     @Query("q") query: String
    ): Single<ImgurEnvelope>
}

@Singleton
class ImgurRepository
@Inject
constructor(val scheduler: Scheduler, retrofit: Retrofit) {
    private val api = retrofit.create(ImgurApi::class.java)
    private val simpleCache = mutableMapOf<String, ImageResource>()

    fun searchImages(pageNumber: Int, query: String): Single<List<ImageResource>> {
        val single = api
            .searchImages(pageNumber, query)
            .map {
                val imageResourceList = it.data

                imageResourceList.forEach { imageResource ->
                    simpleCache[imageResource.id] = imageResource
                }

                imageResourceList
            }
            .subscribeOn(scheduler)

        return single
    }

    fun getImage(imageResourceId: String): Single<String> {
        val link = simpleCache[imageResourceId]!!.link

        return Single.just(link)
    }
}
