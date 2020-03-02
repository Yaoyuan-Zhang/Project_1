package com.yyz.project_1.models

data class CartItemList (
    var data :ArrayList<CartItem>
){
    constructor(): this(ArrayList())
}