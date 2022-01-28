package com.sufadi.reciteinterviewquestions

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.sufadi.reciteinterviewquestions.db.DBHelper
import com.sufadi.reciteinterviewquestions.utils.Constans
import kotlinx.android.synthetic.main.recite_interview_layout.*


class ReciteInterviewActivity : AppCompatActivity(), View.OnClickListener {

    private var type = Constans.TYPE_JAVA
    private lateinit var dbHelper: DBHelper
    private var size = 0
    private var curIndex = 1
    private var answerInfo: String? = null

    private val TYPE_QUESTION = 0
    private val TYPE_ANSWER = 1

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
            tv_question.setText("没有题目哦，当前数据库大小为:$size")
            tv_question.isEnabled = false
            tv_answer.isEnabled = false
        } else {
            updateUI()
        }
        Log.d(Constans.TAG, "getTableSize: $size")

        tv_answer.movementMethod = ScrollingMovementMethod.getInstance()
        tv_question.movementMethod = ScrollingMovementMethod.getInstance()
    }

    private fun initListeners() {
        tv_question.setOnClickListener(this)
        tv_answer.setOnClickListener(this)
        del_btn.setOnClickListener(this)
        edit_question.setOnClickListener(this)
        edit_answer.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.tv_question -> nextQuestion()
            R.id.tv_answer -> showAnswer()
            R.id.del_btn -> delQuestion()
            R.id.edit_question -> editQuestion()
            R.id.edit_answer -> editAnswer()
        }
    }

    private fun editAnswer() {
        createDialog(TYPE_ANSWER)
    }

    private fun editQuestion() {
        createDialog(TYPE_QUESTION)
    }

    private fun delQuestion() {
        Log.d(Constans.TAG, "delQuestion")
        val mReciteInfo = dbHelper.getTableItem(type, curIndex)
        mReciteInfo?.let {
            dbHelper.delTableItem(type, mReciteInfo.id)
            size = dbHelper.getTableSize(type)
            nextQuestion()
        }
    }

    private fun updateAnswer(new : String) {
        Log.d(Constans.TAG, "updateAnswer")
        dbHelper.updateAnswerTableItem(type, curIndex, new)
    }

    private fun updateQuestion(new : String) {
        Log.d(Constans.TAG, "updateQuestion")
        dbHelper.updateQuestionTableItem(type, curIndex, new)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menu?.add(Menu.NONE, Menu.FIRST, 0, "新增")
        return true
    }

    private fun createDialog(type: Int) {
        val dialog = AlertDialog.Builder(this)
        if (TYPE_ANSWER == type) {
            dialog.setTitle("修改答案")
        } else {
            dialog.setTitle("修改题目")
        }

        val editText = EditText(this)
        if (TYPE_ANSWER == type) {
            editText.setText(tv_answer.text)
        } else {
            val question = tv_question.text.split(')')[1]
            Log.d(Constans.TAG, "createDialog: $question")

            editText.setText(question)
        }

        dialog.setView(editText)

        dialog.setPositiveButton("确定") { dialog, _ ->
            val new = editText.text.toString()
            new?.let {
                if (TYPE_ANSWER == type) {
                    updateAnswer(new)
                    tv_answer.text = new
                } else {
                    updateQuestion(new)
                    tv_question.text = new
                }
            }

            dialog.dismiss()
        }

        dialog.setNegativeButton("取消") { dialog, _ ->
            dialog.dismiss()
        }

        dialog.show()
    }
}