# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目性质

这是一个个人 Java 代码实验室（code-lab），用于存放代码片段和 demo。每个子模块是独立可运行的示例项目，模块之间没有相互调用关系。

## 环境要求

- Java 21+（根 pom 与大部分 demo）
- `snippets/*` 使用 Java 1.8（由子模块 pom 自行声明 `<maven.compiler.source/target>`）
- 切换方式：直接进入子模块目录执行 `mvn package`，子模块自带版本覆盖根 pom，**无需**通过 `-Dmaven.compiler.source=1.8` 命令行参数切换
- Maven 3.6.3+
- 仅 macOS（`/.DS_Store` 已被 `.gitignore` 忽略，但 `.iml` 历史文件存在）

## 常用构建命令

```bash
# 根目录全量构建（所有模块）
mvn clean package

# 构建单个模块（推荐，加速反馈）
mvn clean package -pl <module-path> -am

# 跳过测试
mvn package -DskipTests

# 运行单个测试类
mvn test -pl <module-path> -Dtest=ClassName

# 运行单个测试方法
mvn test -pl <module-path> -Dtest=ClassName#methodName
```

## 模块结构

根 `pom.xml` 是 parent pom（packaging=pom），统一管理以下子模块：

### `codelab-bom/`
BOM（Bill of Materials），集中管理依赖版本。所有子模块通过 `dependencyManagement` 的 `scope=import` 引入。**新增公共依赖时优先在此处定义版本号**。

### `snippets/`
代码片段库，Java 1.8 编译目标，使用 lombok：

- `java-snippets/`：通用 Java 工具/特性示例，依赖 jackson、guava、antlr4、spring-core 等
- `tenant-snippets/`：平台租户场景代码（最近新增，`target/` 已有产物但 `src/` 暂空）

### `demos/`
独立 demo 项目，每个 demo 都有专属 `README.md`：

- `gameoflife/`：元胞自动机命令行程序，参数：`行数 列数 死亡概率 帧间隔ms`
- `guice-quickstart/`：Google Guice 依赖注入示例
- `java-modules/`：JPMS + Maven 多模块集成（4 个子模块：`app-module`、`dao-module`、`entity-module`、`user-dao-module`），需用 JDK 11
- `jol-quickstart/`：**需 `-javaagent` 启动**，JOL（Java Object Layout）观察对象布局，可验证指针压缩 `-XX:-UseCompressedOops`
- `java-instrumentation/`：**双模块 + 需 `-javaagent`**，JVM Instrumentation API demo；含 `app/`（被注入目标）+ `agent/`（premain/agentmain 字节码改写）
- `jdk-lab/`：JDK 各类 API 用法示例（SPI、Stream、Unicode、Type 等）
- `memory-storage/`：JVM 内存中的行存（`rowstorage`）与列存（`columnstorage`）实现，含 segment/buffer 子包

## 关键约定

### 编码风格（来自全局 `~/.claude/rules/personal.md`）
- **超过 20 行的逻辑块应适当抽象或聚合**
- 所有回答使用中文，关键逻辑必须添加中文注释

### 模块命名
- Java 包路径统一以 `info.victorchu.*` 开头
- groupId：`info.victorchu.codelab`（根/BOM）、`info.victorchu.java`（java-snippets）、`info.victorchu.tenant`（tenant-snippets）
- 版本统一：`1.0-SNAPSHOT`

### 构建插件锁定
父 pom 已锁定 `maven-compiler-plugin 3.13.0`、`maven-surefire-plugin 3.0.0-M5`、`spring-boot-maven-plugin 2.6.7`，子模块一般继承即可，无需重新声明版本。

### Lombok 配置
使用 lombok 的子模块需在 `maven-compiler-plugin` 的 `annotationProcessorPaths` 中显式声明 lombok（已在 `snippets/*` 中配置），否则注解处理器不会生效。

## 运行各 demo 的入口

具体运行命令参考各 demo 子目录下的 `README.md`，例如：

```bash
# gameoflife（元胞自动机）
mvn package -Dmaven.test.skip=true -pl demos/gameoflife
cd demos/gameoflife/target && \
  java -cp gameoflife-1.0-SNAPSHOT.jar info.victorchu.game.gameoflife.Starter 40 50 0.5 500

# jol（带 javaagent 的对象布局查看）
mvn clean package -DskipTests -pl demos/jol-quickstart
java -javaagent:"demos/jol-quickstart/target/jol-quickstart-1.0-SNAPSHOT.jar" \
     -cp demos/jol-quickstart/target/jol-quickstart-1.0-SNAPSHOT.jar \
     info.victorchu.demos.jol.quickstart.JolSimple

# java-instrumentation（premain 模式）
mvn package -DskipTests -pl demos/java-instrumentation -am
java -javaagent:"demos/java-instrumentation/agent/target/lib/agent-1.0-SNAPSHOT.jar" \
     -cp "demos/java-instrumentation/app/target/app-1.0-SNAPSHOT.jar:demos/java-instrumentation/app/target/lib" \
     info.victorchu.demos.javainst.Application
```

## 工作流程注意事项

- 本仓库是**多模块聚合**，修改公共配置（如 BOM、root pom）会影响所有子模块，提交前确认影响范围
- IDE 工程文件（`.iml`、`.idea/`）已忽略，无需提交
- 没有 `package` 阶段之外的额外规范，每个模块可独立演进