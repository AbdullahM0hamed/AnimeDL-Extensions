package com.anime.dl.sources

import com.anime.dl.sources.models.AnimeInfo
import com.anime.dl.sources.models.AnimePage
import com.anime.dl.sources.models.EpisodeInfo
import okhttp3.CacheControl
import org.jsoup.nodes.Element
import java.util.concurrent.TimeUnit.MINUTES

class Ryuanime : HttpSource() {

    private val cache = CacheControl.Builder().maxAge(10, MINUTES).build()

    override val id = 0L

    override val name = "Ryuanime"

    override val lang = "en"

    override val baseUrl = "https://ryuanime.com"

    override fun getAnimeDetails(anime: AnimeInfo): AnimeInfo {
        return anime
    }

    override fun browseAnimeRequest(page: Int) = GET("$baseUrl/browse-anime?order=date,desc&lang=subbed", headers, cache)

    override fun browseAnimeSelector() = "div.anime-thumb"

    override fun browseAnimeFromElement(element: Element): AnimeInfo? {
        return AnimeInfo(
            key="0",
            title=element.select("p.anime-name.oswald").text(),
            link=element.select("a.ani-link").attr("abs:href"),
            cover=element.select("img.w-100").attr("abs:src")
        )
    }

    override fun browseAnimeNextPageSelector() = "Illusion"

    override fun searchAnimeRequest(query: String, page: Int) = GET("$baseUrl/browse-anime?search=$query", headers, cache)

    override fun searchAnimeSelector() = browseAnimeSelector()

    override fun searchAnimeFromElement(query: String, element: Element) = browseAnimeFromElement(element)

    override fun searchAnimeNextPageSelector() = browseAnimeNextPageSelector()

    override fun episodeListRequest(link: String, page: Int) = GET(link, headers, cache)

    override fun episodeListSelector() = "div.da-video-tbl"

    override fun episodeFromElement(element: Element): EpisodeInfo? {
        return EpisodeInfo(
            key="0",
            title=element.select("span.ep-num").text().trim(),
            dateUpload=System.currentTimeMillis(),
            ep_number=-1f, //cba atm
            thumbnail=element.select("img").attr("abs:src")
        )
    }

    override fun episodeListNextPageSelector() = "Illusion"
}
