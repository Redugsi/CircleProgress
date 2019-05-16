package com.redugsi.circleprogressexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.redugsi.circleprogress.CircleProgress

class MainActivity : AppCompatActivity() {

    lateinit var runnable: Runnable
    var progressA = 0F


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var progressWheel = findViewById<CircleProgress>(R.id.circleProgress)

        var handler = Handler()

        runnable = Runnable {
            progressWheel.setProgressValue(progressA)
            progressA += 1
            handler.postDelayed(runnable, 100)
        }
        handler.postDelayed(runnable, 1000)
    }
}
