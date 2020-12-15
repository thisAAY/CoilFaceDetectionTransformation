package com.thisaay.coilfacetransformarionlibrary

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF
import android.util.SparseArray
import coil.bitmap.BitmapPool
import coil.size.PixelSize
import coil.size.Size
import coil.transform.Transformation
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import java.lang.IllegalStateException
import kotlin.math.max


class FaceCenterCrop : Transformation {
    override fun key(): String = "face_crop_transformation"

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        if (size !is PixelSize) {
            return input
        }
        val width = size.width
        val height = size.height

        val scaleX: Float = width.toFloat() / input.width
        val scaleY: Float = height.toFloat() / input.height

        return if (scaleX != scaleY) {
            val config =
                if (input.config != null) input.config else Bitmap.Config.ARGB_8888
            val result: Bitmap = pool.get(width, height, config)
            val scale = max(scaleX, scaleY)
            var left = 0f
            var top = 0f
            var scaledWidth: Float = width.toFloat()
            var scaledHeight: Float = height.toFloat()
            val focusPoint = PointF()
            detectFace(input, focusPoint)
            if (scaleX < scaleY) {
                scaledWidth = scale * input.getWidth()
                val faceCenterX = scale * focusPoint.x
                left = getLeftPoint(size.width, scaledWidth, faceCenterX)
            } else {
                scaledHeight = scale * input.getHeight()
                val faceCenterY = scale * focusPoint.y
                top = getTopPoint(size.height, scaledHeight, faceCenterY)
            }
            val targetRect = RectF(left, top, left + scaledWidth, top + scaledHeight)
            val canvas = Canvas(result)
            canvas.drawBitmap(input, null, targetRect, null)
            result
        } else {
            input
        }
    }

    private fun detectFace(bitmap: Bitmap, centerOfAllFaces: PointF) {
        val faceDetector: FaceDetector? = CoilFaceDetector.getFaceDetector()
        if (faceDetector == null){
            throw IllegalStateException("You need to call CoilFaceDetector first")
        }
        if (!faceDetector.isOperational) {
            centerOfAllFaces[bitmap.width / 2.toFloat()] =
                bitmap.height / 2.toFloat() // center crop
            return
        }
        val frame: Frame = Frame.Builder().setBitmap(bitmap).build()
        val faces: SparseArray<Face> = faceDetector.detect(frame)
        val totalFaces = faces.size()
        if (totalFaces > 0) {
            var sumX = 0f
            var sumY = 0f
            for (i in 0 until totalFaces) {
                val faceCenter = PointF()
                getFaceCenter(faces[faces.keyAt(i)], faceCenter)
                sumX += faceCenter.x
                sumY += faceCenter.y
            }
            centerOfAllFaces[sumX / totalFaces] = sumY / totalFaces
            return
        }
        centerOfAllFaces[bitmap.width / 2.toFloat()] = bitmap.height / 2.toFloat() // center crop
    }

    /**
     * Calculates center of a given face
     *
     * @param face   Face
     * @param center Center of the face
     */
    private fun getFaceCenter(face: Face, center: PointF) {
        val x: Float = face.position.x
        val y: Float = face.position.y
        val width: Float = face.width
        val height: Float = face.height
        center[x + width / 2] = y + height / 2 // face center in original bitmap
    }

    private fun getTopPoint(height: Int, scaledHeight: Float, faceCenterY: Float): Float {
        return if (faceCenterY <= height / 2) { // Face is near the top edge
            0f
        } else if (scaledHeight - faceCenterY <= height / 2) { // face is near bottom edge
            height - scaledHeight
        } else {
            height / 2 - faceCenterY
        }
    }

    private fun getLeftPoint(width: Int, scaledWidth: Float, faceCenterX: Float): Float {
        return if (faceCenterX <= width / 2) { // face is near the left edge.
            0f
        } else if (scaledWidth - faceCenterX <= width / 2) {  // face is near right edge
            width - scaledWidth
        } else {
            width / 2 - faceCenterX
        }
    }
}
