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

import kotlinx.coroutines.*

private val operators = listOf(
    "and",
    "or"
)

fun parseQuery(query: String): List<String> {
    val stack = mutableListOf<String>()
    val operatorStack = mutableListOf<String>()

    query
        .toLowerCase()
        .replace(Regex("""([\(\)])""")) {
            ' ' + it.groupValues[0] + ' '
        }
        .split(' ')
        .filter { it.isNotBlank() }
        .let { queries ->
            mutableListOf<String>().apply {
                queries.forEachIndexed { index, s ->
                    this.add(s)
                    if (
                        s != "(" &&
                        (s == ")" && queries.getOrNull(index+1).let { it != null && it !in operators } ||
                        s !in operators && queries.getOrNull(index+1).let { it != null && it !in operators +")" })
                    )
                        this.add("and")
                }
            }
        }.forEach {
            when (it) {
                in operators -> {
                    while (operatorStack.isNotEmpty() && operatorStack.last() != "(" && operators.indexOf(operatorStack.last()) >= operators.indexOf(it))
                        stack.add(operatorStack.removeLast())
                    operatorStack.add(it)
                }
                "(" -> {
                    operatorStack.add(it)
                }
                ")" -> {
                    while (operatorStack.last() != "(") stack.add(operatorStack.removeLast())
                    operatorStack.removeLast()
                }
                else -> {
                    stack.add(it)
                }
            }
        }

    while (operatorStack.isNotEmpty())
        stack.add(operatorStack.removeLast())

    return stack
}

fun doSearch(query: String, sortByPopularity: Boolean = false) : Set<Int> {
    val terms = parseQuery(query)

    val all =
        CoroutineScope(Dispatchers.IO).async {
            kotlin.runCatching {
                getGalleryIDsFromNozomi(null, if (sortByPopularity) "popular" else "index", "all")
            }.getOrElse { emptySet() }
        }

    val results = terms.filter { it !in operators }.map {
        val term = if (it.startsWith('-')) it.drop(1) else it

        Pair(term, CoroutineScope(Dispatchers.IO).async {
            runCatching {
                getGalleryIDsForQuery(term)
            }.getOrElse { emptySet() }
        })
    }.toMap().toMutableMap()

    return runBlocking {
        val result = mutableListOf<Set<Int>>()
        val isNegative = mutableListOf<Boolean>()

        terms.forEach {
            when (it) {
                in operators -> {
                    val a = result.removeLast()
                    val an = isNegative.removeLast()
                    val b = result.removeLast()
                    val bn = isNegative.removeLast()

                    when (it) {
                        "and" ->
                            result.add(
                                when {
                                    an and bn ->
                                        all.await() subtract (if (a.size > b.size) a union b else b union a)
                                    an xor bn ->
                                        if (bn) a subtract b else b subtract a
                                    else ->
                                        if (a.size < b.size) a intersect b else b intersect a
                                }
                            )
                        "or" ->
                            result.add(if (a.size > b.size) a union b else b union a)
                    }
                    isNegative.add(false)
                }
                else -> {
                    result.add(results.remove(it.let { if (it.startsWith('-')) it.drop(1) else it })?.await() ?: emptySet())
                    isNegative.add(it.startsWith('-'))
                }
            }
        }

        all.await() intersect result.last()
    }
}