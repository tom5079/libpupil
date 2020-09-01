/*
 *    Copyright 2019 tom5079
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package xyz.quaver.hiyobi

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.Request
import xyz.quaver.*
import xyz.quaver.hitomi.GalleryFiles
import xyz.quaver.hitomi.GalleryInfo
import xyz.quaver.hitomi.Reader
import xyz.quaver.hitomi.protocol
import java.net.URL

const val hiyobi = "hiyobi.me"
const val primary_img_domain = "cdn.hiyobi.me"
const val user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36"

val cookie: String by lazy { renewCookie() }

data class Images(
    val path: String,
    val no: Int,
    val name: String
)

fun renewCookie() : String {
    val url = "https://$hiyobi/"
    
    val request = Request.Builder()
        .url(url)
        .header("User-Agent", user_agent)
        .build()
    
    return client.newCall(request).execute().use { it.header("Set-Cookie") }!!
}

fun getReader(galleryID: Int) : Reader {
    val data = "https://cdn.$hiyobi/data/json/$galleryID.json"
    val list = "https://cdn.$hiyobi/data/json/${galleryID}_list.json"
    
    val title = json.parseToJsonElement(URL(data).readText(hiyobiHeaderSetter))
        .jsonObject["n"]!!.jsonPrimitive.content

    val galleryFiles = json.decodeFromString<List<GalleryFiles>>(URL(list).readText(hiyobiHeaderSetter))

    return Reader(Code.HIYOBI, GalleryInfo(title = title, files = galleryFiles))
}

fun getReaderOrNull(galleryID: Int) = runCatching { getReader(galleryID) }.getOrNull()

fun createImgList(galleryID: Int, reader: Reader, lowQuality: Boolean = false) =
    if (lowQuality)
        reader.galleryInfo.files.map {
            val name = it.name.replace(Regex("""\.[^/.]+$"""), "")
            Images("$protocol//$primary_img_domain/data_r/$galleryID/$name.jpg", galleryID, it.name)
        }
    else
        reader.galleryInfo.files.map { Images("$protocol//$primary_img_domain/data/$galleryID/${it.name}", galleryID, it.name) }