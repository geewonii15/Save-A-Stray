package com.example.saveastray

data class Cat(
    var id: String = "",
    val name: String = "",
    val breed: String = "",
    val age: String = "",
    val description: String = "",
    val imageUrl: String = "",

    val energyLevel: Int = 3,
    val affectionLevel: Int = 3,
    val socialLevel: Int = 3,
    val noiseLevel: Int = 3
)