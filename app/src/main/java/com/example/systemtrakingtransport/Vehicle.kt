package com.example.systemtrakingtransport;

data class Vehicle(
    var id: Long? = null,
    var brand: String,
    var model: String,
    var year: String,
    var type: String
)

data class Sedan(
    var id: Long? = null,
    var vehicleId: Long
)

data class Wagon(
    var id: Long? = null,
    var vehicleId: Long
)

data class SUV(
    var id: Long? = null,
    var vehicleId: Long
)