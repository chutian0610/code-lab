package info.victorchu.fluentjackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.fasterxml.jackson.databind.util.RawValue;

import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonWriter implements JsonNodeCreator {
    private final ObjectMapper om;

    public JsonWriter(ObjectMapper om) {
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
