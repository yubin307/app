package com.example.w04


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val profileImage: ImageView = findViewById(R.id.profileImage)
        val nameText: TextView = findViewById(R.id.nameText)
        val introText: TextView = findViewById(R.id.introText)

        // 이미지, 이름, 자기소개 변경 가능
        profileImage.setImageResource(R.drawable.ic_launcher_foreground)
        nameText.text = "임유빈"
        introText.text = "프로그래밍을 배우고 있는 임유빈 입니다."
    }
}
