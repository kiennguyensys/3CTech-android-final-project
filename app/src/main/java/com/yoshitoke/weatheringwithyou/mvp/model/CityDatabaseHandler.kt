package com.yoshitoke.weatheringwithyou.mvp.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import com.yoshitoke.weatheringwithyou.mvp.model.DataClass.City

class CityDatabaseHandler(context: Context) : SQLiteOpenHelper(context, DBName, null, DBVersion) {


    companion object {

        val Tag = "CityDatabaseHandler"
        val DBName = "CityDB"
        val DBVersion = 1

        val tableName = "cityTable"
        const val CITY_ID = "id"
        const val CITY_NAME = "name"
        const val CITY_LAT = "latitude"
        const val CITY_LON = "longitude"
    }

    var context: Context? = context
    var sqlObj: SQLiteDatabase

    init {
        sqlObj = this.writableDatabase
    }

    override fun onCreate(sqlite: SQLiteDatabase?) {

        //SQL for creating table
        val sql1: String = "CREATE TABLE IF NOT EXISTS " + tableName + " " +
                "(" + CITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                CITY_NAME + " TEXT, " + CITY_LAT + " TEXT, " + CITY_LON +
                " TEXT );"

        sqlite?.execSQL(sql1);
    }

    override fun onUpgrade(sqlite: SQLiteDatabase?, p1: Int, p2: Int) {

        sqlite?.execSQL("Drop table IF EXISTS " + tableName)
        onCreate(sqlite)

    }

    fun AddCity(values: ContentValues): Boolean {

        var Msg: String = "error";
        val ID = sqlObj.insert(tableName, "", values)

        return ID > 0
    }

    fun FetchCities(keyword: String): List<City> {

        val arraylist = ArrayList<City>()

        val sqb = SQLiteQueryBuilder()
        sqb.tables = tableName
        val cols = arrayOf(CITY_ID, CITY_NAME, CITY_LAT, CITY_LON)
        val rowSelArg = arrayOf(keyword)

        val cur = sqb.query(sqlObj, cols, "name like ?", rowSelArg, null, null, "name")

        if (cur.moveToFirst()) {

            do {
                //val id = cur.getInt(cur.getColumnIndex("id"))
                val cityName = cur.getString(cur.getColumnIndex(CITY_NAME))
                val cityLat = cur.getString(cur.getColumnIndex(CITY_LAT))
                val cityLon = cur.getString(cur.getColumnIndex(CITY_LON))

                //add id
                arraylist.add(City(cityName, cityLon.toDouble(), cityLat.toDouble()))

            } while (cur.moveToNext())
        }

        return arraylist
    }

    fun UpdateCity(values: ContentValues, id: Int): Boolean {

        val selectionArs = arrayOf(id.toString())

        val i = sqlObj.update(tableName, values, "id=?", selectionArs)
        return i > 0
    }

    fun RemoveCity(id: Int): Boolean {

        val selectionArs = arrayOf(id.toString())

        val i = sqlObj.delete(tableName, "id=?", selectionArs)
        return i > 0
    }
}
