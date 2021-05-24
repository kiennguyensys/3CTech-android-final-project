package com.yoshitoke.weatheringwithyou.mvp.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.provider.BaseColumns
import android.util.Log
import com.yoshitoke.weatheringwithyou.alarm.AlarmData
import com.yoshitoke.weatheringwithyou.mvp.model.DataClass.City

class AlarmDatabaseHandler(context: Context) : SQLiteOpenHelper(context, DBName, null, DBVersion) {


    companion object {

        val Tag = "AlarmDatabaseHandler"
        val DBName = "AlarmDB"
        val DBVersion = 1

        val tableName = "alarmTable"
        const val ALARM_ID = "id"
        const val ALARM_WEATHER_TYPE = "weather_type"
        const val ALARM_HOUR = "hour"
        const val ALARM_MINUTE = "minute"
        const val ALARM_DAYS = "days"
        const val ALARM_ADMINISTERED = "administered"
    }

    var context: Context? = context
    var sqlObj: SQLiteDatabase

    init {
        sqlObj = this.writableDatabase
    }

    fun createContentValues(alarmData: AlarmData): ContentValues {

        return ContentValues().apply {
            put(ALARM_HOUR, alarmData.hour)
            put(ALARM_MINUTE, alarmData.minute)
            val stringBuilder: StringBuilder
            if (alarmData.days != null) {
                stringBuilder = StringBuilder()

                alarmData.days?.forEachIndexed { index, day ->
                    if (day != null) {
                        stringBuilder.append(day)
                        if (index - 1 < alarmData.days?.size as Int) {
                            stringBuilder.append("-")
                        }
                    }
                }

                put(ALARM_DAYS, stringBuilder.toString())
            }

            val typeStringBuilder: StringBuilder
            if (alarmData.weatherTypes != null) {
                typeStringBuilder = StringBuilder()

                alarmData.weatherTypes?.forEachIndexed { index, weather ->
                    if (weather != null) {
                        typeStringBuilder.append(weather)
                        if (index - 1 < alarmData.weatherTypes?.size as Int) {
                            typeStringBuilder.append("-")
                        }
                    }
                }

                put(ALARM_WEATHER_TYPE, typeStringBuilder.toString())
            }
            put(ALARM_ADMINISTERED, if (alarmData.administered) 1 else 0)
        }
    }

    fun createAlarmFromCursor(cursor: Cursor): AlarmData {
        return AlarmData().apply {
            id = cursor.getInt(cursor.getColumnIndex(ALARM_ID))
            hour = cursor.getInt(cursor.getColumnIndex(ALARM_HOUR))
            minute = cursor.getInt(cursor.getColumnIndex(ALARM_MINUTE))
            val daysLocal = cursor.getString(cursor.getColumnIndex(ALARM_DAYS))
            if (daysLocal != null) {
                days = daysLocal.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toList()
            }
            val types = cursor.getString(cursor.getColumnIndex(ALARM_WEATHER_TYPE))
            if (types != null) {
                weatherTypes = daysLocal.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toList()
            }
            administered = cursor.getInt(cursor.getColumnIndex(ALARM_ADMINISTERED)) == 1

        }
    }

    override fun onCreate(sqlite: SQLiteDatabase?) {

        //SQL for creating table
        val CREATE_TABLE = ("CREATE TABLE " + tableName + " ("
                + ALARM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + ALARM_WEATHER_TYPE + " TEXT NOT NULL, "
                + ALARM_HOUR + " INTEGER, "
                + ALARM_MINUTE + " INTEGER, "
                + ALARM_DAYS + " TEXT NOT NULL, "
                + ALARM_ADMINISTERED + " INTEGER DEFAULT 0);")

        sqlite?.execSQL(CREATE_TABLE);
    }

    override fun onUpgrade(sqlite: SQLiteDatabase?, p1: Int, p2: Int) {

        sqlite?.execSQL("DROP TABLE IF EXISTS " + tableName)
        onCreate(sqlite)

    }

    fun createAlarm(alarmData: AlarmData): Int {

        val contentValues = createContentValues(alarmData as AlarmData)
        val rowId = sqlObj.insert(tableName, null, contentValues)

        alarmData.id = rowId.toInt()

        return rowId.toInt()
    }

    fun fetchAlarms(): List<AlarmData> {

        val cursor = sqlObj.query(tableName, null,
                null, null, null, null, null)
        val alarms = ArrayList<AlarmData>(cursor.count)
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val alarmData = createAlarmFromCursor(cursor)
                alarms.add(alarmData)
                cursor.moveToNext()
            }
        }
        cursor.close()

        return alarms
    }

    fun updateAlarm(alarmData: AlarmData): Boolean {
        val selectionArs = arrayOf(alarmData.id.toString())
        val contentValues = createContentValues(alarmData)

        val i = sqlObj.update(tableName, contentValues, "id=?", selectionArs)
        return i > 0
    }

    fun removeAlarm(id: Int): Boolean {

        val selectionArs = arrayOf(id.toString())

        val i = sqlObj.delete(tableName, "id=?", selectionArs)
        return i > 0
    }
}
