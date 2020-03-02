package com.yyz.project_1.dbmanager

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.yyz.project_1.app.Config
import com.yyz.project_1.models.CartItem

class DBHelper(var mContext: Context): SQLiteOpenHelper(mContext, Config.DATABASE_NAME,null,Config.DATABASE_VERSION) {


    companion object{
        const val TABLE_NAME = "cart"
        const val COLUMN_COUNT ="count"
        const val COLUMN_PRICE ="price"
        const val COLUMN_IMAGE ="image"
        const val  COLUMN_PRODUCTNAME ="productname"
        const val COLUMN_SUBID ="subid"

    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTable = "drop table $TABLE_NAME"
        db?.execSQL(dropTable)
        onCreate(db)


    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "create table $TABLE_NAME($COLUMN_COUNT integer,$COLUMN_PRODUCTNAME char(50),$COLUMN_PRICE float,$COLUMN_IMAGE char(50),$COLUMN_SUBID integer)"
        db?.execSQL(createTable)
    }

    fun getCartCount():Int{
        var db: SQLiteDatabase=this.writableDatabase
        var count = 0
        var cursor = db.rawQuery("select * from $TABLE_NAME",null)

        if(cursor.count!=0){
            cursor!!.moveToFirst()

            var currentcount = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_COUNT))
            count = count +currentcount

            while (cursor.moveToNext()) {


                var currentcount = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_COUNT))
                count = count +currentcount

            }
        }






        return count
    }

    fun isItemInCart(name:String):Boolean{
        var db: SQLiteDatabase=this.writableDatabase
        val query = "Select * from $TABLE_NAME where $COLUMN_PRODUCTNAME=?"
        val cursor = db.rawQuery(query, arrayOf(name))
        var count = cursor.count
        return count != 0
    }


    fun insertRecord(cartItem:CartItem){
        var db :SQLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_SUBID,cartItem.subId)
        contentValues.put(COLUMN_COUNT,cartItem.count)
        contentValues.put(COLUMN_PRICE,cartItem.price)
        contentValues.put(COLUMN_IMAGE,cartItem.image)
        contentValues.put(COLUMN_PRODUCTNAME,cartItem.productName)

        if(isItemInCart(cartItem.productName)){
            updateRecord(cartItem)
        }else{
            db.insert(TABLE_NAME,null,contentValues)
        }


    }

   fun updateRecord(cartItem: CartItem) {

       var db :SQLiteDatabase = this.writableDatabase
       val contentValues = ContentValues()
       contentValues.put(COLUMN_SUBID,cartItem.subId)
       contentValues.put(COLUMN_COUNT,cartItem.count)
       contentValues.put(COLUMN_PRICE,cartItem.price)
       contentValues.put(COLUMN_IMAGE,cartItem.image)
       contentValues.put(COLUMN_PRODUCTNAME,cartItem.productName)
       var whereClause = "$COLUMN_PRODUCTNAME = ?"
       var whereArgs = arrayOf(cartItem.productName)
       db.update(TABLE_NAME,contentValues,whereClause,whereArgs)

    }

    fun deleteRecord(name:String){
        var db : SQLiteDatabase =this.writableDatabase
        //val contentValues = ContentValues()

        var whereClause = "$COLUMN_PRODUCTNAME =?"
        var whereArgs = arrayOf(name)
        db.delete(TABLE_NAME,whereClause,whereArgs)
    }




    fun readRecord() : Cursor?{
        var db :SQLiteDatabase =this.writableDatabase
        return db.rawQuery("select * from $TABLE_NAME",null)
    }

    fun QueryRecord(name:String):Cursor?{
        var db: SQLiteDatabase=this.writableDatabase
        var selection = "$COLUMN_PRODUCTNAME=?"
        var selectionArgs = arrayOf(name)
        var columns = arrayOf(COLUMN_COUNT)
        val cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null)


        return cursor
    }

    fun deleteAllRecord(){
        var db : SQLiteDatabase =this.writableDatabase

        db.delete(TABLE_NAME,null,null)

    }


}