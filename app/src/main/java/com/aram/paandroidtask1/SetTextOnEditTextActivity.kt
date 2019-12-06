package com.aram.paandroidtask1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class SetTextOnEditTextActivity : AppCompatActivity() {

     private var userInput:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        set_btn.setOnClickListener{
            userInput = text_et.text.toString()
            if (userInput.isEmpty()){
                Toast.makeText(this,"no input",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            text_tv.text = userInput
        }
    }
}
