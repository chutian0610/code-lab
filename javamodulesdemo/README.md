# java 9 maven 多模块项目 demo

注意，在IDEA中将 Project Structure - module -dependencies 中sdk 设置为jdk11

## 项目结构

```shell
.
├── README.md
├── appmodule
│   ├── appmodule.iml
│   ├── pom.xml
│   └── src
│       └── main
│           └── java
│               ├── info
│               │   └── victorchu
│               │       └── demo
│               │           └── javamodule
│               │               └── appmodule
│               │                   └── Application.java
│               └── module-info.java
├── daomodule
│   ├── daomodule.iml
│   ├── pom.xml
│   └── src
│       └── main
│           └── java
│               ├── info
│               │   └── victorchu
│               │       └── demo
│               │           └── javamodule
│               │               └── daomodule
│               │                   └── Dao.java
│               └── module-info.java
├── entitymodule
│   ├── entitymodule.iml
│   ├── pom.xml
│   └── src
│       └── main
│           └── java
│               ├── info
│               │   └── victorchu
│               │       └── demo
│               │           └── javamodule
│               │               └── entitymodule
│               │                   └── User.java
│               └── module-info.java
├── javamodulesdemo.iml
├── pom.xml
└── userdaomodule
    ├── pom.xml
    ├── src
    │   └── main
    │       └── java
    │           ├── info
    │           │   └── victorchu
    │           │       └── demo
    │           │           └── javamodule
    │           │               └── userdaomodule
    │           │                   └── UserDao.java
    │           └── module-info.java
    └── userdaomodule.iml
```

## 执行

```shell
# 执行前先构建 mvn clean package
$ cd javamodulesdemo
$ java --module-path entitymodule/target/entitymodule-1.0-SNAPSHOT.jar:daomodule/target/daomodule-1.0-SNAPSHOT.jar:userdaomodule/target/userdaomodule-1.0-SNAPSHOT.jar:appmodule/target/appmodule-1.0-SNAPSHOT.jar  --module info.victorchu.demo.javamodule.appmodule/info.victorchu.demo.javamodule.appmodule.Application
```
