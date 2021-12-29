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

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Request
import org.junit.Test
import xyz.quaver.client
import java.util.*
import org.junit.Assert.*

class UnitTest {
    @Test
    fun test_empty() {
        print(
            "".trim()
                    .replace(Regex("""^\?"""), "")
                .lowercase(Locale.getDefault())
            .split(Regex("\\s+"))
            .map {
                it.replace('_', ' ')
            })
    }
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
        val r = doSearch("language:korean")

        print(r.take(10))
    }

    @Test
    fun test_getBlock() {
        val galleryBlock = getGalleryBlock(2049821)

        print(Json.encodeToString(galleryBlock))
    }

    @Test
    fun test_getGallery() {
        val gallery = getGallery(1771195)

        print(gallery)
    }

    @Test
    fun test_getReader() {
        val reader = getGalleryInfo(1722144)

        print(reader)
    }

    @Test
    fun test_getImages() {
        val expected = listOf(
            "https://bb.hitomi.la/images/1639745412/3214/861706766f9680dc8d978ba4b4edea9ec18ea6e066f0aedbba09dd94f866d8ec.jpg",
            "https://bb.hitomi.la/images/1639745412/2814/bfb60cc7dda17b04a58a7e3b9d923cb56e536172945e8e08a78d3ffeda0ccfea.jpg",
            "https://ab.hitomi.la/images/1639745412/4006/4ff5a057d2b1120c9c40242db5da20fc9f3cd147a67d08d729f9b4cb6b51aa6f.jpg",
            "https://bb.hitomi.la/images/1639745412/1465/bcddff65a05477dd88b9bd7f378e4622de48db0b7731a7d9c508e96b495aeb95.jpg",
            "https://bb.hitomi.la/images/1639745412/2372/a8193e9c956791cf04b32e0ac7bb1056816c64b15eee3ea079af5f0006030449.jpg",
            "https://ab.hitomi.la/images/1639745412/414/be574a1ddd67c048495c287b28320bd55447024715216200330c2027ffc969e1.jpg",
            "https://bb.hitomi.la/images/1639745412/3807/681e985d9d6be186ea52a8366e5c5acebbbbf8f93683f1a033d87d7e0d3d1dfe.jpg",
            "https://bb.hitomi.la/images/1639745412/3883/a8eb35f3d0494b24c3162245c2cb8cd488cf0fa2926c00f308f45502ede462bf.jpg",
            "https://bb.hitomi.la/images/1639745412/3811/80020c727cbcb4ae718b773ebc58371df0e175d44707a63401c1625b37216e3e.jpg",
            "https://bb.hitomi.la/images/1639745412/194/40cc5e1f53db89d10abad43be64dfa5329f0096e014622a9fa19cde6471a3c20.jpg",
            "https://ab.hitomi.la/images/1639745412/1384/b47696a404c9788139b7a5899c23b4678def9a40a60d77b23efc7316d9b79685.jpg",
            "https://bb.hitomi.la/images/1639745412/98/fffd32ece896a961e3ead019f7f2310339b84f75188c3c9dc3d7c7b178d03620.jpg",
            "https://bb.hitomi.la/images/1639745412/3931/66aedc8af244ddc7d63bf268108cf6a3f11f1e85bca7fa70ef3cc91345c275bf.jpg",
            "https://ab.hitomi.la/images/1639745412/1085/1a2eca4ddee4268518c7b7b25bbcc9facf2daaa4357a25e200adfece281173d4.jpg",
            "https://ab.hitomi.la/images/1639745412/3556/17c7e0841759b640b9fc9ba1f46c5353c48b4e59b1d6efdf4327eceabae64e4d.jpg",
            "https://bb.hitomi.la/images/1639745412/1880/80c0bc979379a5e43d658cdcec637e78c62500d637109d0cb38c902105bff587.jpg",
            "https://ab.hitomi.la/images/1639745412/1882/2a22c484fa72302ce58ac0d3c53cf9e5c025d647698e1ba9308c1a02ac6675a7.jpg",
            "https://bb.hitomi.la/images/1639745412/758/7f657575c75cedf3df82fdc290da64a1ae9107cf1a5e53346a43cb43d6abdf62.jpg",
            "https://bb.hitomi.la/images/1639745412/2710/a2470712c971e64389b73685722f0993b865d6307a9cec42d2c5c118e41e596a.jpg",
            "https://ab.hitomi.la/images/1639745412/3586/304ed08c39f8f4497f51c7899afc5d4dc1dfe232c8003761ba7c4315b1c1802e.jpg",
            "https://bb.hitomi.la/images/1639745412/992/2f185b6ea7e9da7214517c0d6be04a1dba7e35d4c5234375214c4c4893d40e03.jpg",
            "https://ab.hitomi.la/images/1639745412/1614/24762a235bb74bf4914ba111f9fe073a5d65e7e65bbfabca90bbffba1599b4e6.jpg",
            "https://ab.hitomi.la/images/1639745412/2944/3052c3f029022740f73ead392ba8c405e93a43ad3fc5998d1852eebc35cee80b.jpg",
            "https://bb.hitomi.la/images/1639745412/2735/48d78be359b4a7ad7281b687b711b638f725fefbbda68a4a54366b3d85e24afa.jpg",
            "https://bb.hitomi.la/images/1639745412/2162/5a909ba4b25f48ab6d8edc1a89d49e4528970ba59e3b787871e2cad50a086728.jpg",
            "https://ab.hitomi.la/images/1639745412/3757/31bd826c4ac7873e6ec176d5ef6bbb474d91f65c0c691695fc12a88048f27ade.jpg",
            "https://bb.hitomi.la/images/1639745412/2183/24d7a53b645cc86363b3fadb365caafba8891506ae28bb150f2849269a973878.png",
            "https://bb.hitomi.la/images/1639745412/1846/7c3cce3418f6d6bcd5e06d773b9901c6071203cb82122e39157799bda70d2367.png",
            "https://ab.hitomi.la/images/1639745412/1615/0a450ffd44479f76d0d4fb463774db0a0252ce755388c915420fd91e1bf8d4f6.png"
        )

        val actual = getGalleryInfo(1034095).files.map {
            imageUrlFromImage(1034095, it, true)
        }

        assertArrayEquals(expected.toTypedArray(), actual.toTypedArray())
    }

    @Test
    fun test_urlFromUrlFromHash() {
        val url = urlFromUrlFromHash(1531795, GalleryFiles(
            212, "719d46a7556be0d0021c5105878507129b5b3308b02cf67f18901b69dbb3b5ef", 1, "00.jpg", 300
        ), "webp")

        print(url)
    }

    @Test
    fun test_doSearch_extreme() {
        val query = "language:korean -tag:sample -female:humiliation -female:diaper -female:strap-on -female:squirting -female:lizard_girl -female:voyeurism -type:artistcg -female:blood -female:ryona -male:blood -male:ryona -female:crotch_tattoo -male:urethra_insertion -female:living_clothes -male:tentacles -female:slave -female:gag -male:gag -female:wooden_horse -male:exhibitionism -male:miniguy -female:mind_break -male:mind_break -male:unbirth -tag:scanmark -tag:no_penetration -tag:nudity_only -female:enema -female:brain_fuck -female:navel_fuck -tag:novel -tag:mosaic_censorship -tag:webtoon -male:rape -female:rape -female:yuri -male:anal -female:anal -female:futanari -female:huge_breasts -female:big_areolae -male:torture -male:stuck_in_wall -female:stuck_in_wall -female:torture -female:birth -female:pregnant -female:drugs -female:bdsm -female:body_writing -female:cbt -male:dark_skin -male:insect -female:insect -male:vore -female:vore -female:vomit -female:urination -female:urethra_insertion -tag:mmf_threesome -female:sex_toys -female:double_penetration -female:eggs -female:prolapse -male:smell -male:bestiality -female:bestiality -female:big_ass -female:milf -female:mother -male:dilf -male:netorare -female:netorare -female:cosplaying -female:filming -female:armpit_sex -female:armpit_licking -female:tickling -female:lactation -male:skinsuit -female:skinsuit -male:bbm -female:prostitution -female:double_penetration -female:females_only -male:males_only -female:tentacles -female:tentacles -female:stomach_deformation -female:hairy_armpits -female:large_insertions -female:mind_control -male:orc -female:dark_skin -male:yandere -female:yandere -female:scat -female:toddlercon -female:bbw -female:hairy -male:cuntboy -male:lactation -male:drugs -female:body_modification -female:monoeye -female:chikan -female:long_tongue -female:harness -female:fisting -female:glory_hole -female:latex -male:latex -female:unbirth -female:giantess -female:sole_dickgirl -female:robot -female:doll_joints -female:machine -tag:artbook -male:cbt -female:farting -male:farting -male:midget -female:midget -female:exhibitionism  -male:monster -female:big_nipples -female:big_clit -female:gyaru -female:piercing -female:necrophilia -female:snuff -female:smell -male:cheating -female:cheating -male:snuff -female:harem -male:harem"
        print(doSearch(query).size)
    }

    @Test
    fun test_parse() {
        print(doSearch("-male:yaoi -female:yaoi -female:loli").size)
    }

    @Test
    fun test_subdomainFromUrl() {
        val galleryInfo = getGalleryInfo(1929109).files[2]
        print(urlFromUrlFromHash(1929109, galleryInfo, "webp", null, "a"))
    }
}