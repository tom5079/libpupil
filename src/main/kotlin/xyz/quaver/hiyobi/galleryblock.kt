/*
 *    Copyright 2020 tom5079
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
import kotlinx.serialization.json.*
import xyz.quaver.json
import xyz.quaver.readText
import java.net.URL

@Serializable
data class GalleryBlock(
    val id: String,
    val galleryUrl: String,
    val thumbnails: List<String>,
    val title: String,
    val artists: List<String>,
    val series: List<String>,
    val type: String,
    val language: String,
    val relatedTags: List<String>
)

fun getGalleryBlock(galleryID: String) : GalleryBlock {
    val url = "https://api.$hiyobi/gallery/$galleryID"
    
    val galleryBlock = json.parseToJsonElement(URL(url).readText()).jsonObject

    val galleryUrl = "reader/$galleryID"

    val thumbnails = listOf("https://cdn.$hiyobi/tn/$galleryID.jpg")

    val title = galleryBlock["title"]!!.jsonPrimitive.content
    val artists = galleryBlock["artists"]?.jsonArray?.mapNotNull {
        it.jsonObject["value"]?.jsonPrimitive?.contentOrNull
    } ?: listOf()
    val series = galleryBlock["parodys"]?.jsonArray?.mapNotNull {
        it.jsonObject["value"]?.jsonPrimitive?.contentOrNull
    } ?: listOf()
    val type = when (galleryBlock["type"]?.jsonPrimitive?.intOrNull) {
        1 -> "doujinshi"
        2 -> "manga"
        3 -> "artistcg"
        4 -> "gamecg"
        else -> ""
    }

    val language = "korean"

    val relatedTags = galleryBlock["tags"]?.jsonArray?.mapNotNull {
        it.jsonObject["value"]?.jsonPrimitive?.contentOrNull
    } ?: listOf()

    return GalleryBlock(galleryID, galleryUrl, thumbnails, title, artists, series, type, language, relatedTags)
}