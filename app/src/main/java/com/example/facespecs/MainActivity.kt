package com.example.facespecs

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.minus
import androidx.core.view.updateLayoutParams
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark

class MainActivity : AppCompatActivity() {

    private lateinit var rootView: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val faceImageView = findViewById<ImageView>(R.id.faceImageView)
        val specImageView = findViewById<ImageView>(R.id.specImageView)
         rootView = findViewById(R.id.rootView)

//        val imageBitmap = BitmapFactory.decodeResource(
//            resources,
//            R.drawable.person_lowres_face);

        val imageBitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.person_lowres_face,
            )
//        specImageView.setImageBitmap(imageBitmap)
        val perfectImage = Bitmap.createScaledBitmap(imageBitmap, resources.displayMetrics.widthPixels, imageBitmap.height, true)
//        val scaledBitmap = Bitmap.



// High-accuracy landmark detection and face classification
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
//            .setContourMode()
            .build()

// Real-time contour detection
        val realTimeOpts = FaceDetectorOptions.Builder()
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .build()

        val image = InputImage.fromBitmap(perfectImage, 0)

        val detector = FaceDetection.getClient(realTimeOpts)

        val result = detector.process(image)
            .addOnSuccessListener { faces ->
                // Task completed successfully
                // ...

                for (face in faces) {
                    val bounds = face.boundingBox
                    val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                    val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees

                    // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                    // nose available):
                    val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
                    val rightEar = face.getLandmark(FaceLandmark.RIGHT_EAR)!!.position
                    val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)!!.position
                    leftEar?.let {
                        val leftEarPos = leftEar.position
                        specImageView.visibility = View.VISIBLE
                        faceImageView.visibility = View.VISIBLE
                        faceImageView.setImageBitmap(perfectImage)
                        specImageView.x = leftEarPos.x
                        specImageView.updateLayoutParams {
                            width = (rightEar.x - leftEarPos.x).toInt()
                        }
                        Log.d("sahil", "x is : ${leftEarPos.x} and y is => ${leftEarPos.y}")

                    }

//                     If contour detection was enabled:
                    val leftEyeContour = face.getContour(FaceContour.LEFT_EYEBROW_TOP)?.points
//                    val upperLipBottomContour = face.getContour(FaceContour.RIGHT_EYE)?.points
//                    val upperLip = face.getContour(FaceContour.UPPER_LIP_TOP)?.points
//
////                    face.getContour(FaceContour.LEFT_EYE).
                    leftEyeContour?.let {
//                        val leftEarPos = leftEar?.position
//                        specImageView.visibility = View.VISIBLE
//                        faceImageView.visibility = View.VISIBLE
//                        faceImageView.setImageBitmap(imageBitmap)
////                        it.forEach { coordinates ->
////                            showOverlay(coordinates.x, coordinates.y)
////                        }
//                        showOverlay(it[0].x, it[0].y)
//                        specImageView.x = it[0].x
//                        specImageView.offsetLeftAndRight(it[0].offset())
                        specImageView.y = it[0].y

                    }
                }

//                    upperLipBottomContour?.let{
////                        val leftEarPos = leftEar?.position
////                        specImageView.visibility = View.VISIBLE
////                        faceImageView.visibility = View.VISIBLE
////                        faceImageView.setImageBitmap(imageBitmap)
//                        it.forEach { coordinates ->
//                            showOverlay(coordinates.x, coordinates.y)
//                        }
////                        showOverlay(it[0].x, it[0].y)
////                        specImageView.x = it[0].x
////                        specImageView.y = it[0].y
//                    }
//
//                    upperLip?.let{
////                        val leftEarPos = leftEar?.position
////                        specImageView.visibility = View.VISIBLE
////                        faceImageView.visibility = View.VISIBLE
////                        faceImageView.setImageBitmap(imageBitmap)
//                        it.forEach { coordinates ->
//                            showOverlay(coordinates.x, coordinates.y)
//                        }
////                        showOverlay(it[0].x, it[0].y)
////                        specImageView.x = it[0].x
////                        specImageView.y = it[0].y
//                    }

//
//                    // If classification was enabled:
//                    if (face.smilingProbability != null) {
//                        val smileProb = face.smilingProbability
//                    }
//                    if (face.rightEyeOpenProbability != null) {
//                        val rightEyeOpenProb = face.rightEyeOpenProbability
//                    }
//
//                    // If face tracking was enabled:
//                    if (face.trackingId != null) {
//                        val id = face.trackingId
//                    }
//                }
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
            }

    }

    private fun showOverlay(x: Float, y: Float){
        val imageview = ImageView(this)
        imageview.setImageResource(R.drawable.circle)
        imageview.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        imageview.x = x
        imageview.y = y
        rootView.addView(imageview)

    }
}