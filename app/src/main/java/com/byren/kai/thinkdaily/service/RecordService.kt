package com.byren.kai.thinkdaily.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.Binder
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.support.annotation.RequiresApi
import java.io.File
import java.io.IOException

@SuppressLint("Registered")
class RecordService : Service() {
    private var recorder: MediaRecorder? = null
    private var filePath = File(Environment.getExternalStorageDirectory(), "tdaily")
    private var filName = ""
    private var startTime = 0L
    private var time = 0L
    override fun onBind(intent: Intent?): IBinder {
        return Binder()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        startRecord()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecord()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startRecord() {
        recorder = MediaRecorder()
        recorder!!.setAudioChannels(1)
        recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)

        recorder!!.setOutputFile(filePath)
        recorder!!.setAudioSamplingRate(44100)
        recorder!!.setAudioEncodingBitRate(192000)
        try {
            recorder!!.prepare()
            recorder!!.start()
            startTime = System.currentTimeMillis()
        } catch (e: IOException) {

        }
    }

    private fun stopRecord() {
        if (recorder != null) {
            recorder!!.stop()
            time = System.currentTimeMillis() - startTime
//            saveRecord()
            recorder!!.release()
        }
    }

    private fun saveRecord() {

    }
}