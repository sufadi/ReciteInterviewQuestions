# 1. 软件名称：卡片式记忆面试题
# 2. 软件方案:
思路:卡片记忆法
交互:
1. 分上下2页，上页显示题目，下页显示答案。

2. 点击上页切换到下一个题目，同时清空下页内容

3. 下页默认不显示内容，点击则显示该题目答案。


暂时可以进行如下分类:

1. 经典Java面试题

2. 经典Android面试题

3. 经典算法题

4. 经典Android系统题

5. 正则表达式

# 3. Git 提交
## 3.1 
下载：git clone https://github.com/sufadi/ReciteInterviewQuestions.git

## 3.2 首次提交工程
echo "# ReciteInterviewQuestions" >> README.md
git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/sufadi/ReciteInterviewQuestions.git
git push -u origin main

## 3.3 提交代码
git add
git commit -m "xxx"
git push -u origin main