# ASimpleFTP
Simple ftp server and client implement by java for the purpose of learning.

用 java 实现的简单的FTP服务器与客户端



## 了解一下

**目前只实现Server端，Client端尚未开坑。**

### 支持的系统

- [x] Windows10
- [x] Centos7 
- [ ] Ubuntu （待测试）

### 已实现的功能

![FTP服务器功能及指令](README.assets/FTP服务器功能及指令.bmp)

### 已实现的命令

``` shell
ABOR,CWD,DELE,DIR,EPRT,EPSV,HELP,MKD,NLIST,OPTS,PASS,PASV,PORT,PWD,QUIT,RETR,RMD,SIZE,STOR,SYST,TYPE,USER
```



### ToDoList

- [ ]  SSL加密传输
- [ ] 注释补全
- [ ] Remote Help信息补全
- [ ] 更多的服务端指令实现
- [ ] 客户端
- [ ] ...