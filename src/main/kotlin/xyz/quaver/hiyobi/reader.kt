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

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import xyz.quaver.hitomi.GalleryFiles
import xyz.quaver.json
import xyz.quaver.readText
import java.net.URL

const val hiyobi = "hiyobi.me"
const val primary_img_domain = "cdn.hiyobi.me"

@Serializable
data class Images(
    val path: String,
    val no: String,
    val name: String
)

@Serializable
data class GalleryInfo(
    val language_localname: String? = null,
    val language: String? = null,
    val date: String? = null,
    val files: List<GalleryFiles>,
    val id: String? = null,
    val type: String? = null,
    val title: String? = null
)
fun getGalleryInfo(galleryID: String) : GalleryInfo {
    val list = "https://cdn.$hiyobi/json/${galleryID}_list.json"

    val galleryFiles = json.decodeFromString<List<GalleryFiles>>(URL(list).readText())

    val title = getGalleryBlock(galleryID).title

    return GalleryInfo(id = galleryID, title = title, files = galleryFiles)
}

@Deprecated("", replaceWith = ReplaceWith("getGalleryInfo"))
fun getReader(galleryID: String) : GalleryInfo {
    val list = "https://cdn.$hiyobi/json/${galleryID}_list.json"

    val galleryFiles = json.decodeFromString<List<GalleryFiles>>(URL(list).readText())

    val title = getGalleryBlock(galleryID).title

    return GalleryInfo(id = galleryID, title = title, files = galleryFiles)
}

fun createImgList(galleryID: String, galleryInfo: GalleryInfo, lowQuality: Boolean = false) =
    if (lowQuality)
        galleryInfo.files.map {
            val name = it.name.replace(Regex("""\.[^/.]+$"""), "")
            Images("https://$primary_img_domain/data_r/$galleryID/$name.jpg", galleryID, it.name)
        }
    else
        galleryInfo.files.map { Images("https://$primary_img_domain/data/$galleryID/${it.name}", galleryID, it.name) }