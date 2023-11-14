# jol quickstart

## build

```sh
mvn clean package -DskipTests -pl demos/jol-quickstart
```

## run

```sh
java -javaagent:"target/jol-quickstart-1.0-SNAPSHOT.jar" -cp target/jol-quickstart-1.0-SNAPSHOT.jar info.victorchu.demos.jol.quickstart.JolSimple
```

关闭指针压缩

```sh
java -javaagent:"target/jol-quickstart-1.0-SNAPSHOT.jar" -XX:-UseCompressedOops -cp target/jol-quickstart-1.0-SNAPSHOT.jar info.victorchu.demos.jol.quickstart.JolSimple
```