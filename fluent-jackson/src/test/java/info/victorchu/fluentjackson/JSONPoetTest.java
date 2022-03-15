package info.victorchu.fluentjackson;

import org.junit.jupiter.api.Test;

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

}