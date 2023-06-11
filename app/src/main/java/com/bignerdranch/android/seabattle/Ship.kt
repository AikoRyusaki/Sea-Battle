package com.bignerdranch.android.seabattle

// Определение класса Ship, который представляет корабль со свойствами длины, направления, позиций (пара: индекс, состояние) и состояния
class Ship(val length: Int, var isHorizontal: Boolean, val positions: MutableList<Pair <Int, Boolean>>, var alive: Boolean) {
}