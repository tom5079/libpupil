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

package xyz.quaver

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URL

private var mutex = Mutex()
private var clientInstance: OkHttpClient? = null
val client
    get() = clientInstance
        ?: OkHttpClient().also { runBlocking { mutex.withLock {  
            clientInstance = it    
        } } }

fun setClient(client: OkHttpClient) = runBlocking {
    mutex.withLock {
        clientInstance = client
    }
}

/**
 * kotlinx.serialization.json.Json object for global use  
 * properties should not be changed
 *
 * @see [https://kotlin.github.io/kotlinx.serialization/kotlinx-serialization-core/kotlinx-serialization-core/kotlinx.serialization.json/-json/index.html]
 */
val json = Json {
    isLenient = true
    ignoreUnknownKeys = true
    allowSpecialFloatingPointValues = true
    useArrayPolymorphism = true
}

/**
 * Check if gallery is available in [hiyobi](https://hiyobi.me)
 *
 * @param galleryID Gallery ID to be checked
 * @return true if the gallery exists, false otherwise
 */
fun availableInHiyobi(galleryID: Int) : Boolean {
    return try {
        xyz.quaver.hiyobi.getReader(galleryID)
        true
    } catch (e: Exception) {
        false
    }
}

typealias HeaderSetter = (Request.Builder) -> Request.Builder
fun URL.readText(settings: HeaderSetter? = null): String {
    val request = Request.Builder()
        .url(this).let { 
            settings?.invoke(it) ?: it
        }.build()
    
    return client.newCall(request).execute().also{ if (it.code() != 200) throw IOException() }.body()?.use { it.string() } ?: throw IOException()
}

fun URL.readBytes(settings: HeaderSetter? = null): ByteArray {
    val request = Request.Builder()
        .url(this).let {
            settings?.invoke(it) ?: it
        }.build()

    return client.newCall(request).execute().also { if (it.code() != 200) throw IOException() }.body()?.use { it.bytes() } ?: throw IOException()
}