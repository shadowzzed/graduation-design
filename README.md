# graduation-design
2020年度毕设-基于主题搜索的知识管理系统设计与实现

# 基本流程
1. 从知网等网站上提取论文信息（包括但不限作者、日期、关键词）
2. 提取的信息进行分词，提取出新的关键词进行爬取
3. 从某个维度进行分析（比如在log4j上作者做了三个版本，主要优化在哪个方面）


# 技术选型
* 基础架构 springboot
* 数据持久化 mybatis
* 消息队列 rabbitmq
* 分词技术（待定） - 流程2
* 大数据在线或离线计算（待定） - 流程3


# 爬取CNKI时的坑
不可以直接通过解密cnki的url然后自己构造url进行爬取<br>CNKI的返回机制是根据cookie判断用户最后一次搜索结果,只要前缀符合规则那么无论如何构造URL都会返回相同的<br>因此我们需要构造一个cookie,同时让这个cookie去搜索关键词然后再爬取一定规则的url(如果只有cookie没有搜索过也不可以)<br>综上我们需要 模拟浏览器进行点击,cookie 两个东西<br>所以我选择了`htmlunit`可以在模拟点击同时保存cookie然后去访问url获得数据
