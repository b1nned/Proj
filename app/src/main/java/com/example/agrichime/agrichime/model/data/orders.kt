package com.example.agrichime.agrichime.model.data

data class orders(
    var name: String,
    var locality: String,
    var city: String,
    var state: String,
    var pincode: String,
    var mobile: String,
    var time: String,
    var productId: String,
    var itemCost: Int,
    var quantity: Int,
    var deliveryCost: Int,
    var deliveryStatus: String
)