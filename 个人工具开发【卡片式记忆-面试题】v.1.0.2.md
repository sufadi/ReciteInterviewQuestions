## 1. 个人工具开发【卡片式记忆面试题开发】v.1.0.2
目的：主要是供个人地铁或公交路上，做一些自己整理的面试题。
例如目前比较感兴趣的java、android、算法、系统、正则表达式相关的面试题。方便地铁利用手机记忆用途

## 2. 思路:卡片记忆法
## 2.1 交互
1. 分上下2页，上页显示题目，下页显示答案。
2. 点击上页切换到下一个题目，同时清空下页内容
3. 下页默认不显示内容，点击则显示该题目答案。

可能的拓展：
v1.0 暂时做成固定数据库
1. 做成可以自定义新增或删除的功能
2. 类似百词斩可以进行斩掉，下次重新背的时候再出现
3. 数据库变大时的性能优化
当然这个只是个人的一些想法，基本上是看下自己的需求程度，满足需求就行，不打算做得太复杂，万一哪天自己不想用了，岂不白白浪费时间。
毕竟工具制作只是个人爱好，要是有报酬的工具单子可以接，也是很快乐的事情。不过这种事情想想就好。

## 2.2 功能
1. 经典Java面试题
2. 经典Android面试题
3. 经典算法题
4. 经典Android系统题
5. 正则表达式

## 3. 本次开发情况
- v.1.0.0 先将简单的UI绘制处理(https://blog.csdn.net/su749520/article/details/122143883)
- v.1.0.1 申请SD卡读写权限，建立数据库，对功能进行简单填充（https://blog.csdn.net/su749520/article/details/122149684?spm=1001.2014.3001.5501）
- v.1.0.2 建立自定义路径的数据库，对功能进行简单填充（本文）
- v.1.0.2 读取数据库，调试数据库题库和UI（下文）

## 4. 数据库的结构设计
1. 建立数据库名称：ReciteData.db
2. 根据不同的背诵类别，新建不同的表名
    a. 表名:java
    b. 表名:android 
    c. 表名:android_system
    d. 表名:recite_algorithm
    e. 表名:regular_expression

3. 每张表的数据库字段
    a. 自增的id(可作为题目的序号): _id
    b. 题目内容: question
    c. 题目答案: answer
    d. 题目出现次数（拓展功能）: show_count
    e. 题目是否已背(拓展功能，后续不要显示用途): recitedStatus

上述的前提是需要申请访问 SdCard 读写权限

## 5. 软件编码
### 5.1 建立自定义路径的数据库
目的主要是为了个人导出数据库，进行手动录入题库到数据库，方便apk随时读取新的题目
思路：重写 ContextWrapper 中的 getDatabasePath 的方法
```
package com.sufadi.reciteinterviewquestions.db

import android.content.Context
import android.content.ContextWrapper
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.util.Log
import com.sufadi.reciteinterviewquestions.utils.Constans
import java.io.File

/**
 * 自定义数据库路径
 * 重点重写 getDatabasePath 的方法
 */
class DataBaseContext(private val mContext: Context) : ContextWrapper(mContext) {

    /**重写数据库路径方法, 实现自定义数据库路径 */
    override fun getDatabasePath(name: String): File {
        val dirPath: String = Constans.DB_PTAH
        var path: String? = null
        val parentFile = File(dirPath)
        if (!parentFile.exists()) {
            parentFile.mkdirs()
        }
        val parentPath = parentFile.absolutePath
        path = if (parentPath.lastIndexOf("\\/") != -1) {
            dirPath + File.separator + name
        } else {
            dirPath + name
        }
        Log.d(Constans.TAG, "getDatabasePath: $path")
        return File(path)
    }

    override fun openOrCreateDatabase(
        name: String,
        mode: Int,
        factory: CursorFactory
    ): SQLiteDatabase {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name).absolutePath, factory)
    }

    override fun openOrCreateDatabase(
        name: String,
        mode: Int,
        factory: CursorFactory,
        errorHandler: DatabaseErrorHandler?
    ): SQLiteDatabase {
        return SQLiteDatabase.openOrCreateDatabase(
            getDatabasePath(name).absolutePath,
            factory,
            errorHandler
        )
    }

}
```
### 5.2 建立数据库
1. 建立数据库名称：ReciteData.db
2. 根据不同的背诵类别，新建不同的表名
    a. 表名:java
    b. 表名:android 
    c. 表名:android_system
    d. 表名:recite_algorithm
    e. 表名:regular_expression

3. 每张表的数据库字段
    a. 自增的id(可作为题目的序号): _id
    b. 题目内容: question
    c. 题目答案: answer
    d. 题目出现次数（拓展功能）: show_count
    e. 题目是否已背(拓展功能，后续不要显示用途): recitedStatus
```
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

    constructor(context: Context?) : this(context, DB_NAME, null, 1) {
        Log.d(Constans.TAG, "DBOpenHelper constructor")
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
    }


}
```
### 5.3 遇到的报错-Failed to open database '/storage/emulated/0/
之前以为只要新增动态运行运行权限即可，没想到还需要在AndroidManifest.xml新增 android:requestLegacyExternalStorage="true"
### 5.4 查看运行结果
#### a. 查看数据库是否生成
```
HWCLT:/storage/emulated/0 $ ls -al Rec
ReciteData.db          ReciteData.db-journal
```
#### b. 查看数据库是否可以插入数据
使用 insertDataToTableAlgorithm和queryFormTableAlgorithm 检测下，例如写入一组数据
```
package com.sufadi.reciteinterviewquestions.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
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
}
```
#### c. 数据库存储位置和数据库查询结果
重点：queryFormTableAlgorithm id=1, question=操作符？的含义, answer=前一个字符串0次或1次拓展，例如abc? 表示 ab, abc
```
2021-12-26 23:22:24.550 25543-25543/com.sufadi.reciteinterviewquestions D/shz_debug: onClick: 2131165253
2021-12-26 23:22:24.550 25543-25543/com.sufadi.reciteinterviewquestions D/shz_debug: gotoReciteUI: 0
2021-12-26 23:22:24.663 25543-25543/com.sufadi.reciteinterviewquestions D/shz_debug: DBOpenHelper constructor
2021-12-26 23:22:24.671 25543-25543/com.sufadi.reciteinterviewquestions D/shz_debug: getDatabasePath: /storage/emulated/0/ReciteData.db
2021-12-26 23:22:24.694 25543-25543/com.sufadi.reciteinterviewquestions D/shz_debug: DBOpenHelper onCreate
2021-12-26 23:22:24.698 25543-25543/com.sufadi.reciteinterviewquestions D/shz_debug: insertDataToTableAlgorithm sql=INSERT INTO recite_algorithm(question,answer) VALUES('操作符？的含义','前一个字符串0次或1次拓展，例如abc? 表示 ab, abc
    ');
2021-12-26 23:22:24.700 25543-25543/com.sufadi.reciteinterviewquestions D/shz_debug: queryFormTableAlgorithm id=1, question=操作符？的含义, answer=前一个字符串0次或1次拓展，例如abc? 表示 ab, abc
```


