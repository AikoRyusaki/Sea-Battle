package com.bignerdranch.android.seabattle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import kotlin.system.exitProcess

// Определение класса MainActivity, который является главным экраном приложения
class MainActivity : AppCompatActivity() {

    // Определение свойств класса, которые представляют кнопки на экране
    private lateinit var exitButton: Button
    private lateinit var newGameButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Задание макета главного экрана
        setContentView(R.layout.activity_main)

        // Нахождение кнопок в макете
        exitButton = findViewById(R.id.exit_button)
        newGameButton = findViewById(R.id.new_game_button)

        // Обработчик нажатия на кнопку выхода.
        exitButton.setOnClickListener {
            finish() // Завершает текущую активность
            exitProcess(0) // Завершает все процессы приложения
        }

        // Обработчик нажатия на кнопку новой игры.
        newGameButton.setOnClickListener {
            // Создание нового экрана для выбора режима игры и запуск его.
            val intent = Intent(this, PlayerNames::class.java)
            startActivity(intent)
        }
    }
}