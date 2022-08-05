package info.victorchu.commontool.json.jackson;

import info.victorchu.commontool.json.jackson.fluent.JSONPoet;
import org.junit.jupiter.api.Assertions;
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
        Assertions.assertEquals(str,"[ \"dsdsd\", \"aweds\" ]");
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
        Assertions.assertEquals(str,"{\n" +
                "  \"id\" : \"dsdsd\",\n" +
                "  \"name\" : \"aweds\"\n" +
                "}");
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

        Assertions.assertEquals(str,"[ {\n" +
                "  \"id\" : \"aweds\"\n" +
                "}, {\n" +
                "  \"id\" : \"aweds\"\n" +
                "}, {\n" +
                "  \"id\" : \"aweds\"\n" +
                "}, {\n" +
                "  \"id\" : \"aweds\"\n" +
                "} ]");
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
        Assertions.assertEquals(str,"{\n" +
                "  \"array\" : [ \"aweds\" ]\n" +
                "}");
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
        Assertions.assertEquals(str,"{\n" +
                "  \"array\" : [ \"aweds\" ],\n" +
                "  \"dsdsddsdsd\" : [ \"dsdad\" ]\n" +
                "}");
    }

}