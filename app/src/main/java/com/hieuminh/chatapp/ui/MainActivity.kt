package com.hieuminh.chatapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hieuminh.chatapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
    }
}
