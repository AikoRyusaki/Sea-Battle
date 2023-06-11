package com.bignerdranch.android.seabattle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

var winner = ""

class Game : AppCompatActivity() {

    // объявляем все переменные, которые будут использоваться в классе
    private lateinit var gridViewShipsFirst: GridView
    private lateinit var gridViewShipsSecond: GridView
    private lateinit var textViewMessageFirst: TextView
    private lateinit var textViewMessageSecond: TextView
    private lateinit var resultIntent: Intent

    // объявляем переменные, которые будут использоваться в обработчиках событий
    private var turn = player1Name
    private var usedFirst = mutableListOf<Int>()
    private var usedSecond = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game)

        textViewMessageFirst = findViewById(R.id.grid_first_text)
        textViewMessageSecond = findViewById(R.id.grid_second_text)
        gridViewShipsFirst = findViewById(R.id.grid_view_ships)
        gridViewShipsSecond = findViewById(R.id.grid_view_ships_second)

        gridViewShipsFirst.adapter = ShipsAdapterFirst(this)
        gridViewShipsSecond.adapter = ShipsAdapterSecond(this)

        resultIntent = Intent(this, Result::class.java)

        textViewMessageFirst.append(" $player1Name")
        textViewMessageSecond.append(" $player2Name")
    }

    //Получение используемого адаптера
    private fun returnAdapter(): GridView {
        return if (turn == player1Name){
            gridViewShipsSecond
        } else {
            gridViewShipsFirst
        }
    }

    //Получение используемого списка ходов
    private fun returnUsed(): MutableList<Int> {
        return if (turn == player1Name){
            usedSecond
        } else {
            usedFirst
        }
    }

    //Получение используемого списка подбитых ячеек кораблей
    private fun returnShipList(): MutableList<Ship> {
        return if (turn == player1Name){
            shipListSecond
        } else {
            shipListFirst
        }
    }

    // Моделирование игрового процесса
    private fun gameModel(position: Int){
            //проверяем, открыта ли выбранная ячейка
            if (returnUsed().contains(position)) {
                Toast.makeText(this,"This cell is already open, please select another.", Toast.LENGTH_SHORT).show()
            } else {
                if (hitCheckup(position)){
                    hit(position)
                    if (!shipListFirst.any { it.alive } || !shipListSecond.any { it.alive }) {
                        winner = turn
                        startActivity(resultIntent)
                    }
                } else {
                    miss(position)
                }
            }
    }

    // Проверка попадания
    private fun hitCheckup(position: Int): Boolean{
        return returnShipList().any { it.positions.any { pair -> pair.first == position } }
    }

    // Действия при попадании
    private fun hit(position: Int){
        // Отмечаем попадание на карте
        val cellView = returnAdapter().getChildAt(position)
        cellView.setBackgroundResource(R.drawable.hit)
        // Добавляем позицию в лист ходов
        returnUsed().add(position)
        for (i in 0 until returnShipList().size) {
            for (j in 0 until returnShipList()[i].positions.size) {
                    if (position == returnShipList()[i].positions[j].first){
                        returnShipList()[i].positions[j] = returnShipList()[i].positions[j].copy(second = false)
                        if (returnShipList()[i].positions.all { !it.second }) {
                            returnShipList()[i].alive = false
                            // Показываем корабль на поле
                            addShipOnGrid(returnShipList()[i])
                            shipMiss(returnShipList()[i])
                        }
                    }
                }
            }
        }

    // Действия при промахе
    private fun miss(position: Int){
        // Отмечаем промах на карте
        val cellView = returnAdapter().getChildAt(position)
        cellView.setBackgroundResource(R.drawable.miss)
        // Добавляем позицию в лист ходов
        returnUsed().add(position)
        //Меняем атакующего
        if (turn == player1Name) {
            turn = player2Name
            //Даем возможность использовать доску для хода
            gridViewShipsFirst.isEnabled = true
            gridViewShipsSecond.isEnabled = false

        } else {
            turn = player1Name
            //Даем возможность использовать доску для хода
            gridViewShipsFirst.isEnabled = false
            gridViewShipsSecond.isEnabled = true
        }
        //Сообщаем, чей сейчас ход
        Toast.makeText(this, "$turn's turn", Toast.LENGTH_SHORT).show()
    }

     // Добавление корабля на игровое поле
     private fun addShipOnGrid(ship : Ship){
             if (ship.length == 4) {
                 if (!ship.isHorizontal) {
                     val cellView = returnAdapter().getChildAt(ship.positions[0].first)
                     cellView.setBackgroundResource(R.drawable.ship4_01)
                     val cellView2 = returnAdapter().getChildAt(ship.positions[1].first)
                     cellView2.setBackgroundResource(R.drawable.ship4_02)
                     val cellView3 = returnAdapter().getChildAt(ship.positions[2].first)
                     cellView3.setBackgroundResource(R.drawable.ship4_03)
                     val cellView4 = returnAdapter().getChildAt(ship.positions[3].first)
                     cellView4.setBackgroundResource(R.drawable.ship4_04)
                 } else {
                     val cellView = returnAdapter().getChildAt(ship.positions[0].first)
                     cellView.setBackgroundResource(R.drawable.ship4hor_01)
                     val cellView2 = returnAdapter().getChildAt(ship.positions[1].first)
                     cellView2.setBackgroundResource(R.drawable.ship4hor_02)
                     val cellView3 = returnAdapter().getChildAt(ship.positions[2].first)
                     cellView3.setBackgroundResource(R.drawable.ship4hor_03)
                     val cellView4 = returnAdapter().getChildAt(ship.positions[3].first)
                     cellView4.setBackgroundResource(R.drawable.ship4hor_04)
                 }
             } else if (ship.length == 3) {
                 if (!ship.isHorizontal) {
                     val cellView = returnAdapter().getChildAt(ship.positions[0].first)
                     cellView.setBackgroundResource(R.drawable.ship3_01)
                     val cellView2 = returnAdapter().getChildAt(ship.positions[1].first)
                     cellView2.setBackgroundResource(R.drawable.ship3_02)
                     val cellView3 = returnAdapter().getChildAt(ship.positions[2].first)
                     cellView3.setBackgroundResource(R.drawable.ship3_03)
                 } else {
                     val cellView = returnAdapter().getChildAt(ship.positions[0].first)
                     cellView.setBackgroundResource(R.drawable.ship3hor_01)
                     val cellView2 = returnAdapter().getChildAt(ship.positions[1].first)
                     cellView2.setBackgroundResource(R.drawable.ship3hor_02)
                     val cellView3 = returnAdapter().getChildAt(ship.positions[2].first)
                     cellView3.setBackgroundResource(R.drawable.ship3hor_03)
                 }
             } else if (ship.length == 2) {
                 if (!ship.isHorizontal) {
                     val cellView = returnAdapter().getChildAt(ship.positions[0].first)
                     cellView.setBackgroundResource(R.drawable.ship2_01)
                     val cellView2 = returnAdapter().getChildAt(ship.positions[1].first)
                     cellView2.setBackgroundResource(R.drawable.ship2_02)
                 } else {
                     val cellView = returnAdapter().getChildAt(ship.positions[0].first)
                     cellView.setBackgroundResource(R.drawable.ship2hor_01)
                     val cellView2 = returnAdapter().getChildAt(ship.positions[1].first)
                     cellView2.setBackgroundResource(R.drawable.ship2hor_02)
                 }
             } else {
                 val cellView = returnAdapter().getChildAt(ship.positions[0].first)
                 cellView.setBackgroundResource(R.drawable.ship1)
             }
    }

    // Обведем побежденный корабль
    private fun shipMiss(ship: Ship){
        val shipMissList = mutableListOf<Int>()
        if (ship.isHorizontal){
            shipMissList.add(ship.positions[0].first - 10)
            shipMissList.add(ship.positions[0].first - 1)
            shipMissList.add(ship.positions[0].first + 10)
            shipMissList.add(ship.positions[0].first - 11)
            shipMissList.add(ship.positions[0].first + 9)
            shipMissList.add(ship.positions[ship.length - 1].first - 10)
            shipMissList.add(ship.positions[ship.length - 1].first + 1)
            shipMissList.add(ship.positions[ship.length - 1].first + 10)
            shipMissList.add(ship.positions[ship.length - 1].first - 9)
            shipMissList.add(ship.positions[ship.length - 1].first + 11)
            for (i in 1 until ship.length - 1){
                shipMissList.add(ship.positions[i].first - 10)
                shipMissList.add(ship.positions[i].first + 10)
            }
            if (ship.positions[0].first % 10 == 0){
                shipMissList.removeAt(4)
                shipMissList.removeAt(3)
                shipMissList.removeAt(1)
            } else if (ship.positions[ship.length - 1].first % 10 == 9){
                shipMissList.removeAt(9)
                shipMissList.removeAt(8)
                shipMissList.removeAt(6)
            }
        } else {
            shipMissList.add(ship.positions[0].first - 10)
            shipMissList.add(ship.positions[0].first - 1)
            shipMissList.add(ship.positions[0].first + 1)
            shipMissList.add(ship.positions[0].first - 11)
            shipMissList.add(ship.positions[0].first - 9)
            shipMissList.add(ship.positions[ship.length - 1].first + 10)
            shipMissList.add(ship.positions[ship.length - 1].first + 1)
            shipMissList.add(ship.positions[ship.length - 1].first - 1)
            shipMissList.add(ship.positions[ship.length - 1].first + 9)
            shipMissList.add(ship.positions[ship.length - 1].first + 11)
            for (i in 1 until ship.length - 1) {
                shipMissList.add(ship.positions[i].first - 1)
                shipMissList.add(ship.positions[i].first + 1)
                if (ship.positions[0].first % 10 == 0) {
                    shipMissList.removeAt(shipMissList.size - 2)
                }else if (ship.positions[0].first % 10 == 9) {
                    shipMissList.removeAt(shipMissList.size - 1)
                }
            }
            if (ship.positions[0].first % 10 == 0) {
                shipMissList.removeAt(8)
                shipMissList.removeAt(7)
                shipMissList.removeAt(3)
                shipMissList.removeAt(1)
            } else if (ship.positions[0].first % 10 == 9) {
                shipMissList.removeAt(9)
                shipMissList.removeAt(6)
                shipMissList.removeAt(4)
                shipMissList.removeAt(2)
            }
        }
        for (i in shipMissList.size - 1 downTo 0){
            if (shipMissList[i] < 0 || shipMissList[i] > 99){
                shipMissList.removeAt(i)
            }
        }
        for (i in 0 until shipMissList.size) {
            val cellView = returnAdapter().getChildAt(shipMissList[i])
            cellView.setBackgroundResource(R.drawable.miss)
        }
    }

    private inner class ShipsAdapterFirst(context: Context) : BaseAdapter() {
        private val mContext: Context
        init {
            mContext = context
        }
        override fun getCount(): Int {
            return 100
        }
        override fun getItem(position: Int): Any? {
            return null
        }
        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            var cellView = convertView
            if (cellView == null) {
                cellView = LayoutInflater.from(mContext).inflate(R.layout.grid_cell, parent, false)
            }
            cellView?.setOnClickListener {
                if (turn == player2Name){
                    gameModel(position)
                } else {
                    Toast.makeText(mContext,"Select a cell in another field.", Toast.LENGTH_SHORT).show()
                }
            }
            return cellView
        }
    }

    private inner class ShipsAdapterSecond(context: Context) : BaseAdapter() {
        private val mContext: Context
        init {
            mContext = context
        }
        override fun getCount(): Int {
            return 100
        }
        override fun getItem(position: Int): Any? {
            return null
        }
        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            var cellView = convertView
            if (cellView == null) {
                cellView = LayoutInflater.from(mContext).inflate(R.layout.grid_cell, parent, false)
            }

            cellView?.setOnClickListener {
                if (turn == player1Name){
                    gameModel(position)
                } else {
                    Toast.makeText(mContext,"Select a cell in another field.", Toast.LENGTH_SHORT).show()
                }
            }
            return cellView
        }
    }
}