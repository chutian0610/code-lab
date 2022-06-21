# code-lab

存放一些代码片段和demo

## dependencies

### jdk version

java 8 & java 11.

### maven 版本

由于使用了jdk11,maven版本是3.6.3(需要大于3.5.0)。

jdk versions path managed in module bom.

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <JAVA_11_HOME>/Users/didi/.sdkman/candidates/java/11.0.2-open</JAVA_11_HOME>
    <JAVA_8_HOME>/Users/didi/.sdkman/candidates/java/ora-1.8.0_311</JAVA_8_HOME>
  </properties>
```

编译和test插件配置

```xml
  <build>
    <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
      <plugins>
      <!--至少3.8.0版本的Maven编译器插件 -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.8.0</version>
        <configuration>
          <verbose>true</verbose>
          <fork>true</fork>
           <executable>${JAVA_?_HOME}/bin/javac</executable>
        </configuration>
      </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-surefire-plugin</artifactId>
          <version>3.0.0-M5</version>
          <configuration>
            <jvm>${JAVA_?_HOME}/bin/java</jvm>
          </configuration>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
```
