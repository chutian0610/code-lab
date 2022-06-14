package info.victorchu.commontool.fluentjson.jackson;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

class JSONSTest {
    @Test
    void buildJson1(){
        JSONS jsons = new JSONS();
        ObjectNode jsonObject = jsons.object()
                .textNode("test","test")
                .nullNode("haha")
                .objectNode("ob",jsons.object().textNode("name","vic").build())
                .arrayNode("array",jsons.array().add("dsdsds").build())
                .build();

        System.out.println(jsonObject.toString());
    }
}