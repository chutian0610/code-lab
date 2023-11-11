# java instrumentation

java instrumentation 示例程序由两个模块组成:

- 一个应用程序 app
- 一个jvm代理 agent

jvm 代理将展示如何通过 instrumentation api 修改app的字节码。

## premain 运行

```shell
# cd demos/java-instrumentation/app/target;
dir=$(pwd);
java -javaagent:"$dir/lib/agent-1.0-SNAPSHOT.jar" -cp $dir/app-1.0-SNAPSHOT.jar:$dir/lib info.victorchu.demos.javainst.Application
```

```log
[Agent] premain method set Instrumentation
[Agent] Transforming class info/victorchu/demos/javainst/Application
Enter method-> info/victorchu/demos/javainst/Application.main
Enter method-> info/victorchu/demos/javainst/Application.count
Enter method-> info/victorchu/demos/javainst/Application.count
Enter method-> info/victorchu/demos/javainst/Application.count
Enter method-> info/victorchu/demos/javainst/Application.count
```

## agentmain 运行

启动app

```shell
# cd demos/java-instrumentation/app/target;
dir=$(pwd);
java -cp $dir/app-1.0-SNAPSHOT.jar:$dir/lib info.victorchu.demos.javainst.Application
```

```shell
cd demos/java-instrumentation/app/target;
dir=$(pwd);
java -cp $dir/app-1.0-SNAPSHOT.jar:$dir/lib info.victorchu.demos.javainst.Application $pid $dir/lib/agent-1.0-SNAPSHOT.jar
```

```
count 10 times
pid=1421
count 10 times
pid=1421
count 10 times
pid=1421
[Agent] In agentmain method
[Agent] Transforming class info/victorchu/demos/javainst/Application
Enter method-> info/victorchu/demos/javainst/Application.count
Enter method-> info/victorchu/demos/javainst/Application.count
Enter method-> info/victorchu/demos/javainst/Application.count
Enter method-> info/victorchu/demos/javainst/Application.count
```