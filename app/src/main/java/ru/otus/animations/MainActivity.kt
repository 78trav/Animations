package ru.otus.animations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.otus.animations.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        val b = ActivityMainBinding.inflate(layoutInflater)

        setContentView(b.root)

        b.figure1.setOnClickListener {
            b.figure1.repeatCount = b.figureRepeat.value.toInt()
            b.figure1.duration = b.figureDuration.value.toInt() * 1000
            b.figure1.startAnimation()
        }

        b.figure2.setOnClickListener {
            b.figure2.repeatCount = b.figureRepeat.value.toInt()
            b.figure2.duration = b.figureDuration.value.toInt() * 1000
            b.figure2.startAnimation()
        }

    }
}
