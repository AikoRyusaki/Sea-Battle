package com.bignerdranch.android.seabattle

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

// Определение глобальных переменных для имен игроков
var player1Name = ""
var player2Name = ""

// Определение класса для ввода имен игроков
class PlayerNames : AppCompatActivity() {

    // Инициализация переменных для кнопки подтверждения и полей ввода имени
    private lateinit var confirmButton: Button
    private lateinit var firstPlayerName: EditText
    private lateinit var secondPlayerName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Установка макета для активности
        setContentView(R.layout.player_names)

        // Нахождение кнопки и полей ввода имени в макете
        confirmButton = findViewById(R.id.confirm_button)
        firstPlayerName = findViewById(R.id.edit_text_name_player_first)
        secondPlayerName = findViewById(R.id.edit_text_name_player_second)

        // Установка обработчика событий на нажатие кнопки подтверждения
        confirmButton.setOnClickListener {
            // Присваивание имени первого игрока значения из поля ввода или "Player1", если поле пустое
            player1Name = firstPlayerName.text.toString().ifBlank {
                "Player1"
            }

            // Присваивание имени второго игрока значения из поля ввода или "Player2", если поле пустое
            player2Name = secondPlayerName.text.toString().ifBlank {
                "Player2"
            }

            // Создание интента для перехода к активности подготовки к игре
            val intent = Intent(this, Preparation::class.java)
            startActivity(intent)
        }
    }
}