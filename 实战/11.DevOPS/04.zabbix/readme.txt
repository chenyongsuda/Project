不错的Zabbix应用教程
http://www.zsythink.net/archives/551


监控基本需求
1. CPU占有率     1分钟间隔      连续5次采样操过80%     SMS+Mail
2. 内存占有率    1分钟间隔      连续5次采样操过70%     SMS+Mail
3. 磁盘空间      3分钟间隔      连续5次采样操过70%     SMS+Mail
4. Traffic      1分钟间隔      连续5次采样操过80%     SMS+Mail
5. NIC          1分钟间隔      如果有UP/DOWN          SMS+Mail
6. 服务+管理端口 1分钟间隔      连续三次不响应          SMS+Mail
7. 系统进程      1分钟间隔      系统进程丢失            SMS+Mail
8.系统日志       N/A           日志中发现关键词         SMS+Mail



zabbix有时候有问题的时候安装
yum install zabbix-get 

通过如下命令校验问题
zabbix_get -s 192.168.1.109 -p 10050 -k "system.cpu.load[all,avg1]"

还可以看server的日志和客户端的日志



========================================================================
1. CPU占有率     1分钟间隔      连续5次采样操过80%     SMS+Mail

system.cpu.util[<cpu>,<type>,<mode>]
CPU利用率	百分比	cpu - cpu数量 (默认是所有cpu) type - 可用值: idle, nice, user (默认), system (windows系统默认值）, iowait, interrupt, softirq,steal mode - 可用值: avg1 (一分钟平均，默认值), avg5(5分钟平均, avg15 (15分钟平均值)	范例key: system.cpu.util[0,user,avg5] 
老命名方式: system.cpu.idleX, system.cpu.niceX, system.cpu.systemX, system.cpu.userX

具体示例和格式就是这样的：system.cpu.util[0,user,avg5]
默认的参数是这样的 system.cpu.util[<cpu>,<type>,<mode>]
cpu具体编号就是CPU的具体核心，为空就代笔CPU所以核心
type就是CPU的不同状态值
idle, nice, user (default), system (default for Windows), iowait, interrupt, softirq, steal
其中idle表示空闲，user表示用户使用
最后的avg5表示平均每5分钟的值，为空表示当前值，要改成平均每15分钟只需要写成avg15
当前CPU使用率写成system.cpu.util[,user]即可，同理system.cpu.util[,idle]


zabbix监控中小编用的最多的是count的这函数，确认多次以减少了很多误告警，提高了运维效率。
可以设置连续几次都异常才发出告警，这样一来，只要发出告警基本上就已经确定发生故障了。

count
参数：秒或#num
支持类型：float,int,str,text,log
作用：返回指定时间间隔内数值的统计，
举例：
count(600)最近10分钟得到值的个数
count(600,12)最近10分钟得到值的个数等于12
count(600,12,"gt")最近10分钟得到值大于12的个数
count(#10,12,"gt")最近10个值中，值大于12的个数
count(600,12,"gt",86400)24小时之前的10分钟内值大于12的个数
count(600,,,86400)24小时之前的10分钟数据值的个数
第一个参数：指定时间段
第二个参数：样本数据
第三个参数：操作参数
第四个参数：漂移参数

支持的操作类型
eq: 相等
ne: 不相等
gt: 大于
ge: 大于等于
lt: 小于
le: 小于等于
like: 内容匹配
日常使用实例
上行流量最近两次都大于50M告警
{zabbix:net.if.out[em1].count(#2,50M,"gt")}=2
最近30分钟zabbix这个主机超过5次不可到达。
{zabbix:icmpping.count(30m,0)}>5

作者：_简_述_
链接：https://www.jianshu.com/p/0c800a095e6c
來源：简书
简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
