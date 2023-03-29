package com.learndm.nnsman.novelreader.ui.read

import androidx.lifecycle.*
import com.learndm.nnsman.R
import com.learndm.nnsman.novelreader.data.BookRepository
import com.learndm.nnsman.novelreader.data.model.Book
import com.learndm.nnsman.novelreader.data.model.Chapter
import com.learndm.nnsman.novelreader.ui.flyer.ReadTheme
import kotlinx.coroutines.launch

class ReadViewModel(private val bookRepository: BookRepository, handle: SavedStateHandle) :
    ViewModel() {
    var isFullScreen = true
    private val _book: MutableLiveData<Book> = MutableLiveData()
    val book: LiveData<Book> = _book
    private val _chapters: MutableLiveData<List<Chapter>> = MutableLiveData()
    val chapters: LiveData<List<Chapter>> = _chapters
    val readThemes = arrayListOf<ReadTheme>()
    fun getDetailBookInfo(bookUrl: String) {
        viewModelScope.launch {
            bookRepository.getDetailBookInfo(bookUrl, intArrayOf(1, 2, 3, 4)).collect {
                it?.let {
                    _book.value = it
                }
            }
        }
    }

    fun getChapters(chapterNumbers: IntArray) {
        _book.value?.let { book ->
            viewModelScope.launch {
                bookRepository.getChapters(book.url, chapterNumbers).collect {
                    _chapters.value = it
                }
            }
        }
    }

    fun initReadTheme() {
        val theme1 = ReadTheme(R.color.t1, R.color.tc_1, R.color.tb_1)
        val theme2 = ReadTheme(R.color.t2, R.color.tc_2, R.color.tb_2, R.color.st_2, R.color.ct_2)
        val theme3 = ReadTheme(R.color.t3, R.color.tc_3, R.color.tb_3)
        val theme4 = ReadTheme(R.color.t4, R.color.tc_4, R.color.tb_4)
        val theme5 = ReadTheme(R.color.t5, R.color.tc_5, R.color.tb_5)
        val theme6 = ReadTheme(R.color.t6, R.color.tc_6, R.color.tb_6)
        val theme7 = ReadTheme(R.color.t7, R.color.tc_7, R.color.tb_7)
        readThemes.add(theme1)
        readThemes.add(theme2)
        readThemes.add(theme3)
        readThemes.add(theme4)
        readThemes.add(theme5)
        readThemes.add(theme6)
        readThemes.add(theme7)
    }
}