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