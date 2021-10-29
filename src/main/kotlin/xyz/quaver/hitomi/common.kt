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

package xyz.quaver.hitomi

import kotlinx.serialization.decodeFromString
import xyz.quaver.json
import xyz.quaver.readText
import java.net.URL

const val protocol = "https:"

@Suppress("EXPERIMENTAL_API_USAGE")
fun getGalleryInfo(galleryID: Int) =
    json.decodeFromString<GalleryInfo>(
        URL("$protocol//$domain/galleries/$galleryID.js").readText()
            .replace("var galleryinfo = ", "")
    )

//common.js
const val domain = "ltn.hitomi.la"
const val galleryblockextension = ".html"
const val galleryblockdir = "galleryblock"
const val nozomiextension = ".nozomi"

fun subdomainFromGalleryID(g: Int, numberOfFrontends: Int) : String {
    val o = g % numberOfFrontends

    return (97+o).toChar().toString()
}

fun subdomainFromURL(url: String, base: String? = null) : String {
    var retval = "b"

    if (!base.isNullOrBlank())
        retval = base

    var numberOfFrontends = 2
    val b = 16

    val r = Regex("""/[0-9a-f]/([0-9a-f]{2})/""")
    val m = r.find(url) ?: return "a"

    val g = m.groupValues[1].toIntOrNull(b)

    if (g != null) {
        val o = when {
            g < 0x7b -> 1
            else -> 0
        }

        // retval = subdomainFromGalleryID(g, numberOfFrontends) + retval
        retval = (97+o).toChar().toString() + retval
    }

    return retval
}

fun urlFromURL(url: String, base: String? = null) : String {
    return url.replace(Regex("""//..?\.hitomi\.la/"""), "//${subdomainFromURL(url, base)}.hitomi.la/")
}


fun fullPathFromHash(hash: String?) : String? {
    return when {
        (hash?.length ?: 0) < 3 -> hash
        else -> hash!!.replace(Regex("^.*(..)(.)$"), "$2/$1/$hash")
    }
}

@Suppress("NAME_SHADOWING", "UNUSED_PARAMETER")
fun urlFromHash(galleryID: Int, image: GalleryFiles, dir: String? = null, ext: String? = null) : String {
    val ext = ext ?: dir ?: image.name.split('.').last()
    val dir = dir ?: "images"
    return "$protocol//a.hitomi.la/$dir/${fullPathFromHash(image.hash)}.$ext"
}

fun urlFromUrlFromHash(galleryID: Int, image: GalleryFiles, dir: String? = null, ext: String? = null, base: String? = null) =
    urlFromURL(urlFromHash(galleryID, image, dir, ext), base)

fun rewriteTnPaths(html: String) =
    html.replace(Regex("""//tn\.hitomi\.la/[^/]+/[0-9a-f]/[0-9a-f]{2}/""")) { url ->
        urlFromURL(url.value, "tn")
    }

fun imageUrlFromImage(galleryID: Int, image: GalleryFiles, noWebp: Boolean) : String {
    return when {
        noWebp ->
            urlFromUrlFromHash(galleryID, image)
      //image.hasavif != 0 ->
      //    urlFromUrlFromHash(galleryID, image, "avif", null, "a")
        image.haswebp != 0 ->
            urlFromUrlFromHash(galleryID, image, "webp", null, "a")
        else ->
            urlFromUrlFromHash(galleryID, image)
    }
}
