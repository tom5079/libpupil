package xyz.quaver.hiyobi

import kotlinx.serialization.json.*
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import xyz.quaver.client
import xyz.quaver.json
import java.io.IOException
import kotlin.math.*

private const val PER_PAGE = 15
fun search(query: String, range: IntRange): Pair<List<GalleryBlock>, Int> {
    val url = "https://api.hiyobi.me/search"

    val result = mutableListOf<GalleryBlock>()

    val requests = (range.first .. range.last step PER_PAGE).map { JsonObject(mapOf(
        "search" to JsonArray(query.split(" ").map { JsonPrimitive(it) }),
        "paging" to JsonPrimitive((it / PER_PAGE) +1)
    )) }

    var count = -1

    requests.forEachIndexed { index, request ->
        val requestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            request.toString()
        )

        val text = client.newCall(
            Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
        ).execute().also { if (it.code() != 200) throw IOException() }.body()?.use { it.string() } ?: throw IOException()

        val response = Json.parseToJsonElement(text)

        if (count < 0)
            count = response.jsonObject["count"]?.jsonPrimitive?.int ?: return Pair(emptyList(), 0)

        val slice = when {
            index != 0 && index != range.last / PER_PAGE -> 0 until PER_PAGE
            index == 0 && index == range.last / PER_PAGE -> range.first .. range.last
            index == 0 -> range.first until PER_PAGE
            index == range.last / PER_PAGE -> 0 .. range.last % PER_PAGE
            else -> error("")
        }

        val list = response.jsonObject["list"]?.jsonArray ?: emptyList()

        val sanitizedSlice = max(0, slice.first) .. min(slice.last, list.size-1)

        result.addAll(list.map { json.decodeFromJsonElement<GalleryBlock>(it) }.slice(sanitizedSlice))
    }

    return Pair(result, count)
}

fun list(range: IntRange): Pair<List<GalleryBlock>, Int> {
    val url = "https://api.hiyobi.me/list/"

    val result = mutableListOf<GalleryBlock>()

    val pages = (range.first / PER_PAGE)+1 .. (range.last / PER_PAGE)+1

    var count = -1

    pages.forEachIndexed { index, page ->
        val text = client.newCall(
            Request.Builder()
                .url(url + page.toString())
                .build()
        ).execute().also { if (it.code() != 200) throw IOException() }.body()?.use { it.string() } ?: throw IOException()

        val response = Json.parseToJsonElement(text)

        if (count < 0)
            count = response.jsonObject["count"]?.jsonPrimitive?.int ?: return Pair(emptyList(), 0)

        val slice = when {
            index != 0 && index != range.last / PER_PAGE -> 0 until PER_PAGE
            index == 0 && index == range.last / PER_PAGE -> range.first .. range.last
            index == 0 -> range.first until PER_PAGE
            index == range.last / PER_PAGE -> 0 .. range.last % PER_PAGE
            else -> error("")
        }

        val list = response.jsonObject["list"]?.jsonArray ?: emptyList()

        val sanitizedSlice = max(0, slice.first) .. min(slice.last, list.size-1)

        result.addAll(list.map { json.decodeFromJsonElement<GalleryBlock>(it) }.slice(sanitizedSlice))
    }

    return Pair(result, count)
}