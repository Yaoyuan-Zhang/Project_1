package com.yyz.project_1.models

data class ProductList (
    var data :ArrayList<Product>
){
    constructor():this(ArrayList<Product>())
}
