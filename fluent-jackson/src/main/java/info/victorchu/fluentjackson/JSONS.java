package info.victorchu.fluentjackson;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.math.BigInteger;

public class JSONS {
    protected final JsonWriter jsonWriter;

    public JSONS(JsonWriter jsonWriter) {
        this.jsonWriter = jsonWriter;
    }

    public JsonObjects object() {
        return new JsonObjects(jsonWriter);
    }

    public JsonArrays array() {
        return new JsonArrays(jsonWriter);
    }
    static class JsonObjects extends JSONS {
        protected ObjectNode currentObject;
        private JsonObjects(JsonWriter jsonWriter) {
            super(jsonWriter);
            currentObject = jsonWriter.objectNode();
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
            currentObject.set(name,jsonWriter.binaryNode(v, offset,length));
            return this;
        }

        public JsonObjects nullNode(String name) {
            currentObject.set(name,jsonWriter.nullNode());
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
            currentObject.set(name,jsonWriter.pojo(pojo));
            return this;
        }

        public ObjectNode build() {
            return currentObject;
        }
    }

    static class JsonArrays extends JSONS {
        protected ArrayNode array;
        public JsonArrays(JsonWriter jsonWriter) {
            super(jsonWriter);
            array = jsonWriter.arrayNode();
        }
        public JsonArrays add(boolean v) {
            array.add(jsonWriter.booleanNode(v));
            return this;
        }

        public JsonArrays add(byte v) {
            array.add(jsonWriter.numberNode(v));
            return this;
        }

        public JsonArrays add(Byte v) {
            array.add(jsonWriter.numberNode(v));
            return this;
        }

        public JsonArrays add(short v) {
            array.add(jsonWriter.numberNode(v));
            return this;
        }

        public JsonArrays add(Short v) {
            array.add(jsonWriter.numberNode(v));
            return this;
        }

        public JsonArrays add(int v) {
            array.add(jsonWriter.numberNode(v));
            return this;
        }

        public JsonArrays add(Integer v) {
            array.add(jsonWriter.numberNode(v));
            return this;
        }

        public JsonArrays add(long v) {
            array.add(jsonWriter.numberNode(v));
            return this;
        }

        public JsonArrays add(Long v) {
            array.add(jsonWriter.numberNode(v));
            return this;
        }

        public JsonArrays add(BigInteger v) {
            array.add(jsonWriter.numberNode(v));
            return this;
        }

        public JsonArrays add(float v) {
            array.add(jsonWriter.numberNode(v));
            return this;
        }

        public JsonArrays add(Float v) {
            array.add(jsonWriter.numberNode(v));
            return this;
        }

        public JsonArrays add(double v) {
            array.add(jsonWriter.numberNode(v));
            return this;
        }

        public JsonArrays add(Double v) {
            array.add(jsonWriter.numberNode(v));
            return this;
        }

        public JsonArrays add(BigDecimal v) {
            array.add(jsonWriter.numberNode(v));
            return this;
        }

        public JsonArrays add(String v) {
            array.add(jsonWriter.textNode(v));
            return this;
        }

        public JsonArrays add(byte[] v) {
            array.add(jsonWriter.binaryNode(v));
            return this;
        }

        public JsonArrays add(byte[] v, int offset, int length) {
            array.add(jsonWriter.binaryNode(v, offset, length));
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
            array.add(jsonWriter.nullNode());
            return this;
        }

        public JsonArrays addPojo(Object pojo) {
            array.add(jsonWriter.pojo(pojo));
            return this;
        }
        public ArrayNode build() {
            return array;
        }
    }
}
