package xyz.quaver.hiyobi

import kotlinx.serialization.json.*
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import xyz.quaver.client
import java.io.IOException

private const val PER_PAGE = 15
fun search(query: String, range: IntRange): Pair<List<String>, Int> {
    val url = "https://api.hiyobi.me/search"

    val result = mutableListOf<String>()

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

        val respose = Json.parseToJsonElement(text)

        if (count < 0)
            count = respose.jsonObject["count"]?.jsonPrimitive?.int ?: -1

        val slice = when {
            index != 0 && index != range.last / PER_PAGE -> 0 until PER_PAGE
            index == 0 && index == range.last / PER_PAGE -> range.first .. range.last
            index == 0 -> range.first until PER_PAGE
            index == range.last / PER_PAGE -> 0 .. range.last % PER_PAGE
            else -> error("")
        }

        result.addAll(
            (respose.jsonObject["list"]?.jsonArray?.mapNotNull { it.jsonObject["id"]?.jsonPrimitive?.content } ?: emptyList())
                .slice(slice)
        )
    }

    return Pair(result, count)
}