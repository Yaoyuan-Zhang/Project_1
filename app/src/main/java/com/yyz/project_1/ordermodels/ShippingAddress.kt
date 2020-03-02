package com.yyz.project_1.ordermodels

data class ShippingAddress (
    var user_email : String,
    var address1: String,
    var address2: String,
    var city : String,
    var State : String,
    var zipcode : String
){
    constructor():this("","","","","",""){

    }

}
