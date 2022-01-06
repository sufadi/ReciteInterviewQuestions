package com.sufadi.reciteinterviewquestions.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.sufadi.reciteinterviewquestions.domain.ReciteInfo
import com.sufadi.reciteinterviewquestions.utils.Constans

class DBHelper(val context: Context) {
    var mDBOpenHelper: DBOpenHelper? = null

    private val dBOpenHelper: DBOpenHelper
        private get() {
            if (mDBOpenHelper == null) {
                mDBOpenHelper = DBOpenHelper(DataBaseContext(context))
            }
            return mDBOpenHelper!!
        }

    /**获取数据库对象 */
    val dateBase: SQLiteDatabase?
        public get() = dBOpenHelper.instance

    /**关闭数据库 */
    fun closeDB() {
        val db = dateBase
        db?.close()
    }

    // just test
    fun insertDataToTableAlgorithm() {
        val sql="INSERT INTO ${Constans.DB_TABLE_NAME_RECITE_ALGORITHM}(${Constans.DB_KEY_QUESTION},${Constans.DB_KEY_ANSWER}) VALUES('操作符？的含义','前一个字符串0次或1次拓展，例如abc? 表示 ab, abc\n');";
        dateBase?.execSQL(sql)
        Log.d(Constans.TAG, "insertDataToTableAlgorithm sql=${sql}")
    }

    // just test
    fun queryFormTableAlgorithm() {
        val cursor: Cursor? = dateBase?.rawQuery("select * from ${Constans.DB_TABLE_NAME_RECITE_ALGORITHM}", null) ?: null
        while (cursor!!.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndex(Constans.DB_KEY_ID))
            val question = cursor.getString(cursor.getColumnIndex(Constans.DB_KEY_QUESTION))
            val answer = cursor.getString(cursor.getColumnIndex(Constans.DB_KEY_ANSWER))
            Log.d(Constans.TAG, "queryFormTableAlgorithm id=$id, question=$question, answer=$answer")
        }
        cursor.close()
    }

    private fun getTableName(type: Int): String {
        return when(type) {
            Constans.TYPE_JAVA -> Constans.DB_TABLE_NAME_JAVA
            Constans.TYPE_ALGORITHM -> Constans.DB_TABLE_NAME_RECITE_ALGORITHM
            Constans.TYPE_ANDROID_SYSTEM -> Constans.DB_TABLE_NAME_ANDROID_SYSTEM
            Constans.TYPE_ANDROID -> Constans.DB_TABLE_NAME_ANDROID
            Constans.TYPE_REGULAR_EXPRESSION -> Constans.DB_TABLE_NAME_REGULAR_EXPRESSION
            Constans.TYPE_REGULAR_SQLITE3 -> Constans.DB_TABLE_NAME_SQLITE3
            Constans.TYPE_POWER_CONSUMPTION  -> Constans.DB_TABLE_NAME_POWER_CONSUMPTION
            else -> Constans.DB_TABLE_NAME_JAVA
        }
    }

    /**
     * count() 是个聚合函数 作用是求表的所有记录数
     * select * from 表名          这个是查询表的所有记录
     * select count(*) from 表名   这个是查询表的所有记录数 换句话说就是该表的总行数
     */
    fun getTableSize(type : Int): Int {
        val tableName = getTableName(type)
        val cursor: Cursor? = dateBase?.rawQuery("Select count(*) from $tableName;", null)
        if (cursor!!.moveToNext()) {
            return cursor.getInt(0)
        }
        cursor.close()
        return 0
    }

    /**
     * Android sqlite 采用execSQL和rawQuery方法完成数据的添删改查操作
     * https://blog.csdn.net/qq156036678/article/details/84483839
     */
    fun getTableItem(type: Int, index: Int): ReciteInfo? {
        var result: ReciteInfo? = null
        val tableName = getTableName(type)
        val cursor: Cursor? = dateBase?.rawQuery("select * from $tableName WHERE ${Constans.DB_KEY_ID}=?", arrayOf(index.toString())) ?: null
        if (cursor!!.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(Constans.DB_KEY_ID))
            val question = cursor.getString(cursor.getColumnIndex(Constans.DB_KEY_QUESTION))
            val answer = cursor.getString(cursor.getColumnIndex(Constans.DB_KEY_ANSWER))
            Log.d(Constans.TAG, "queryFormTable $tableName id=$id, question=$question, answer=$answer")
            result = ReciteInfo(id, question, answer)
        }
        cursor.close()
        return result
    }
}
