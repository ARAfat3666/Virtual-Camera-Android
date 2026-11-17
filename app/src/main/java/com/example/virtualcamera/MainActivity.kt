
package com.example.virtualcamera

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import android.content.Intent

class MainActivity : Activity() {

    private lateinit var imageView: ImageView
    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(24, 24, 24, 24)

        val title = TextView(this)
        title.text = "Virtual Camera"
        title.textSize = 26f
        title.gravity = Gravity.CENTER

        val photoButton = Button(this)
        photoButton.text = "Select Photo"

        val videoButton = Button(this)
        videoButton.text = "Select Video"

        imageView = ImageView(this)
        imageView.adjustViewBounds = true
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER

        videoView = VideoView(this)

        layout.addView(title)
        layout.addView(photoButton)
        layout.addView(videoButton)
        layout.addView(imageView,
            LinearLayout.LayoutParams(-1, 500))
        layout.addView(videoView,
            LinearLayout.LayoutParams(-1, 500))

        setContentView(layout)

        photoButton.setOnClickListener {
            openGallery("image/*")
        }

        videoButton.setOnClickListener {
            openGallery("video/*")
        }
    }

    private fun openGallery(type: String) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = type
        startActivityForResult(intent, 100)
    }

    @Deprecated("Deprecated in Android")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 &&
            resultCode == RESULT_OK &&
            data?.data != null) {

            val uri = data.data!!

            if (contentResolver.getType(uri)?.startsWith("video") == true) {
                imageView.setImageDrawable(null)
                videoView.setVideoURI(uri)
                videoView.start()
            } else {
                videoView.stopPlayback()
                imageView.setImageURI(uri)
            }
        }
    }
}
