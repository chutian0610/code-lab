package info.victorchu.commontool.json.jackson;

import com.fasterxml.jackson.databind.node.ObjectNode;
import info.victorchu.commontool.json.jackson.fluent.JsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonBuilderTest {
    @Test
    void buildJson1(){
        JsonBuilder jsonBuilder = new JsonBuilder();
        ObjectNode jsonObject = jsonBuilder.object()
                .textNode("test","test")
                .nullNode("haha")
                .objectNode("ob", jsonBuilder.object().textNode("name","vic").build())
                .arrayNode("array", jsonBuilder.array().add("dsdsds").build())
                .build();
        Assertions.assertEquals(jsonObject.toString(),"{\"test\":\"test\",\"haha\":null,\"ob\":{\"name\":\"vic\"},\"array\":[\"dsdsds\"]}");
    }
}