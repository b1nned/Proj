package com.example.agrichime.agrichime.utilities

interface CartItemBuy {
    abstract var prePaymentfragment: Any
    abstract val RazorPayActivity: Unit

    fun addToOrders(productId: String, quantity: Int, itemCost: Int, deliveryCost: Int)
    abstract fun PrePaymentFragment(): Any
}