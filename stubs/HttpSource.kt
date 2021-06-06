package com.anime.dl.sources

import com.anime.dl.sources.models.AnimeInfo
import com.anime.dl.sources.models.AnimePage
import com.anime.dl.sources.models.EpisodeInfo
import okhttp3.CacheControl
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.jsoup.nodes.Element
import java.util.concurrent.TimeUnit.MINUTES

abstract class HttpSource : Source {

    private val DEFAULT_CACHE_CONTROL = CacheControl.Builder().maxAge(10, MINUTES).build()
    private val DEFAULT_BODY: RequestBody = FormBody.Builder().build()

    fun GET(
        url: String,
        headers: Headers = headersBuilder().build(),
        cache: CacheControl = DEFAULT_CACHE_CONTROL
    ): Request {
        throw Exception("Illusion!")
    }

    fun POST(
        url: String,
        headers: Headers = headersBuilder().build(),
        body: RequestBody = DEFAULT_BODY,
        cache: CacheControl = DEFAULT_CACHE_CONTROL
    ): Request {
        throw Exception("Illusion!")
    }

    open val versionId: Int = throw Exception("Illusion!")

    val headers: Headers = throw Exception("Illusion!")

    open val client: OkHttpClient = throw Exception("Illusion!")

    protected open fun headersBuilder(): Headers.Builder {
        throw Exception("Illusion!")
    }

    abstract fun browseAnimeRequest(page: Int): Request

    open fun browseAnimeSelector(): String? = throw Exception("Illusion!")

    open fun browseAnimeFromElement(element: Element): AnimeInfo? = throw Exception("Illusion!")

    open fun browseAnimeNextPageSelector(): String? = throw Exception("Illusion!")

    open fun browseAnimeFromJson(json: String): List<AnimeInfo>? = throw Exception("Illusion!")

    open fun browseAnimeNextPageFromJson(json: String): Boolean = throw Exception("Illusion!")

    abstract fun searchAnimeRequest(query: String, page: Int): Request

    open fun searchAnimeSelector(): String? = throw Exception("Illusion!")

    open fun searchAnimeFromElement(query: String, element: Element): AnimeInfo? = throw Exception("Illusion!")

    open fun searchAnimeNextPageSelector(): String? = throw Exception("Illusion!")

    open fun searchAnimeFromJson(query: String, json: String): List<AnimeInfo>? = null

    open fun searchAnimeNextPageFromJson(json: String): Boolean = throw Exception("Illusion!")

    open fun episodeListRequest(link: String, page: Int): Request = throw Exception("Illusion!")

    open fun episodeListSelector(): String? = throw Exception("Illusion!")

    open fun episodeListNextPageSelector(): String? = throw Exception("Illusion!")

    open fun episodeFromElement(element: Element): EpisodeInfo? = throw Exception("Illusion!")

    open fun episodeListFromJson(link: String, json: String): List<EpisodeInfo> = throw Exception("Illusion!")

    open fun episodeListNextPageFromJson(json: String): Boolean = throw Exception("Illusion!")

    override fun getAnimeList(page: Int): AnimePage {
        throw Exception("Illusion!")
    }

    override fun getSearchList(query: String, page: Int): AnimePage {
        throw Exception("Illusion!")
    }

    override fun getEpisodeList(anime: AnimeInfo, page: Int): List<EpisodeInfo> {
        throw Exception("Illusion!")
    }
}
