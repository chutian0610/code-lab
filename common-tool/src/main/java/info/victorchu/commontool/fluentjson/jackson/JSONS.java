package info.victorchu.commontool.fluentjson.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.fasterxml.jackson.databind.util.RawValue;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * another json build tool for jackson.
 */
public class JSONS {
    protected final JsonUtil jsonUtil;

    public JSONS() {
        this.jsonUtil = new JsonUtil(new ObjectMapper());
    }

    public JSONS(ObjectMapper om) {
        this.jsonUtil = new JsonUtil(om);
    }

    /**
     * build json object
     * @return
     */
    public JsonObjects object() {
        return new JsonObjects(jsonUtil.getOm());
    }

    /**
     * build json array
     * @return
     */
    public JsonArrays array() {
        return new JsonArrays(jsonUtil.getOm());
    }

    public static class JsonObjects extends JSONS {
        protected ObjectNode currentObject;
        private JsonObjects(ObjectMapper om) {
            super(om);
            currentObject = jsonUtil.objectNode();
        }

        public JsonObjects booleanNode(String name, boolean v) {
            currentObject.put(name,v);
            return this;
        }

        public JsonObjects numberNode(String name, byte v) {
            currentObject.put(name,v);
            return this;
        }

        public JsonObjects numberNode(String name, Byte v) {
            currentObject.put(name,v);
            return this;
        }

        public JsonObjects numberNode(String name, short v) {
            currentObject.put(name,v);
            return this;
        }

        public JsonObjects numberNode(String name, Short v) {
            currentObject.put(name,v);
            return this;
        }

        public JsonObjects numberNode(String name, int v) {
            currentObject.put(name,v);
            return this;
        }

        public JsonObjects numberNode(String name, Integer v) {
            currentObject.put(name,v);
            return this;
        }

        public JsonObjects numberNode(String name, long v) {
            currentObject.put(name,v);
            return this;
        }

        public JsonObjects numberNode(String name, Long v) {
            currentObject.put(name,v);
            return this;
        }

        public JsonObjects numberNode(String name, BigInteger v) {
            currentObject.put(name,v);
            return this;
        }

        public JsonObjects numberNode(String name, float v) {
            currentObject.put(name,v);
            return this;
        }

        public JsonObjects numberNode(String name, Float v) {
            currentObject.put(name,v);
            return this;
        }

        public JsonObjects numberNode(String name, double v) {
            currentObject.put(name,v);
            return this;
        }

        public JsonObjects numberNode(String name, Double v) {
            currentObject.put(name,v);
            return this;
        }

        public JsonObjects numberNode(String name, BigDecimal v) {
            currentObject.put(name,v);
            return this;
        }

        public JsonObjects textNode(String name, String v) {
            currentObject.put(name,v);
            return this;
        }

        public JsonObjects binaryNode(String name, byte[] v) {
            currentObject.put(name,v);
            return this;
        }

        public JsonObjects binaryNode(String name, byte[] v, int offset, int length) {
            currentObject.set(name, jsonUtil.binaryNode(v, offset,length));
            return this;
        }

        public JsonObjects nullNode(String name) {
            currentObject.set(name, jsonUtil.nullNode());
            return this;
        }

        public JsonObjects objectNode(String name,ObjectNode value) {
            currentObject.set(name,value);
            return this;
        }

        public JsonObjects arrayNode(String name, ArrayNode value) {
            currentObject.set(name,value);
            return this;
        }

        public JsonObjects pojo(String name,Object pojo){
            currentObject.set(name, jsonUtil.pojo(pojo));
            return this;
        }

        public ObjectNode build() {
            return currentObject;
        }
    }

    public static class JsonArrays extends JSONS {
        protected ArrayNode array;
        public JsonArrays(ObjectMapper om) {
            super(om);
            array = jsonUtil.arrayNode();
        }
        public JsonArrays add(boolean v) {
            array.add(jsonUtil.booleanNode(v));
            return this;
        }

        public JsonArrays add(byte v) {
            array.add(jsonUtil.numberNode(v));
            return this;
        }

        public JsonArrays add(Byte v) {
            array.add(jsonUtil.numberNode(v));
            return this;
        }

        public JsonArrays add(short v) {
            array.add(jsonUtil.numberNode(v));
            return this;
        }

        public JsonArrays add(Short v) {
            array.add(jsonUtil.numberNode(v));
            return this;
        }

        public JsonArrays add(int v) {
            array.add(jsonUtil.numberNode(v));
            return this;
        }

        public JsonArrays add(Integer v) {
            array.add(jsonUtil.numberNode(v));
            return this;
        }

