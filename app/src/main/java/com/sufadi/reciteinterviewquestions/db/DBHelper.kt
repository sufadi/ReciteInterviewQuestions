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
            Constans.TYPE_DESIGN_PATTERN -> Constans.DB_TABLE_NAME_DESIGN_PATTERN
            Constans.TYPE_PERFORMANCE -> Constans.DB_TABLE_NAME_PERFORMANCE
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
     *
     * 【土方法，不建议】获取指定行数的数据，可以使用 limit + moveToLast
     *
     * 先获取一个范围，例如从0条开始，偏移1条，得到1条数据，并使用moveToLast获取最后1条数据，得到获取第1行数据目的
     * select * from java limit 0,1
     *
     * 先获取一个范围，例如从0条开始，偏移2条，得到2条数据，并使用moveToLast获取最后1条数据，得到获取第2行数据目的
     * select * from java limit 0,2
     */
    fun getTableItem(type: Int, index: Int): ReciteInfo? {
        var result: ReciteInfo? = null
        val tableName = getTableName(type)
        val cursor: Cursor? = dateBase?.rawQuery("select * from $tableName limit 0, $index", null)
        if (cursor!!.moveToLast()) {
            val id = cursor.getInt(cursor.getColumnIndex(Constans.DB_KEY_ID))
            val question = cursor.getString(cursor.getColumnIndex(Constans.DB_KEY_QUESTION))
            val answer = cursor.getString(cursor.getColumnIndex(Constans.DB_KEY_ANSWER))
            Log.d(Constans.TAG, "queryFormTable $tableName id=$id, question=$question, answer=$answer")
            result = ReciteInfo(id, question, answer)
        }
        cursor.close()
        return result
    }

    fun delTableItem(type: Int, index: Int) {
        val tableName = getTableName(type)
        dateBase?.execSQL("delete from $tableName WHERE ${Constans.DB_KEY_ID}=${index}")
    }

    fun updateAnswerTableItem(type: Int, index: Int, new: String) {
        val tableName = getTableName(type)
        dateBase?.execSQL("update $tableName set ${Constans.DB_KEY_ANSWER}  = '$new' WHERE ${Constans.DB_KEY_ID}=${index}")
    }

    fun updateQuestionTableItem(type: Int, index: Int, new: String) {
        val tableName = getTableName(type)
        dateBase?.execSQL("update $tableName set ${Constans.DB_KEY_QUESTION}  = '$new' WHERE ${Constans.DB_KEY_ID}=${index}")
    }
}
