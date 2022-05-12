package info.victorchu.fluentjson.jackson;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class JSONPoetTest {
    @Test
    void buildJson1(){
        String str = JSONPoet.builder()
                .build()
                .beginArray()
                .textNode("dsdsd")
                .textNode("aweds")
                .endArray()
                .buildToString(true);
        System.out.println(str);
    }

    @Test
    void buildJson2(){
        String str = JSONPoet.builder()
                .build()
                .beginObject()
                .name("id")
                .textNode("dsdsd")
                .name("name")
                .textNode("aweds")
                .endObject()
                .buildToString(true);
        System.out.println(str);
    }

    @Test
    void buildJson3(){
        String str = JSONPoet.builder()
                .build()
                .beginArray()
                .beginObject()
                .name("id")
                .textNode("aweds")
                .endObject()
                .beginObject()
                .name("id")
                .textNode("aweds")
                .endObject()
                .beginObject()
                .name("id")
                .textNode("aweds")
                .endObject()
                .beginObject()
                .name("id")
                .textNode("aweds")
                .endObject()
                .endArray()
                .buildToString(true);

        System.out.println(str);
    }

    @Test
    void buildJson4(){
        String str = JSONPoet.builder()
                .build()
                .beginObject()
                    .name("array")
                    .beginArray()
                    .textNode("aweds")
                    .endArray()
                .endObject()
                .buildToString(true);

        System.out.println(str);
    }

    @Test
    void buildJson5(){
        List<String> list = new ArrayList<>();
        list.add("dsdad");
        String str = JSONPoet.builder()
                .build()
                .beginObject()
                    .name("array")
                    .beginArray()
                    .textNode("aweds")
                    .endArray()
                    .name("dsdsddsdsd")
                    .pojo(list)
                .endObject()
                .buildToString(true);
        System.out.println(str);
    }

}