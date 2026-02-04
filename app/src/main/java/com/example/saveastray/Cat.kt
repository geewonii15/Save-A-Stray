package com.example.saveastray

data class Cat(
    var name: String = "",
    var breed: String = "",
    var age: String = "",
    var description: String = "",
    var imageUrl: String = "",

    var energyLevel: Int = 3,
    var affectionLevel: Int = 3,
    var socialLevel: Int = 3,
    var noiseLevel: Int = 3,

    var tags: List<String> = listOf()
)