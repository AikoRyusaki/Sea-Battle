package com.bignerdranch.android.seabattle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

// объявляем списки кораблей для двух игроков
var shipListFirst = mutableListOf<Ship>()
var shipListSecond = mutableListOf<Ship>()
var player = player1Name

class Preparation : AppCompatActivity() {

    // объявляем все переменные, которые будут использоваться в классе
    private lateinit var ConfirmButton: Button
    private lateinit var textViewMessage: TextView
    private lateinit var gridViewShips: GridView
    private lateinit var placement4Text: TextView
    private lateinit var placement3Text: TextView
    private lateinit var placement2Text: TextView
    private lateinit var placement1Text: TextView
    private lateinit var shipFourVer: ImageView
    private lateinit var shipFourHor: ImageView
    private lateinit var shipThreeVer: ImageView
    private lateinit var shipThreeHor: ImageView
    private lateinit var shipTwoVer: ImageView
    private lateinit var shipTwoHor: ImageView
    private lateinit var shipOne: ImageView

    // Объявляем переменные, которые будут использоваться в обработчиках событий
    private var ship4 = 1
    private var ship3 = 2
    private var ship2 = 3
    private var ship1 = 4
    private var auto = false
    private var shipList = shipListFirst
    private var selectedShip: Ship? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preparation)

        // находим все элементы интерфейса
        ConfirmButton = findViewById(R.id.confirm_button)
        textViewMessage = findViewById(R.id.preparation_text)
        placement4Text = findViewById(R.id.placement_four_text)
        placement3Text = findViewById(R.id.placement_three_text)
        placement2Text = findViewById(R.id.placement_two_text)
        placement1Text = findViewById(R.id.placement_one_text)
        gridViewShips = findViewById(R.id.grid_view_ships)
        shipFourVer = findViewById(R.id.ship_four_ver)
        shipFourHor = findViewById(R.id.ship_four_hor)
        shipThreeVer = findViewById(R.id.ship_three_ver)
        shipThreeHor = findViewById(R.id.ship_three_hor)
        shipTwoVer = findViewById(R.id.ship_two_ver)
        shipTwoHor = findViewById(R.id.ship_two_hor)
        shipOne = findViewById(R.id.ship_one_ver)

        // выводим сообщение в TextView с именем игрока, который будет заполнять доску
        textViewMessage.append(" $player")

        //изменяем рабочее поле, если изменилось имя игрока
        if (player == player2Name) {
            shipList = shipListSecond
        }

        // выводим количество кораблей разной длины, которые еще нужно расставить
        placement4Text.append(" $ship4")
        placement3Text.append(" $ship3")
        placement2Text.append(" $ship2")
        placement1Text.append(" $ship1")

        // создаем адаптер для GridView и устанавливаем его
        gridViewShips.adapter = ShipsAdapter(this)

        // устанавливаем обработчики событий для кнопок выбора корабля
        shipFourVer.setOnClickListener {
            selectedShip = Ship(4, false, mutableListOf(), true)
        }
        shipFourHor.setOnClickListener {
            selectedShip = Ship(4, true, mutableListOf(), true)
        }
        shipThreeVer.setOnClickListener {
            selectedShip = Ship(3, false, mutableListOf(), true)
        }
        shipThreeHor.setOnClickListener {
            selectedShip = Ship(3, true, mutableListOf(), true)
        }
        shipTwoVer.setOnClickListener {
            selectedShip = Ship(2, false, mutableListOf(),true)
        }
        shipTwoHor.setOnClickListener {
            selectedShip = Ship(2, true, mutableListOf(),true)
        }
        shipOne.setOnClickListener {
            selectedShip = Ship(1, false, mutableListOf(),true)
        }

        //Переход на страничку с игрой или заполнение доски второго игрока
        ConfirmButton.setOnClickListener {
            if (shipList.size == 10) {
                if (player == player1Name) {
                    player = player2Name
                    val intent = Intent(this, Preparation::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, Game::class.java)
                    startActivity(intent)
                }
            } else {
                //выводим сообщение об ошибке, если не все корабли расставлены
                Toast.makeText(this,"Not all ships are in their places.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Определение координат корабля
    fun preparation (position: Int, selectedShip: Ship): MutableList<Int> {
        // определяем номер строки и столбца ячейки, на которую нажал пользователь
        val row = position / 10
        val col = position % 10

        // определяем номера ячеек, которые занимает корабль
        val cellNumbers = mutableListOf<Int>()
        if (selectedShip.isHorizontal) {
            for (i in 0 until selectedShip.length) {
                val cellNumber = row * 10 + col + i
                cellNumbers.add(cellNumber)
            }
        } else {
            for (i in 0 until selectedShip.length) {
                val cellNumber = (row + i) * 10 + col
                cellNumbers.add(cellNumber)
            }
        }
        return cellNumbers
    }

    // определение корректности расстановки
    fun correct(cellNumbers: MutableList<Int>, shipList: MutableList<Ship>): Boolean {
        var correct = true
        if (selectedShip == null){
            correct = false
        }
        if (cellNumbers[cellNumbers.size - 1] % 10 < cellNumbers[0] % 10) {
            correct = false
        }
        for (n in 0 until cellNumbers.size) {
            if (cellNumbers[n] > 100) {
                correct = false
            }
        }
        for (i in 0 until shipList.size) {
            for (j in 0 until shipList[i].positions.size) {
                for (n in 0 until cellNumbers.size) {
                    if (cellNumbers[n] % 10 == 0) {
                        if (cellNumbers[n] == shipList[i].positions[j].first ||
                            cellNumbers[n] + 1 == shipList[i].positions[j].first ||
                            cellNumbers[n] + 10 == shipList[i].positions[j].first ||
                            cellNumbers[n] - 10 == shipList[i].positions[j].first ||
                            cellNumbers[n] + 11 == shipList[i].positions[j].first ||
                            cellNumbers[n] - 9 == shipList[i].positions[j].first
                        ) {
                            correct = false
                        }
                    } else if (cellNumbers[n] % 10 == 9) {
                        if (cellNumbers[n] == shipList[i].positions[j].first ||
                            cellNumbers[n] - 1 == shipList[i].positions[j].first ||
                            cellNumbers[n] + 10 == shipList[i].positions[j].first ||
                            cellNumbers[n] - 10 == shipList[i].positions[j].first ||
                            cellNumbers[n] + 9 == shipList[i].positions[j].first ||
                            cellNumbers[n] - 11 == shipList[i].positions[j].first
                        ) {
                            correct = false
                        }
                    } else {
                        if (cellNumbers[n] == shipList[i].positions[j].first ||
                            cellNumbers[n] + 1 == shipList[i].positions[j].first ||
                            cellNumbers[n] - 1 == shipList[i].positions[j].first ||
                            cellNumbers[n] + 10 == shipList[i].positions[j].first ||
                            cellNumbers[n] - 10 == shipList[i].positions[j].first ||
                            cellNumbers[n] + 11 == shipList[i].positions[j].first ||
                            cellNumbers[n] + 9 == shipList[i].positions[j].first ||
                            cellNumbers[n] - 11 == shipList[i].positions[j].first ||
                            cellNumbers[n] - 9 == shipList[i].positions[j].first
                        ) {
                            correct = false
                        }
                    }
                }
            }
        }
        if (!auto && !correct) {
            Toast.makeText(this, "Please select a different cell.", Toast.LENGTH_SHORT).show()
        }
        auto = false
        return correct
    }

    // добавление корабля
    fun addShip(cellNumbers: MutableList<Int>, shipList: MutableList<Ship>, selectedShip: Ship) {
        selectedShip.positions.addAll(cellNumbers.map { it to true })
        shipList.add(selectedShip)
        if (selectedShip.length == 4) {
            ship4 -= 1
            if (ship4 == 0) {
                shipFourVer.isEnabled = false
                shipFourHor.isEnabled = false
            }
        } else if (selectedShip.length == 3) {
            ship3 -= 1
            if (ship3 == 0) {
                shipThreeVer.isEnabled = false
                shipThreeHor.isEnabled = false
            }
        } else if (selectedShip.length == 2) {
            ship2 -= 1
            if (ship2 == 0) {
                shipTwoVer.isEnabled = false
                shipTwoHor.isEnabled = false
            }
        } else if (selectedShip.length == 1) {
            ship1 -= 1
            if (ship1 == 0) {
                shipOne.isEnabled = false
            }
        }
    }

    // добавление корабля на игровое поле
    fun addShipOnGrid(cellNumbers: MutableList<Int>, selectedShip: Ship){
        if (selectedShip.length == 4) {
            placement4Text.text = placement4Text.text.substring(0, placement4Text.length() - 1) + ship4
            if (!selectedShip.isHorizontal) {
                val cellView = gridViewShips.getChildAt(cellNumbers[0])
                cellView.setBackgroundResource(R.drawable.ship4_01)
                val cellView2 = gridViewShips.getChildAt(cellNumbers[1])
                cellView2.setBackgroundResource(R.drawable.ship4_02)
                val cellView3 = gridViewShips.getChildAt(cellNumbers[2])
                cellView3.setBackgroundResource(R.drawable.ship4_03)
                val cellView4 = gridViewShips.getChildAt(cellNumbers[3])
                cellView4.setBackgroundResource(R.drawable.ship4_04)
            } else {
                val cellView = gridViewShips.getChildAt(cellNumbers[0])
                cellView.setBackgroundResource(R.drawable.ship4hor_01)
                val cellView2 = gridViewShips.getChildAt(cellNumbers[1])
                cellView2.setBackgroundResource(R.drawable.ship4hor_02)
                val cellView3 = gridViewShips.getChildAt(cellNumbers[2])
                cellView3.setBackgroundResource(R.drawable.ship4hor_03)
                val cellView4 = gridViewShips.getChildAt(cellNumbers[3])
                cellView4.setBackgroundResource(R.drawable.ship4hor_04)
            }
        } else if (selectedShip.length == 3) {
            placement3Text.text = placement3Text.text.substring(0, placement4Text.length() - 1) + ship3
            if (!selectedShip.isHorizontal) {
                val cellView = gridViewShips.getChildAt(cellNumbers[0])
                cellView.setBackgroundResource(R.drawable.ship3_01)
                val cellView2 = gridViewShips.getChildAt(cellNumbers[1])
                cellView2.setBackgroundResource(R.drawable.ship3_02)
                val cellView3 = gridViewShips.getChildAt(cellNumbers[2])
                cellView3.setBackgroundResource(R.drawable.ship3_03)
            } else {
                val cellView = gridViewShips.getChildAt(cellNumbers[0])
                cellView.setBackgroundResource(R.drawable.ship3hor_01)
                val cellView2 = gridViewShips.getChildAt(cellNumbers[1])
                cellView2.setBackgroundResource(R.drawable.ship3hor_02)
                val cellView3 = gridViewShips.getChildAt(cellNumbers[2])
                cellView3.setBackgroundResource(R.drawable.ship3hor_03)
            }
        } else if (selectedShip.length == 2) {
            placement2Text.text = placement2Text.text.substring(0, placement4Text.length() - 1) + ship2

            if (!selectedShip.isHorizontal) {
                val cellView = gridViewShips.getChildAt(cellNumbers[0])
                cellView.setBackgroundResource(R.drawable.ship2_01)
                val cellView2 = gridViewShips.getChildAt(cellNumbers[1])
                cellView2.setBackgroundResource(R.drawable.ship2_02)
            } else {
                val cellView = gridViewShips.getChildAt(cellNumbers[0])
                cellView.setBackgroundResource(R.drawable.ship2hor_01)
                val cellView2 = gridViewShips.getChildAt(cellNumbers[1])
                cellView2.setBackgroundResource(R.drawable.ship2hor_02)
            }
        } else if (selectedShip.length == 1) {
            placement1Text.text = placement1Text.text.substring(0, placement4Text.length() - 1) + ship1

            for (i in 0 until cellNumbers.size) {
                val cellView = gridViewShips.getChildAt(cellNumbers[i])
                cellView.setBackgroundResource(R.drawable.ship1)
            }
        }
    }

    // удаление корабля
    fun deleteShip(position: Int,shipList: MutableList<Ship>) {
        // Ищем корабль, который содержит указанную позицию
        val shipToRemove = shipList.find { it.positions.any { it.first == position } }
        if (shipToRemove != null) {
            // Находим оставшиеся координаты корабля
            val positionsToRemove = shipToRemove.positions.map { it.first } ?: emptyList()
            //Удаляем корабль с доски
            for (i in 0 until shipToRemove.length) {
                val cellView = gridViewShips.getChildAt(positionsToRemove[i])
                cellView.setBackgroundResource(R.drawable.grid_cell_border)
            }

            //Меняем количество кораблей для расстановки
            if (shipToRemove.length == 4) {
                ship4 += 1
                placement4Text.text = placement4Text.text.substring(0, placement4Text.length() - 1) + ship4

                if (ship4 != 0) {
                    shipFourVer.isEnabled = true
                    shipFourHor.isEnabled = true
                }
            }
            if (shipToRemove.length == 3) {
                ship3 += 1
                placement3Text.text = placement3Text.text.substring(0, placement4Text.length() - 1) + ship3

                if (ship3 != 0) {
                    shipThreeVer.isEnabled = true
                    shipThreeHor.isEnabled = true
                }
            }
            if (shipToRemove.length == 2) {
                ship2 += 1
                placement2Text.text = placement2Text.text.substring(0, placement4Text.length() - 1) + ship2

                if (ship2 != 0) {
                    shipTwoVer.isEnabled = true
                    shipTwoHor.isEnabled = true
                }
            }
            if (shipToRemove.length == 1) {
                ship1 += 1
                placement1Text.text = placement1Text.text.substring(0, placement4Text.length() - 1) + ship1

                if (ship1 != 0) {
                    shipOne.isEnabled = true
                }
            }

            // Удаляем корабль из списка
            shipList.remove(shipToRemove)
        }
    }

    private inner class ShipsAdapter(context: Context) : BaseAdapter() {
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
                if (selectedShip != null) {
                    if (correct(preparation(position, selectedShip!!), shipList)) {
                        addShip((preparation(position, selectedShip!!)), shipList, selectedShip!!)
                        addShipOnGrid((preparation(position, selectedShip!!)),selectedShip!!)
                        selectedShip = null
                    }
                    } else {
                        deleteShip(position, shipList)
                }
            }
            return cellView
        }
    }
}