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

data class Images(
    val path: String,
    val no: Int,
    val name: String
)

fun getReader(galleryID: Int) : Reader {
    val data = "https://cdn.$hiyobi/data/json/$galleryID.json"
    val list = "https://cdn.$hiyobi/data/json/${galleryID}_list.json"
    
    val title = json.parseToJsonElement(URL(data).readText())
        .jsonObject["n"]!!.jsonPrimitive.content

    val galleryFiles = json.decodeFromString<List<GalleryFiles>>(URL(list).readText())

    return Reader(Code.HIYOBI, GalleryInfo(id = galleryID, title = title, files = galleryFiles))
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