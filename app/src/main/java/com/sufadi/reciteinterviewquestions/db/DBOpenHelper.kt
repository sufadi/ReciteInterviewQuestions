package com.sufadi.reciteinterviewquestions.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.sufadi.reciteinterviewquestions.utils.Constans

/**
 * 方便后续填充题目到数据库中，更新数据库，即可以更新后续的背诵内容了
 * 1. 建立数据库名称：ReciteData.db
 * 2. 根据不同的背诵类别，新建不同的表名
 *  a. 表名:java
 *  b. 表名:android
 *  c. 表名:android_system
 *  d. 表名:recite_algorithm
 *  e. 表名:regular_expression

 * 3. 每张表的数据库字段
 *  a. 自增的id(可作为题目的序号): _id
 *  b. 题目内容: question
 *  c. 题目答案: answer
 *  d. 题目出现次数（拓展功能）: show_count
 *  e. 题目是否已背(拓展功能，后续不要显示用途): recitedStatus 0:未背， 1：已背
 */
class DBOpenHelper(context: Context?, dbName: String?, factory: CursorFactory?, version: Int) : SQLiteOpenHelper(context, dbName, factory, version) {

    constructor(context: Context?) : this(context, DB_NAME, null, 2) {
        mDataBaseContext = context as DataBaseContext
    }

    companion object {
        private const val DB_NAME = Constans.DB_NAME //数据库文件名
        private var INSTANCE: SQLiteDatabase? = null
    }

    private var mDataBaseContext: DataBaseContext? = null
    val instance: SQLiteDatabase?
        get() {
            if (INSTANCE == null) {
                INSTANCE = DBOpenHelper(mDataBaseContext).writableDatabase
            }
            return INSTANCE
        }



    /**
     * 建立数据库的表格
     */
    override fun onCreate(db: SQLiteDatabase) {
        Log.d(Constans.TAG, "DBOpenHelper onCreate")
        val createTableJava =
            """CREATE TABLE IF NOT EXISTS ${Constans.DB_TABLE_NAME_JAVA}(
                                ${Constans.DB_KEY_ID} integer NOT NULL PRIMARY KEY AUTOINCREMENT,
                                ${Constans.DB_KEY_QUESTION} text,
                                ${Constans.DB_KEY_ANSWER} text,
                                ${Constans.DB_KEY_SHOW_COUNT} integer,
                                ${Constans.DB_KEY_RECITED_STATUS} integer);"""
        db.execSQL(createTableJava)

        val createTableAndroid =
            """CREATE TABLE IF NOT EXISTS ${Constans.DB_TABLE_NAME_ANDROID}(
                                ${Constans.DB_KEY_ID} integer NOT NULL PRIMARY KEY AUTOINCREMENT,
                                ${Constans.DB_KEY_QUESTION} text,
                                ${Constans.DB_KEY_ANSWER} text,
                                ${Constans.DB_KEY_SHOW_COUNT} integer,
                                ${Constans.DB_KEY_RECITED_STATUS} integer);"""
        db.execSQL(createTableAndroid)

        val createTableAndroidSystem =
            """CREATE TABLE IF NOT EXISTS ${Constans.DB_TABLE_NAME_ANDROID_SYSTEM}(
                                ${Constans.DB_KEY_ID} integer NOT NULL PRIMARY KEY AUTOINCREMENT,
                                ${Constans.DB_KEY_QUESTION} text,
                                ${Constans.DB_KEY_ANSWER} text,
                                ${Constans.DB_KEY_SHOW_COUNT} integer,
                                ${Constans.DB_KEY_RECITED_STATUS} integer);"""
        db.execSQL(createTableAndroidSystem)

        val createTableAlgorithm =
            """CREATE TABLE IF NOT EXISTS ${Constans.DB_TABLE_NAME_RECITE_ALGORITHM}(
                                ${Constans.DB_KEY_ID} integer NOT NULL PRIMARY KEY AUTOINCREMENT,
                                ${Constans.DB_KEY_QUESTION} text,
                                ${Constans.DB_KEY_ANSWER} text,
                                ${Constans.DB_KEY_SHOW_COUNT} integer,
                                ${Constans.DB_KEY_RECITED_STATUS} integer);"""
        db.execSQL(createTableAlgorithm)

        val createTableRegularExpression =
            """CREATE TABLE IF NOT EXISTS ${Constans.DB_TABLE_NAME_REGULAR_EXPRESSION}(
                                ${Constans.DB_KEY_ID} integer NOT NULL PRIMARY KEY AUTOINCREMENT,
                                ${Constans.DB_KEY_QUESTION} text,
                                ${Constans.DB_KEY_ANSWER} text,
                                ${Constans.DB_KEY_SHOW_COUNT} integer,
                                ${Constans.DB_KEY_RECITED_STATUS} integer);"""
        db.execSQL(createTableRegularExpression)
    }

    //当数据库的版本发生变化的时候会自动执行，禁止人为调用
    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        when(newVersion) {
            2 -> {
                val createTableRegularSqlite3 =
                    """CREATE TABLE IF NOT EXISTS ${Constans.DB_TABLE_NAME_SQLITE3}(
                                ${Constans.DB_KEY_ID} integer NOT NULL PRIMARY KEY AUTOINCREMENT,
                                ${Constans.DB_KEY_QUESTION} text,
                                ${Constans.DB_KEY_ANSWER} text,
                                ${Constans.DB_KEY_SHOW_COUNT} integer,
                                ${Constans.DB_KEY_RECITED_STATUS} integer);"""
                db.execSQL(createTableRegularSqlite3)
            }
        }
    }


}