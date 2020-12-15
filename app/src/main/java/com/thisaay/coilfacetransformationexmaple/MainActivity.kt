package com.thisaay.coilfacetransformationexmaple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import com.thisaay.coilfacetransformarionlibrary.FaceCropTransformation
import com.thisaay.coilfacetransformarionlibrary.CoilFaceDetector
import com.thisaay.coilfacetransformationexmaple.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater,null,false) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        CoilFaceDetector.init(this,lifecycle)

        binding.image
            .load("some_url"){
                transformations(FaceCropTransformation())
            }
    }
}
