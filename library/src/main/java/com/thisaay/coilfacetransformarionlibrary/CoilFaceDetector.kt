package com.thisaay.coilfacetransformarionlibrary

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.vision.face.FaceDetector

object CoilFaceDetector : LifecycleObserver {
    private var faceDetector: FaceDetector? = null
    private var context: Context? = null

    fun init(context: Context, lifecycle: Lifecycle) {
        this.context = context.applicationContext
        lifecycle.addObserver(this)
    }

    private fun initDetector() {
        if (faceDetector != null) {
            return
        }

        context?.let {
            faceDetector = FaceDetector.Builder(it)
                .setTrackingEnabled(false)
                .build()
        }
    }

    fun getFaceDetector(): FaceDetector? {
        initDetector()
        return faceDetector
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun releaseDetector() {
        faceDetector?.release()
        faceDetector = null
        context = null
    }
}
