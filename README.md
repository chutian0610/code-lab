# code-lab

## jdk version

default jdk version is java 1.8.

need jdk 11 configuration: 

```xml
<profiles>
    <profile>
        <id>compiler</id>
        <properties>
          <!-- need to reset-->
          <JAVA_11_HOME>~/.sdkman/candidates/java/11.0.11.hs-adpt</JAVA_11_HOME>
        </properties>
        <activation>
          <activeByDefault>true</activeByDefault>
        </activation>
      </profile>
    </profiles>
```

