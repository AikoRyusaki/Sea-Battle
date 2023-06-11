package com.bignerdranch.android.seabattle

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

// Определение класса Result, который представляет экран с результатами игры
class Result : AppCompatActivity() {

    // Определение свойств класса, которые представляют кнопки и текстовые поля на экране.
    private lateinit var exitButton: Button
    private lateinit var restartButton: Button
    private lateinit var textViewMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Задание макета экрана
        setContentView(R.layout.result)

        // Нахождение кнопок в макете
        exitButton = findViewById(R.id.exit_button)
        restartButton = findViewById(R.id.restart_button)
        textViewMessage = findViewById(R.id.winner_text)

        // Добавление имени победителя в текстовое поле
        textViewMessage.append(" $winner")

        // Обработчик нажатия на кнопку выхода
        exitButton.setOnClickListener {
            finish() // Завершает текущую активность
            exitProcess(0) // Завершает все процессы приложения
        }

        // Обработчик нажатия на кнопку перезапуска игры
        restartButton.setOnClickListener {
            //очистка списков кораблей для двух игроков
            shipListFirst = mutableListOf<Ship>()
            shipListSecond = mutableListOf<Ship>()
            //передача хода первому игроку
            player = player1Name
            // Создание нового экрана Preparation и запуск его
            val intent = Intent(this, Preparation::class.java)
            startActivity(intent)
        }
    }
}