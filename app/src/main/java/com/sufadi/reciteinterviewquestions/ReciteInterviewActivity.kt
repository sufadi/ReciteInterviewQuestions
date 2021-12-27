package com.sufadi.reciteinterviewquestions

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.sufadi.reciteinterviewquestions.db.DBHelper
import com.sufadi.reciteinterviewquestions.utils.Constans
import kotlinx.android.synthetic.main.recite_interview_layout.*

class ReciteInterviewActivity : AppCompatActivity(), View.OnClickListener {

    private var type = Constans.TYPE_JAVA
    private lateinit var dbHelper: DBHelper
    private var size = 0
    private var curIndex = 1
    private var answerInfo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recite_interview_layout)
        initTitle()
        initValue()
        initListeners()
    }

    private fun initTitle() {
        val title = intent.getStringExtra(Constans.TITLE)
        setTitle(title)
    }

    private fun initValue() {
        dbHelper = DBHelper(this)
        type = intent.getIntExtra(Constans.TYPE, Constans.TYPE_JAVA)
        size = dbHelper.getTableSize(type)
        path_info.text = "数据库存放的路径：${Constans.DB_PTAH}${Constans.DB_NAME}"

        if (size == 0) {
            tv_question.text = "没有题目哦，当前数据库大小为:$size"
            tv_question.isEnabled = false
            tv_answer.isEnabled = false
        } else {
            updateUI()
        }
        Log.d(Constans.TAG, "getTableSize: $size")
    }

    private fun initListeners() {
        tv_question.setOnClickListener(this)
        tv_answer.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.tv_question -> nextQuestion()
            R.id.tv_answer -> showAnswer()
        }
    }

    private fun updateUI() {
        if (size <= 0) {
            return
        }
        val mReciteInfo = dbHelper.getTableItem(type, curIndex)
        mReciteInfo?.let {
            tv_question.text = "($curIndex/$size)" + it.question
            answerInfo = it.answer
            tv_answer.text = "点击本区域可显示答案"
        }
    }

    private fun nextQuestion() {
        if (curIndex < size) {
            curIndex ++
        } else {
            curIndex = 0
        }
        updateUI()
    }

    private fun showAnswer() {
        tv_answer.text = answerInfo
    }

}