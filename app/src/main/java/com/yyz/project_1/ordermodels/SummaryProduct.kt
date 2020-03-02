package com.yyz.project_1.ordermodels

data class SummaryProduct(
    var productName:String,
    var price:Double,
    var count:Int

){

    constructor():this("",0.0,0){

    }
}