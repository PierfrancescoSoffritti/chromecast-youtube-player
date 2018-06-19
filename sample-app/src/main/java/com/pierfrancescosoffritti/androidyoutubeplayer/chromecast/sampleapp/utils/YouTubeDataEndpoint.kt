package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils;

import android.graphics.Bitmap
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.extensions.android.json.AndroidJsonFactory
import com.google.api.services.youtube.YouTube
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import java.io.IOException

object YouTubeDataEndpoint {

    const val APP_NAME = "android-youtube-player-chromecast"
    const val YOUTUBE_DATA_API_KEY = "AIzaSyAVeTsyAjfpfBBbUQq4E7jooWwtV2D_tjE"

    fun getVideoTitleFromYouTubeDataAPIs(videoId: String): Single<Pair<String, Bitmap?>> {
        val onSubscribe = SingleOnSubscribe<Pair<String, Bitmap?>> { emitter ->
            try {

                val youTubeDataAPIEndpoint = buildYouTubeEndpoint()

                val query = buildQuery(youTubeDataAPIEndpoint, videoId)
                val videoListResponse = query.execute()

                if (videoListResponse.items.size != 1)
                    throw RuntimeException("There should be exactly one video with the specified id")

                val video = videoListResponse.items[0]

                val videoTitle = video.snippet.title
                val bitmap = NetworkUtils.getBitmapFromURL(video.snippet.thumbnails.medium.url)

                emitter.onSuccess(Pair<String, Bitmap?>(videoTitle, bitmap))

            } catch (e: IOException) {
                emitter.onError(e)
            }
        }

        return Single.create(onSubscribe)
    }

    private fun buildQuery(youTubeDataAPIEndpoint: YouTube, videoId: String): YouTube.Videos.List {
        return youTubeDataAPIEndpoint
                .videos()
                .list("snippet")
                .setFields("items(snippet(title,thumbnails(medium(url))))")
                .setId(videoId)
                .setKey(YOUTUBE_DATA_API_KEY)
    }

    private fun buildYouTubeEndpoint(): YouTube {
        return YouTube.Builder(AndroidHttp.newCompatibleTransport(), AndroidJsonFactory(), null)
                .setApplicationName(APP_NAME)
                .build()
    }
}
