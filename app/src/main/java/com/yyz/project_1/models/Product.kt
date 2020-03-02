package com.yyz.project_1.models

import java.io.Serializable

data class Product(
    val __v: Int,
    val _id: String,
    val catId: Int,
    val created: String,
    val description: String,
    val image: String,
    val mrp: Double,
    val position: Int,
    val price: Double,
    val productName: String,
    val quantity: Int,
    val status: Boolean,
    val subId: Int,
    val unit: String
):Serializable{
    constructor(): this(0,"",0,"","","",0.0,0,0.0,"",0,false,0,"")
}