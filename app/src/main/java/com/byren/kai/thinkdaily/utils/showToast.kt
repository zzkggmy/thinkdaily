package com.byren.kai.thinkdaily.utils

import android.widget.Toast

object showToast {

    fun shortToast(message: String) {
        Toast.makeText(Common.mContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun longToast(message: String) {
        Toast.makeText(Common.mContext(), message, Toast.LENGTH_LONG).show()
    }
}