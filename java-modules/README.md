# java 9 maven 多模块项目 demo

> 注意，运行demo前需要确认在IDEA中将 Project Structure - module -dependencies 中sdk 设置为jdk11

java9 给java平台带来了模块系统(JPMS)。在JPMS的介绍文档中，都是基于Java的项目结构, 例如:

```shell
.
└── src
    └── main
        └── java
            ├── info.victorchu.appmodule # 模块 app
            │   └── info
            │       └── victorchu
            │           └── appmodule # java package
            ├── info.victorchu.daomodule
            │   └── info
            │       └── victorchu
            │           └── daomodule
            ├── info.victorchu.entitymodule
            │   └── info
            │       └── victorchu
            │           └── entitymodule
            └── info.victorchu.userdaomodule
                └── info
                    └── victorchu
                        └── userdaomodule
```

但是，JPMS不是构建工具，缺乏自动管理项目依赖项的能力。而构建工具maven提供了module的概念(当然这和JPMS的模块概念并不相同)。这里尝试将 JPMS和Maven集成起来，将每个maven模块包装成一个java模块。

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

项目共有4个子模块:

- appmodule
- daomodule
- entitymodule
- userdaomodule

模块间的依赖关系通过模块的`pom.xml`和`module-info.java` 共同维护。

## 执行

```shell
# 执行前先构建 mvn clean package
$ java --module-path entitymodule/target/entitymodule-1.0-SNAPSHOT.jar:daomodule/target/daomodule-1.0-SNAPSHOT.jar:userdaomodule/target/userdaomodule-1.0-SNAPSHOT.jar:appmodule/target/appmodule-1.0-SNAPSHOT.jar  --module info.victorchu.demo.javamodule.appmodule/info.victorchu.demo.javamodule.appmodule.Application
```

输出:

```shell
User{name='Julie', id=1}
User{name='David', id=2}
```