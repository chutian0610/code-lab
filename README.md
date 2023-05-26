# code-lab

存放一些代码片段和demo

## dependencies

### jdk version

- java 8
- java 11
- java 17

### maven 版本

由于使用了jdk11,maven版本是3.6.3(需要大于3.5.0)。

jdk versions path managed in etc/jdk.properties.

```properties
JAVA_8_HOME=/Users/didi/.sdkman/candidates/java/8.0.332-zulu
JAVA_11_HOME=/Users/didi/.sdkman/candidates/java/11.0.18-zulu
JAVA_17_HOME=/Users/didi/.sdkman/candidates/java/17.0.6-zulu
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