        public JsonArrays add(long v) {
            array.add(jsonUtil.numberNode(v));
            return this;
        }

        public JsonArrays add(Long v) {
            array.add(jsonUtil.numberNode(v));
            return this;
        }

        public JsonArrays add(BigInteger v) {
            array.add(jsonUtil.numberNode(v));
            return this;
        }

        public JsonArrays add(float v) {
            array.add(jsonUtil.numberNode(v));
            return this;
        }

        public JsonArrays add(Float v) {
            array.add(jsonUtil.numberNode(v));
            return this;
        }

        public JsonArrays add(double v) {
            array.add(jsonUtil.numberNode(v));
            return this;
        }

        public JsonArrays add(Double v) {
            array.add(jsonUtil.numberNode(v));
            return this;
        }

        public JsonArrays add(BigDecimal v) {
            array.add(jsonUtil.numberNode(v));
            return this;
        }

        public JsonArrays add(String v) {
            array.add(jsonUtil.textNode(v));
            return this;
        }

        public JsonArrays add(byte[] v) {
            array.add(jsonUtil.binaryNode(v));
            return this;
        }

        public JsonArrays add(byte[] v, int offset, int length) {
            array.add(jsonUtil.binaryNode(v, offset, length));
            return this;
        }

        public JsonArrays add(ObjectNode value) {
            array.add(value);
            return this;
        }

        public JsonArrays add(ArrayNode value) {
            array.add(value);
            return this;
        }

        public JsonArrays addNull() {
            array.add(jsonUtil.nullNode());
            return this;
        }

        public JsonArrays addPojo(Object pojo) {
            array.add(jsonUtil.pojo(pojo));
            return this;
        }
        public ArrayNode build() {
            return array;
        }
    }

    /**
     * Simple JsonNodeCreator implement.
     */
    private static class JsonUtil implements JsonNodeCreator {
        public ObjectMapper getOm() {
            return om;
        }

        private final ObjectMapper om;

        public JsonUtil(ObjectMapper om) {
            this.om = om;
        }

        public JsonNode pojo(Object value){
            return om.valueToTree(value);
        }

        public ValueNode booleanNode(boolean v) {
            return om.getNodeFactory().booleanNode(v);
        }

        public ValueNode numberNode(byte v) {
            return om.getNodeFactory().numberNode(v);
        }

        public ValueNode numberNode(Byte v) {
            return om.getNodeFactory().numberNode(v);
        }

        public ValueNode numberNode(short v) {
            return om.getNodeFactory().numberNode(v);
        }

        public ValueNode numberNode(Short value) {
            return om.getNodeFactory().numberNode(value);
        }

        public ValueNode numberNode(int v) {
            return om.getNodeFactory().numberNode(v);
        }

        public ValueNode numberNode(Integer v) {
            return om.getNodeFactory().numberNode(v);
        }

        public ValueNode numberNode(long v) {
            return om.getNodeFactory().numberNode(v);
        }

        public ValueNode numberNode(Long v) {
            return om.getNodeFactory().numberNode(v);
        }

        public ValueNode numberNode(BigInteger v) {
            return om.getNodeFactory().numberNode(v);
        }

        public ValueNode numberNode(float v) {
            return om.getNodeFactory().numberNode(v);
        }

        public ValueNode numberNode(Float v) {
            return om.getNodeFactory().numberNode(v);
        }

        public ValueNode numberNode(double v) {
            return om.getNodeFactory().numberNode(v);
        }

        public ValueNode numberNode(Double v) {
            return om.getNodeFactory().numberNode(v);
        }

        public ValueNode numberNode(BigDecimal v) {
            return om.getNodeFactory().numberNode(v);
        }

        public ValueNode textNode(String v) {
            return om.getNodeFactory().textNode(v);
        }

        public ValueNode binaryNode(byte[] v) {
            return om.getNodeFactory().binaryNode(v);
        }

        public ValueNode binaryNode(byte[] v, int offset, int length) {
            return om.getNodeFactory().binaryNode(v, offset, length);
        }

        @Override
        public ValueNode pojoNode(Object pojo) {
            return om.getNodeFactory().pojoNode(pojo);
        }

        public ValueNode rawValueNode(RawValue value) {
            return om.getNodeFactory().rawValueNode(value);
        }

        @Override
        public ArrayNode arrayNode() {
            return om.getNodeFactory().arrayNode();
        }

        public ArrayNode arrayNode(int capacity) {
            return om.getNodeFactory().arrayNode(capacity);
        }

        public ObjectNode objectNode() {
            return om.getNodeFactory().objectNode();
        }

        public ValueNode nullNode() {
            return om.getNodeFactory().nullNode();
        }
    }

}
