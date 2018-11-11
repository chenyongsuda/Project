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
