package com.sufadi.reciteinterviewquestions.utils

import android.os.Environment
import java.io.File

object Constans {
    const val TAG = "shz_debug"
    const val TYPE = "type"
    const val TITLE = "title"
    const val TYPE_JAVA = 0
    const val TYPE_ANDROID = 1
    const val TYPE_ALGORITHM = 2
    const val TYPE_ANDROID_SYSTEM = 3
    const val TYPE_REGULAR_EXPRESSION = 4
    const val TYPE_REGULAR_SQLITE3 = 5

    const val DB_NAME = "ReciteData.db"
    // /storage/emulated/0/
    var DB_PTAH =
        Environment.getExternalStorageDirectory().absolutePath
            .toString() + File.separator

    const val DB_TABLE_NAME_JAVA = "java"
    const val DB_TABLE_NAME_ANDROID = "android"
    const val DB_TABLE_NAME_ANDROID_SYSTEM = "android_system"
    const val DB_TABLE_NAME_RECITE_ALGORITHM = "recite_algorithm"
    const val DB_TABLE_NAME_REGULAR_EXPRESSION = "regular_expression"
    const val DB_TABLE_NAME_SQLITE3 = "sqlite3"

    const val DB_KEY_ID= "id"
    const val DB_KEY_QUESTION = "question"
    const val DB_KEY_ANSWER = "answer"
    const val DB_KEY_SHOW_COUNT = "show_count"
    const val DB_KEY_RECITED_STATUS = "recitedStatus"
}