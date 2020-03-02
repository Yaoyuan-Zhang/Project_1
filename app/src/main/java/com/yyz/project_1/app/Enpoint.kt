package com.yyz.project_1.app

class Enpoint {
    companion object{
        const val URL_LOGIN = "auth/login"
        const val URL_REGISTER ="auth/register"
        const val URL_CATEGORY = "category"
        const val URL_SUBCATEGORY = "subcategory/"
        const val URL_PRODUCT = "products/"

        fun getLogin():String{
            return Config.BASE_URL+ URL_LOGIN
        }

        fun getRegister():String{
            return Config.BASE_URL+ URL_REGISTER
        }

        fun getCategory():String{
            return Config.BASE_URL+ URL_CATEGORY
        }

        fun getSubCategory():String{
            return Config.BASE_URL+ URL_SUBCATEGORY
        }

        fun getProduct():String{
            return Config.BASE_URL+ URL_PRODUCT
        }
    }

}