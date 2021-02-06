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

@file:Suppress("UNUSED_VARIABLE")

package xyz.quaver.hitomi

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import xyz.quaver.availableInHiyobi
import java.io.File

class UnitTest {
    @Test
    fun test_nozomi() {
        val nozomi = getGalleryIDsFromNozomi(null, "popular", "all")

        print(nozomi.size)
    }

    @Test
    fun test_search() {
        val ids = getGalleryIDsForQuery("language:korean").reversed()

        print(ids.size)
    }

    @Test
    fun test_suggestions() {
        val suggestions = getSuggestionsForQuery("language:g")

        print(suggestions)
    }

    @Test
    fun test_doSearch() {
        val r = doSearch("female:loli female:bondage language:korean -male:yaoi -male:guro -female:guro", true)

        print(r.size)
    }

    @Test
    fun test_getBlock() {
        val galleryBlock = getGalleryBlock(1262768)

        print(galleryBlock)
    }

    @Test
    fun test_getGallery() {
        val gallery = getGallery(1262768)

        print(gallery)
    }

    @Test
    fun test_getReader() {
        val reader = getReader(1722144)

        print(reader)
    }

    @Test
    fun test_getImages() {
        val reader = getReader(9).galleryInfo.files.forEach {
            println(imageUrlFromImage(9, it, false))
        }
    }

    @Test
    fun test_hiyobi() {
        val reader = xyz.quaver.hiyobi.getReader(1531795)

        print(reader)
    }

    @Test
    fun test_urlFromUrlFromHash() {
        val url = urlFromUrlFromHash(1531795, GalleryFiles(
            212, "719d46a7556be0d0021c5105878507129b5b3308b02cf67f18901b69dbb3b5ef", 1, "00.jpg", 300
        ), "webp")

        print(url)
    }

    @Test
    fun test_availableInHiyobi() {
        val result = availableInHiyobi(1272781)

        print(result)
    }

    @Test
    fun test_hiyobi_galleryBlock() {
        val galleryBlock = xyz.quaver.hiyobi.getGalleryBlock(10000027)

        print(galleryBlock)
    }

    @Test
    fun test_doSearch_extreme() {
        val query = "language:korean -tag:sample -female:humiliation -female:diaper -female:strap-on -female:squirting -female:lizard_girl -female:voyeurism -type:artistcg -female:blood -female:ryona -male:blood -male:ryona -female:crotch_tattoo -male:urethra_insertion -female:living_clothes -male:tentacles -female:slave -female:gag -male:gag -female:wooden_horse -male:exhibitionism -male:miniguy -female:mind_break -male:mind_break -male:unbirth -tag:scanmark -tag:no_penetration -tag:nudity_only -female:enema -female:brain_fuck -female:navel_fuck -tag:novel -tag:mosaic_censorship -tag:webtoon -male:rape -female:rape -female:yuri -male:anal -female:anal -female:futanari -female:huge_breasts -female:big_areolae -male:torture -male:stuck_in_wall -female:stuck_in_wall -female:torture -female:birth -female:pregnant -female:drugs -female:bdsm -female:body_writing -female:cbt -male:dark_skin -male:insect -female:insect -male:vore -female:vore -female:vomit -female:urination -female:urethra_insertion -tag:mmf_threesome -female:sex_toys -female:double_penetration -female:eggs -female:prolapse -male:smell -male:bestiality -female:bestiality -female:big_ass -female:milf -female:mother -male:dilf -male:netorare -female:netorare -female:cosplaying -female:filming -female:armpit_sex -female:armpit_licking -female:tickling -female:lactation -male:skinsuit -female:skinsuit -male:bbm -female:prostitution -female:double_penetration -female:females_only -male:males_only -female:tentacles -female:tentacles -female:stomach_deformation -female:hairy_armpits -female:large_insertions -female:mind_control -male:orc -female:dark_skin -male:yandere -female:yandere -female:scat -female:toddlercon -female:bbw -female:hairy -male:cuntboy -male:lactation -male:drugs -female:body_modification -female:monoeye -female:chikan -female:long_tongue -female:harness -female:fisting -female:glory_hole -female:latex -male:latex -female:unbirth -female:giantess -female:sole_dickgirl -female:robot -female:doll_joints -female:machine -tag:artbook -male:cbt -female:farting -male:farting -male:midget -female:midget -female:exhibitionism  -male:monster -female:big_nipples -female:big_clit -female:gyaru -female:piercing -female:necrophilia -female:snuff -female:smell -male:cheating -female:cheating -male:snuff -female:harem -male:harem"
        print(doSearch(query).size)
    }

    @Test
    fun test_parse() {
        print(doSearch("-female:sole_dickgirl ‐female:scat -female:futanari -female:furry -female:male_on_dickgirl -female:shemale -male:males_only").size)
    }
}