## 1. 个人工具开发【卡片式记忆-面试题】v.1.0.0
目的：主要是供个人地铁或公交路上，做一些自己整理的面试题。例如目前比较感兴趣的java\android相关的面试题。方便地铁利用手机记忆用途

## 2. 思路:卡片记忆法
## 2.1 交互
1. 分上下2页，上页显示题目，下页显示答案。
2. 点击上页切换到下一个题目，同时清空下页内容
3. 下页默认不显示内容，点击则显示该题目答案。

## 2.2 功能
1. 经典Java面试题
2. 经典Android面试题
3. 经典算法题
4. 经典Android系统题
5. 正则表达式

## 3. 本次开发情况
- v.1.0.0 先将简单的UI绘制处理(本文)
- v.1.0.1 建立数据库，对功能进行简单填充（下一文）
## 4. 代码开发
### 4.1 主界面的开发
#### 4.1.1 布局
暂时先画出5个按钮，后续根据是否有必要拓展，才修改为RecycleView
'''
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_margin="24dp"
    tools:context=".MainActivity"
    android:orientation="vertical">

    <!--后续根据是否有必要拓展，才修改为RecycleView-->
    <Button
        android:id="@+id/btn_recite_java"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="1. 背诵Java题"/>

    <Button
        android:id="@+id/btn_recite_android"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="2. 背诵Android题"/>

    <Button
        android:id="@+id/btn_recite_algorithm"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="3. 背诵算法题"/>

    <Button
        android:id="@+id/btn_recite_android_system"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="4. 背诵Android系统题"/>

    <Button
        android:id="@+id/btn_recite_regular_expression"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="5. 背诵正则表达式"/>

</LinearLayout>
'''
#### 4.1.2 主界面的按钮点击事件
主要是点击跳转事件。没啥好说的
'''
package com.sufadi.reciteinterviewquestions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import com.sufadi.reciteinterviewquestions.utils.Constans
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 背诵面试题-主界面
 */
class MainActivity : AppCompatActivity() , OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListeners()
    }

    private fun initListeners() {
        btn_recite_java.setOnClickListener(this)
        btn_recite_android.setOnClickListener(this)
        btn_recite_algorithm.setOnClickListener(this)
        btn_recite_android_system.setOnClickListener(this)
        btn_recite_regular_expression.setOnClickListener(this)
    }

    /**
     * 按钮的数据对于与数据库的内容
     * 建立数据库名称：ReciteData.db
     * 根据不同的背诵类别，新建不同的表名，每张表的只有2个字段题目和答案
     */
    override fun onClick(v: View?) {
        Log.d(Constans.TAG, "onClick: ${v?.id}")
        val buttonView = v as Button
        when(buttonView?.id) {
            R.id.btn_recite_java -> gotoReciteUI(Constans.TYPE_JAVA, buttonView.text as String)
            R.id.btn_recite_android -> gotoReciteUI(Constans.TYPE_ANDROID, buttonView.text as String)
            R.id.btn_recite_android_system -> gotoReciteUI(Constans.TYPE_ANDROID_SYSTEM, buttonView.text as String)
            R.id.btn_recite_algorithm -> gotoReciteUI(Constans.TYPE_ALGORITHM, buttonView.text as String)
            R.id.btn_recite_regular_expression -> gotoReciteUI(Constans.TYPE_REGULAR_EXPRESSION, buttonView.text as String)
        }
    }

    private fun gotoReciteUI(type: Int, title: String) {
        Log.d(Constans.TAG, "gotoReciteUI: $type")
        val intent = Intent(this, ReciteInterviewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(Constans.TYPE, type)
        intent.putExtra(Constans.TITLE,title)
        startActivity(intent)
    }
}
'''
### 4.2 背诵界面的开发
#### 4.2.1 布局
上下均分一半的2个TextView文本，上面显示问题，下面显示答案用途
'''
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <TextView
        android:id="@+id/tv_question"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toTopOf="@+id/tv_answer"/>

    <TextView
        android:id="@+id/tv_answer"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:background="@android:color/holo_green_dark"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/tv_question"
        app:layout_constraintBottom_toBottomOf="parent"/>

</androidx.constraintlayout.widget.ConstraintLayout>
'''
#### 4.2.2 主界面功能
暂时只是改变标题，后续根据类型，读取数据库中的表
'''
package com.sufadi.reciteinterviewquestions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sufadi.reciteinterviewquestions.utils.Constans

class ReciteInterviewActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recite_interview_layout)
        val type = intent.getIntExtra(Constans.TYPE, Constans.TYPE_JAVA)
        val title = intent.getStringExtra(Constans.TITLE)
        setTitle(title)
    }
}
'''
