package sg.com.fairtech.randomize.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sg.com.fairtech.randomize.R
import sg.com.fairtech.randomize.viewmodel.RandomizeViewModel

class MainActivity2 : AppCompatActivity() {
    private lateinit var randomizeViewModel: RandomizeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
    }
}