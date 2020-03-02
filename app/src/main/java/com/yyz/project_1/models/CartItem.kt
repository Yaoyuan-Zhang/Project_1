package com.yyz.project_1.models

import java.io.Serializable

data class CartItem (
    val subId: Int,
    var productName:String,
    var image:String,
    var price:Double,
    var count:Int
):Serializable{
    constructor(): this(0,"","",0.0,0){

    }
}