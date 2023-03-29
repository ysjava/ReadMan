package com.learndm.nnsman.novelreader.ui.flyer

interface OnReaderViewTouchListener {
    fun onCenterClick(readerView: ReaderView)
    //ReaderView是否可以消费移动(move)事件, 根据ReadActivity的菜单栏显示状态返回,隐藏返回true,显示返回false.
    fun canMove():Boolean
    //恢复可移动的状态, 当canMove为false,事件为up时, 通知ReadActivity进行菜单栏隐藏.
    fun resumeMovement()

//    fun chapterCut()
}