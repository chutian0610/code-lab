# Java Socket

## simpleClient

简单的 Socket client

- 支持读取 cli 输入
- 支持发送 cli 输入
- 打印 server return msg

## io

### HealthCheckServer

带心跳的 Client

- 支持读取 cli 输入
- 支持发送 cli 输入
- 打印 server return msg
- 支持心跳包发送

### SimpleServer

- 支持 echo 功能，回显 client 输入
- bye 命令关闭连接
- 支持多线程

## nio

- nio 方式的 client & server
- nio server 支持 simpleClient

## aio

- aio 方式的 client & server
- aio server 支持 simpleClient