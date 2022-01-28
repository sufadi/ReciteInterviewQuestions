package com.sufadi.reciteinterviewquestions

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.sufadi.reciteinterviewquestions.utils.Constans
import kotlinx.android.synthetic.main.activity_main.*


/**
 * 背诵面试题-主界面
 */
class MainActivity : AppCompatActivity() , OnClickListener {

    private val REQUEST_EXTERNAL_STORAGE_OK = 200
    private val PERMISSIONS_STORAGE_LIST = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkSdCardPermissions(this)
        initListeners()
    }

    private fun initListeners() {
        btn_recite_java.setOnClickListener(this)
        btn_recite_android.setOnClickListener(this)
        btn_recite_algorithm.setOnClickListener(this)
        btn_recite_android_system.setOnClickListener(this)
        btn_recite_regular_expression.setOnClickListener(this)
        btn_recite_sqlite3.setOnClickListener(this)
        btn_recite_power_consumption.setOnClickListener(this)
        btn_recite_performance.setOnClickListener(this)
        btn_recite_design_pattern.setOnClickListener(this)
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
            R.id.btn_recite_sqlite3 -> gotoReciteUI(Constans.TYPE_REGULAR_SQLITE3, buttonView.text as String)
            R.id.btn_recite_power_consumption -> gotoReciteUI(Constans.TYPE_POWER_CONSUMPTION, buttonView.text as String)
            R.id.btn_recite_performance -> gotoReciteUI(Constans.TYPE_PERFORMANCE, buttonView.text as String)
            R.id.btn_recite_design_pattern -> gotoReciteUI(Constans.TYPE_DESIGN_PATTERN, buttonView.text as String)
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

    private fun checkSdCardPermissions(activity: Activity?) {
        try {
            //检测是否有写的权限
            val permission = ActivityCompat.checkSelfPermission(
                activity!!,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE_LIST,
                    REQUEST_EXTERNAL_STORAGE_OK
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_EXTERNAL_STORAGE_OK ) {
            var allGranted = true
            for (grant in grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false
                }
            }
            if (allGranted) {
                Log.d(Constans.TAG, "Granted sdcard permission success")
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

}