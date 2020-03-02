package com.yyz.project_1.models

data class FavoriteProduct (
    var likeProduct : Product,
    var user:String
){
    constructor():this(Product(),"")
}