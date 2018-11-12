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

================解决方案
使用监控项：system.cpu.util[,] 表示CPU使用率  如果用system.cpu.util[,idle] window下不支持
使用触发器：{Test Temp:system.cpu.util[,].count(#5,6,"gt")}=5 表示计算五次值大于六的次数为5次 就是连续五次探测连续超过设定的值.
设置出发器为一次还是多次,如果一次的话只提示一次,如果设置多次会一直报.

==============================================================================================================
2. 内存占有率    1分钟间隔      连续5次采样操过70%     SMS+Mail

可用内存：Available memory=free+buffers+cached，即2021=235+394+1392
已使用内存：Used memory=used-buffers-cached，即30217=32003-394-1392

而在用zabbix自身的Template OS Linux模版监控服务器时，发现Used memory都偏高。

这是因为zabbix通过vm.memory.size[used]这个key获取服务器的已使用内存（Used memory）。但vm.memory.size[used]获取的值（如下所示为used 32003）还包含buffers、cached这部份。buffers、cached这部份对服务器来说也是可用的。只不过linux本身是尽可能多地使用内存，只有当内存不足时才会释放buffers、cached空间。
vm.memory.size[available]获取的可用内存倒是挺准确的，也就是说zabbix获取的available是加上buffers和cached的，获取的used也是加上buffers和cached，因此我们这边就会修改Used memory的key值，让总内存减去可用内存即可获取准确的已使用内存。
Used memory的key：(last("vm.memory.size[total]")-last("vm.memory.size[available]"))

请注意使用计算表达式的时候原先涉及到的任何一个取值都要创建出来否则报不存在

说明：
计算类型的key定义主要是根据已定义过的key值来计算的。注意是已定义过的key值。
如这里我要创建一个计算linux服务器内存实际使用大小的监控项(计算方法为：vm.memory.size[total]-vm.memory.size[buffers]-vm.memory.size[cached]-vm.memory.size[free])。但zabbix默认的Items里并没有获取vm.memory.size[cached]这个key值。所以在查看Calculated类型的items时会出现Cannot evaluate function “last()”: item “coolnull:vm.memory.size[cached]” does not exist。要解决的话就需要自己再定义添加coolnull:vm.memory.size[cached]这个Items。
以下这边以获取算linux服务器内存实际使用大小来举例。

具体：
1、在模板中或是主机中选择监控项—->选择Create item

2、监控的名称和key按照其功能随便起一个名字(注：名字和key的名字一定要是英文格式的，包括你名字中包含的特殊字符)，如：mem.realused，key的类型选择计算，这时会出现一个Formula，里面就是你的计算公式，如：
(last(“vm.memory.size[total]”)-last(“vm.memory.size[buffers]”)-last(“vm.memory.size[cached]”)-last(“vm.memory.size[free]”))
这个计算公式就是取各个key值的最后一次值做计算，注意公式中没有空格(经测试发现有时有空格之类的话提交的时候可能报错)，计算的各个key都是同一类型的。

3、其他的选项没有什么特别，参照公式中其中一个key值的设置选项设置即可。

4、最后点保存提交，完成key的定义。
这边发现如果用(last(“vm.memory.size[total]”)-last(“vm.memory.size[buffers]”)-last(“vm.memory.size[cached]”)-last(“vm.memory.size[free]”))来减的话，还需要再定义vm.memory.size[buffers]、vm.memory.size[cached]值。因此我直接使用(last(“vm.memory.size[total]“)-last(“vm.memory.size[available]“))来计算更方便，不需要再定义buffers、cached。


最终的表达式  {Test Temp:vm.memory.size[usepercent].count(#5,80,"ge")}=5

附录：
常用的计算类型key定义时用的有以下几种公式(注意一下公式在写入Formula时要加一个小括号)：
1、计算空闲磁盘空间的比例：100*last(“vfs.fs.size[/,free]”)/last(“vfs.fs.size[/,total]”)
2、计算10分钟主机出流量的平均值：avg(“Zabbix Server:net.if.out[eth0,bytes]”,600)
3、计算网卡总流量：last(“net.if.in[eth0,bytes]”)+last(“net.if.out[eth0,bytes]”)
4、计算进流量占网卡总流量的比例：100*last(“net.if.in[eth0,bytes]”)/(last(“net.if.in[eth0,bytes]”)+last(“net.if.out[eth0,bytes]”))
5、在计算项目中正确使用聚合条目，注意双引号如何被转义:last(“grpsum[\”video\”,\”net.if.out[eth0,bytes]\”,\”last\”,\”0\”]”)/last(“grpsum[\”video\”,\”nginx_stat.sh[active]\”,\”last\”,\”0\”]”)
6、计算多台主机出流量的和：last(“192.168.1.100:net.if.out[eth0,bytes]”)+last(“192.168.1.200:net.if.out[eth0,bytes]”)+last(“192.168.1.110:net.if.out[eth0,bytes]”)
这种计算是多台主机的，至于这个值放在哪个主机的监控项中都是一样的，只要是在此zabbix服务器端中。

=======================================================================================================================
3. 磁盘空间      3分钟间隔      连续5次采样操过70%     SMS+Mail
vfs.fs.size[fs,<mode>]
mode,默认是total,剩下的就是free,used,pfree,pused,p这里应该就是百分的意思吧
windows下：vfs.fs.size[c:,<mode>]

=======================================================================================================================
4. Traffic      1分钟间隔      连续5次采样操过80%     SMS+Mail


=======================================================================================================================
5. NIC          1分钟间隔      如果有UP/DOWN          SMS+Mail

=======================================================================================================================
6. 服务+管理端口 1分钟间隔      连续三次不响应          SMS+Mail
zabbix监控端口使用如下key:
key：net.tcp.listen[port]
Checks if this port is in LISTEN state. 0 - it is not, 1 - it is inLISTEN state.

触发器
{Test Temp:net.tcp.listen[22].count(#3,0,eq)}=3
  
=======================================================================================================================
7. 系统进程      1分钟间隔      系统进程丢失            SMS+Mail
为主机添加对应的监控项，proc.num[GoodSync.exe]，因为我要监控某个进程，所以使用proc.num键值。
添加键值以后，需要建立一个触发器。触发器的表达式如下：
    proc.num[<name>,<user>,<state>,<cmdline>]
    name - 进程名称 (默认“all processes”)
    user - 用户名 (默认 “all users”)
    state - 可用值: all (默认), run,sleep, zomb
    cmdline - 命令行过滤(正则表达式)
    
    示例keys: proc.num[,mysql] – MySQL用户运行的进程数量
    proc.num[apache2,www-data] – www-data运行了多少个apache2进程
    proc.num[,oracle,sleep,oracleZABBIX]
    {wldatacenter:proc.num[GoodSync.exe].last(60)}<1

表达式解释：
wldatacenter    指明哪台机器
proc.num[GoodSync.exe]            指明哪个键值
last()  最近一次获取的值，等同于last(#1).
last: 参数可以是秒，也可以是次数，如果是秒，就忽略，last(5)  和last(100)  因为最近5秒和最近100秒的值都是一个值。
    如果是次数，就是：last(#1)  最近的第一个值   last(#10) 最近的第10个值。
    例子：最近的值为  1, 10, 20, 50. 8 ,3  ，1为最近的取值。
    last(#3)  取值就是20.
======================================================================================================================
8.系统日志       N/A           日志中发现关键词         SMS+Mail
最近开发人员有一个需求，监控java程序的报错日志，如日志中包含“ERROR”关键字的信息，就邮件告警，以下是具体实现方法。
log[file_pattern,<regexp>,<encoding>,<maxlines>,<mode>,<output>]
参数介绍：
file - 日志文件的全路径。
regexp - 过滤日志的正则表达式。
encoding - 字符编码，默认为英文单字节SBCS(Single-Byte Character Set)。
maxlines - agent每秒发送给server（或proxy）的数据的最大行数，这个参数会覆盖掉zabbix_agentd.conf配置文件里的'MaxLinesPerSecond'参数。
mode - 可填参数：all（默认），skip（跳过旧数据）。
output - 自定义格式化输出，默认输出regexp匹配的整行数据。转义字符'\0'表示regexp

键值示例：log[/app/wutongshu/monitorlog/error.log,ERROR,,,skip,]
表达式：{Template App Java logs:log[/app/wutongshu/monitorlog/error.log,ERROR,,,skip,].str(ERROR)}=1  and  {Template App Java logs:log[/app/wutongshu/monitorlog/error.log,ERROR,,,skip,].nodata(60)}=0

拆开解析：
{Template App Java logs:log[/app/wutongshu/monitorlog/error.log,ERROR,,,skip,].str(ERROR)}=1表示如果匹配到“ERROR”关键字，表达式为真。
{Template App Java logs:log[/app/wutongshu/monitorlog/error.log,ERROR,,,skip,].nodata(60)}=0表示60秒内有数据产生则表达式为真，即60秒内如果没有新数据了，则表达式为假。

and表示同时满足两个条件，触发器才会触发。

sample2：
{log_test:log[/var/log/secure,"Failed password"].str(Failed)}=1 and  {log_test:log[/var/log/secure,"Failed password"].nodata(60)}=0
# item 获取的值中出现Failed 就报警, 如果60s 无数据就恢复.

好了这个只是简单的模拟了log的用法..我们匹配了Failed 我们也可以配置 key( log[/var/log/secure,"(Accepted|Failed) password"])  trigger相应改下.
根据不同的需求可以做响应的改变.

=====================================================================================================================================
WEB接口监控
