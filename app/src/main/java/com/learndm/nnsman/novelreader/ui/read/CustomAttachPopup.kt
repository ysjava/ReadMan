package com.learndm.nnsman.novelreader.ui.read

import android.content.Context
import com.learndm.nnsman.R
import com.lxj.xpopup.core.AttachPopupView

class CustomAttachPopup(context: Context): AttachPopupView(context) {
    override fun getImplLayoutId(): Int {
        return R.layout.dialog_test
    }
}