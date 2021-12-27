## 1. 个人工具开发【卡片式记忆面试题开发】v.1.0.2
目的：主要是供个人地铁或公交路上，做一些自己整理的面试题。
例如目前比较感兴趣的java、android、算法、系统、正则表达式相关的面试题。方便地铁利用手机记忆用途

## 2. 思路:卡片记忆法
## 2.1 交互
1. 分上下2页，上页显示题目，下页显示答案。
2. 点击上页切换到下一个题目，同时清空下页内容
具体为点击上页就去查询下一个题目的数据库内容
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
- v.1.0.1 申请SD卡读写权限，新建数据库到Sdcard做准备（https://blog.csdn.net/su749520/article/details/122149684?spm=1001.2014.3001.5501）
- v.1.0.2 建立自定义路径的数据库，对功能进行简单填充（https://blog.csdn.net/su749520/article/details/122163189?spm=1001.2014.3001.5501）
- v.1.0.3 读取数据库，调试数据库题库和UI（本文）
- v.1.0.4 填充数据库的脚本，将题库建立起来

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
### 5.1 查询数据库中某张表的数量
主要使用sql语句：select count(*) from 表名
```
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
```
### 5.2 查询数据库中某张表的信息
主要使用sql语句：select * from $tableName WHERE ${Constans.DB_KEY_ID}=?
```
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
```

### 5.3 更新界面显示
```
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
```