package com.yyz.project_1.ordermodels

import com.yyz.project_1.models.CartItem
import com.yyz.project_1.models.User

data class Order(
    var orderId :String,
    val orderStatus: String,
    var amount : Double,
    val payment: String,
    val products: ArrayList<SummaryProduct>,
    val shippingAddress: ShippingAddress,
    val user: String

){
    constructor(): this("","",0.0,"",
        ArrayList<SummaryProduct>(),ShippingAddress
    (),"")
}