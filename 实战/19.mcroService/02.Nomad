Nomad 是一个集群管理器和调度器，专为微服务和批量处理工作流设计。Nomad 是分布式，高可用，可扩展到跨数据中心和区域的数千个节点。
(这个工具还有调度框架的作用,还有个不错的国产调度框架ETL调度技术平台 TASKCTL)
Nomad 提供一个常规工作流跨基础设施部署应用。开发者使用一个声明式作业规范来定义应用该如何部署，资源有什么要求（CPU，内存，硬盘）。Nomad 接收这些作业，查找可用的资源来运行应用。调度算法确保所有的约束都满足，尽量在一个主机部署尽可能多的应用，优化资源利用。此外，Nomad 支持在所有主流操作系统运行虚拟化，容器化或者是独立的应用，灵活的支持广泛的工作流负载。
Nomad 已经在生产环境使用，主要特性：
Docker：Nomad 支持 Docker 作为第一类的工作负载类型
操作简单
多个数据中心和多个区域 
灵活的工作负载
可扩展

========================================================================================================================
Nomad安装
https://www.nomadproject.io/intro/getting-started/cluster.html
https://www.nomadproject.io/intro/getting-started/ui.html
https://www.nomadproject.io/guides/operations/cluster/manual.html

Clustering
We have started our first agent and run a job against it in development mode. This demonstrates the ease of use and the workflow of Nomad, but did not show how this could be extended to a scalable, production-grade configuration. In this step, we will create our first real cluster with multiple nodes.
========================Config Start===============================
» Starting the Server
The first step is to create the config file for the server. Either download the file from the repository, or paste this into a file called server.hcl:

# Increase log verbosity
log_level = "DEBUG"

# Setup data dir
data_dir = "/tmp/server1"

# Enable the server
server {
    enabled = true

    # Self-elect, should be 3 or 5 for production
    bootstrap_expect = 1
}
========================Config  End===============================
This is a fairly minimal server configuration file, but it is enough to start an agent in server only mode and have it elected as a leader. The major change that should be made for production is to run more than one server, and to change the corresponding bootstrap_expect value.
Once the file is created, start the agent in a new tab:

$ nomad agent -config server.hcl
==> WARNING: Bootstrap mode enabled! Potentially unsafe operation.
==> Starting Nomad agent...

We can see above that client mode is disabled, and that we are only running as the server. This means that this server will manage state and make scheduling decisions but will not run any tasks. Now we need some agents to run tasks!

» Starting the Clients
Similar to the server, we must first configure the clients. Either download the configuration for client1 and client2 from the repository here, or paste the following into client1.hcl:
========================Config Start===============================
# Increase log verbosity
log_level = "DEBUG"

# Setup data dir
data_dir = "/tmp/client1"

# Give the agent a unique name. Defaults to hostname
name = "client1"

# Enable the client
client {
    enabled = true

    # For demo assume we are talking to server1. For production,
    # this should be like "nomad.service.consul:4647" and a system
    # like Consul used for service discovery.
    servers = ["127.0.0.1:4647"]
}

# Modify our port to avoid a collision with server1
ports {
    http = 5656
}
========================Config End===============================

Copy that file to client2.hcl. Change the data_dir to be /tmp/client2, the name to client2, and the http port to 5657. Once you have created both client1.hcl and client2.hcl, open a tab for each and start the agents:
$ sudo nomad agent -config client1.hcl
==> Starting Nomad agent...
==> Nomad agent configuration:


$ nomad node status
ID        DC   Name     Class   Drain  Eligibility  Status
fca62612  dc1  client1  <none>  false  eligible     ready
c887deef  dc1  client2  <none>  false  eligible     ready
We now have a simple three node cluster running. The only difference between a demo and full production cluster is that we are running a single server instead of three or five.


Visit WebUI http://192.168.56.102:4646


=========================Submit a Job=============================

Usage
nomad job run [options] <job file>






