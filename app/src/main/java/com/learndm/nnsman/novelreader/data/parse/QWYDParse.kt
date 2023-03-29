package com.learndm.nnsman.novelreader.data.parse

import com.learndm.nnsman.novelreader.data.model.*
import com.learndm.nnsman.novelreader.data.network.NovelReaderNetwork
import org.jsoup.Jsoup
import java.io.IOException

class QWYDParse(network: NovelReaderNetwork) : BookStoreHtmlParse(network) {
    private val BASE_URL = "https://www.quanwenyuedu.io"
    private val SEARCH_URL = "/index.php?c=xs&a=search&keywords=%s&page=%s"

    companion object {
        val typeMap = mapOf(
            1 to "奇幻玄幻",
            2 to "武侠仙侠",
            3 to "都市官场",
            4 to "历史军事",
            5 to "言情穿越",
            6 to "灵异悬疑",
        )
    }

    override suspend fun searchBookByKeyword(
        keyword: String,
        page: Int
    ): ResponseSearchBookByKeyword {
        val url = BASE_URL + String.format(SEARCH_URL, keyword, page)

        var totalPage = 0
        val books: ArrayList<Book> = arrayListOf()

        try {
//            val document = Jsoup.connect(url).get()
            val document = Jsoup.parse(network.searchBookByKeyword(url))
            //总页数
            totalPage = document.select(".list_page span")[1].html().split("/")[1].trim().toInt()
            val elements = document.select(".box .top")
            elements.forEach {
                val coverUrl = it.select("img").attr("src")
                val bookUrl = it.select("h3 a").attr("href")
                val name = it.select("h3 a").html()
                val p = it.select("p")
                val author = p[0].select("span").html()
                val desc = p[1].html()

                val book = Book(name, author, coverUrl, bookUrl, desc)
                books.add(book)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ResponseSearchBookByKeyword(keyword, page, totalPage, books)
    }

    override suspend fun getChapterTitleList(bookUrl: String): ArrayList<ChapterTitle> {
        val url = BASE_URL + bookUrl + "xiaoshuo.html"
        val list = arrayListOf<ChapterTitle>()
        try {
//            val document = Jsoup.connect(url).get()
            val document = Jsoup.parse(network.getChapterTitleList(url))
            val liList = document.select(".list .list li")
            liList.forEach {
                val chapterUrl = it.select("a").attr("href")
                val title = it.select("a").html()
                val chapterTitle = ChapterTitle(title, chapterUrl)
                list.add(chapterTitle)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    override suspend fun getChaptersInfo(
        bookUrl: String,
        chapterNumbers: IntArray
    ): ArrayList<Chapter> {
        val chapters = arrayListOf<Chapter>()
        for (chapterNumber in chapterNumbers) {
            val url = "$BASE_URL$bookUrl$chapterNumber.html"
            try {
//                val document = Jsoup.connect(url).get()
                val document = Jsoup.parse(network.getChaptersInfo(url))
                val plist = document.select(".articlebody p")
                val title = document.select("h1").html()
                //去掉标题
                if (plist[0].html().trim() == title.trim())
                    plist.removeAt(0)
                val sb = StringBuilder()
                plist.forEach {
                    val paragraphContent = it.html()
                    sb.append(paragraphContent + "\n")
                }
                //删掉最后的换行符
                sb.deleteAt(sb.length - 1)
                chapters.add(Chapter(title, sb.toString(), chapterNumber, bookUrl))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return chapters
    }

    override suspend fun getBooksByType(
        type: Int,
        page: Int
    ): ResponseGetBooksByType {
        var totalPage = 0
        val books = arrayListOf<Book>()
        try {
//            val t = typeMap[type] ?: throw IllegalArgumentException("type not found")
            val url = "${BASE_URL}/c/$type-$page.html"
            val document = Jsoup.parse(network.getBooksByType(url))

            //总页数
            totalPage = document.select(".list_page span")[1].html().split("/")[1].trim().toInt()
            val elements = document.select(".box .top")
            elements.forEach {
                val coverUrl = it.select("img").attr("src")
                val bookUrl = it.select("h3 a").attr("href")
                val name = it.select("h3 a").html()
                val p = it.select("p")
                val author = p[0].select("span").html()
                val desc = p[1].html()

                val book = Book(name, author, coverUrl, bookUrl, desc)
                books.add(book)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ResponseGetBooksByType(type, page, totalPage, books)
    }

    override suspend fun getBookInfoByUrl(bookUrl: String): Book? {

        try {
            val url = "$BASE_URL$bookUrl"
            val document = Jsoup.parse(network.getBookInfo(url))
            val element = document.select(".top")
            val coverUrl = element.select("img").attr("src")
            val bookName = element.select("p")[0].select("span").html()
            val author = element.select("p")[1].select("span").html()
            val desc = document.select(".description").select("p").html()
            return Book(bookName, author, coverUrl, bookUrl, desc)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}