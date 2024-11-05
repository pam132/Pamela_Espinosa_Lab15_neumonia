package com.google.devrel.pamela_espinosa_lab15_neumonia

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.mlkit.common.model.LocalModel
import com.google.devrel.pamela_espinosa_lab15_neumonia.R.id.imageToLabel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import java.io.IOException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val localModel = LocalModel.Builder()
            .setAssetFilePath("pamelan2.tflite")
            .build()
        val img: ImageView = findViewById(imageToLabel)
        // assets folder image file name with extension
        val fileName = "NORMAL2-IM-0892-0001.jpeg"
        // get bitmap from assets folder
        val bitmap: Bitmap? = assetsToBitmap(fileName)
        bitmap?.apply {
            img.setImageBitmap(this)
        }
        val txtOutput : TextView = findViewById(R.id.txtOutput)
        val btn: Button = findViewById(R.id.btnTest)
        btn.setOnClickListener {
            val options = CustomImageLabelerOptions.Builder(localModel)
                .setConfidenceThreshold(0.7f)
                .setMaxResultCount(4)
                .build()
            val labeler = ImageLabeling.getClient(options)
            val image = InputImage.fromBitmap(bitmap!!, 0)
            var outputText = ""
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    // Task completed successfully
                    for (label in labels) {
                        val text = label.text
                        val confidence = label.confidence
                        outputText += "$text : $confidence\n"
                        //val index = label.index
                    }
                    txtOutput.text = outputText
                }
                .addOnFailureListener { _ ->
                    // Task failed with an exception
                    // ...
                }

        }
    }
}

// extension function to get bitmap from assets
fun Context.assetsToBitmap(fileName: String): Bitmap?{
    return try {
        with(assets.open(fileName)){
            BitmapFactory.decodeStream(this)
        }
    } catch (e: IOException) { null }
}