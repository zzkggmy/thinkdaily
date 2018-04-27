package com.byren.kai.thinkdaily.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

object PermissionUtils {
    private val agreeList: ArrayList<String> = ArrayList()
    private val refuseList = arrayListOf<String>()
    private fun checkPermissions(activity: Activity, permissions: Array<String>) {
        for (i in 0 until permissions.size) {
            if (checkAuthorized(activity, permissions[i])) {
                agreeList.add(permissions[i])
            } else {
                refuseList.add(permissions[i])
                explainPerimission(activity, permissions[i])

            }
        }
    }

    private fun explainPerimission(activity: Activity, permission: String) {
        ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    private fun checkAuthorized(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

     fun requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
         checkPermissions(activity,permissions)
    }
}