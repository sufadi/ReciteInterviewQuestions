## 1. 个人工具开发【卡片式记忆面试题开发】v.1.0.1
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
- v.1.0.1 申请SD卡读写权限，建立数据库，对功能进行简单填充（本文）
- v.1.0.2 建立数据库，对功能进行简单填充（下文）

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
    e. 题目是否已背(拓展功能，后续不要显示用途): isRecited

上述的前提是需要申请访问 SdCard 读写权限

## 5. 软件编码
### 5.1 申请 SdCard 的动态运行时权限
#### a. AndroidManifest.xml
```
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```
#### b. 申请权限
```
    private val REQUEST_EXTERNAL_STORAGE_OK = 200
    private val PERMISSIONS_STORAGE_LIST = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

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
```
#### c. sdcard权限申请结果
![在这里插入图片描述](https://img-blog.csdnimg.cn/89eb5f7065ea4766a5fdcbd9a7fd1fcc.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rOV6L-q,size_19,color_FFFFFF,t_70,g_se,x_16)

![在这里插入图片描述](https://img-blog.csdnimg.cn/2f00ba5d5242470a9b0941dc767cad4d.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rOV6L-q,size_19,color_FFFFFF,t_70,g_se,x_16)

上述即可后续的Sdcard 的创建数据库和读写了