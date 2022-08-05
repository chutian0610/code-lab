package info.victorchu.commontool.json.jackson.fluent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.annotation.concurrent.NotThreadSafe;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * json build tool for jackson.
 */
@NotThreadSafe
public class JSONPoet {
    /**
     * builder class
     */
    public static class Builder{
        private ObjectMapper mapper;
        private JsonNode rootNode;
        private Object root;
        public JSONPoet build(){
            if(mapper == null){
                mapper = new ObjectMapper();
            }
            if(root != null){
                rootNode = mapper.valueToTree(root);
            }
            return new JSONPoet(mapper,rootNode);
        }

        public Builder mapper(ObjectMapper mapper){
            this.mapper = mapper;
            return this;
        }

        public Builder rootNode(ObjectNode node){
            remove();
            this.rootNode = node;
            return this;
        }
        public Builder rootNode(ArrayNode node){
            remove();
            this.rootNode = node;
            return this;
        }

        public Builder root(Object fromNode){
            remove();
            this.root = fromNode;
            return this;
        }

        private void remove(){
            root = null;
            rootNode = null;
        }
    }

    public static JSONPoet.Builder builder(){
        return new JSONPoet.Builder();
    }

    private final ObjectMapper om;

    private NestedJsonNode current;

    private JSONPoet(ObjectMapper objectMapper,JsonNode rootNode) {
        this.om = objectMapper;
        if(rootNode!= null) {
            this.current = new NestedJsonNode(rootNode);
        }
    }

    /**
     * retreat to parent node
     * @return
     * @throws IllegalStateException
     */
    NestedJsonNode retreat() throws IllegalStateException {
        if (this.current.isRoot()) {
            // root node  no  need to retreat , just pass
            return null;
        }else {
            NestedJsonNode tmp = this.current;
            this.current = this.current.parent;
            return tmp;
        }
    }


    JSONPoet append(JsonNode jsonNode) throws IllegalStateException {
        if (this.current.value instanceof ArrayNode) {
            ArrayNode arrayNode = (ArrayNode) this.current.value;
            arrayNode.add(jsonNode);
        } else if (this.current.value instanceof ObjectNode) {
            if (this.current.nextName == null) {
                throw new IllegalStateException("tried to append JsonObject("+ jsonNode.toString()+") without name");
            }
            ObjectNode objectNode = (ObjectNode) this.current.value;
            objectNode.put(this.current.nextName, jsonNode);
            this.current.resetName();
        } else {
            throw new IllegalStateException("attempt to append element to " + this.current.value.getClass());
        }
        return this;
    }

    public JsonNode build() throws IllegalStateException {
        if (!this.current.isRoot()) {
            throw new IllegalStateException("current element is not root");
        }
        return this.current.value;
    }

    public String buildToString(boolean prettyPrint) throws IllegalStateException {
        if (!this.current.isRoot()) {
            throw new IllegalStateException("current element is not root");
        }
        if(!prettyPrint) {
            return this.current.value.toString();
        }
        return this.current.value.toPrettyString();
    }

    public ObjectNode buildObject() {
        JsonNode node =  build();
        if(node.isObject()){
            return (ObjectNode) node;
        }
        throw new IllegalStateException("root element is not Object");
    }
    public ArrayNode buildArray() {
        JsonNode node =  build();
        if(node.isArray()){
            return (ArrayNode) node;
        }
        throw new IllegalStateException("root element is not Array");
    }

    /*
    **********************************************************
    * Factory method for Container
    **********************************************************
    */
    public JSONPoet beginArray() {
        this.current = new NestedJsonNode(this.current, this.om.createArrayNode());
        return this;
    }

    public JSONPoet endArray() {
        NestedJsonNode node = retreat();
        if(node!=null) {
            append(node.value);
        }
        return this;
    }

    public JSONPoet beginObject() {
        this.current = new NestedJsonNode(this.current, this.om.createObjectNode());
        return this;
    }

    public JSONPoet endObject() {
        NestedJsonNode node = retreat();
        if(node!=null) {
            append(node.value);
        }
        return this;
    }

    /*
    /**********************************************************
    /* Factory method for JsonNode
    /**********************************************************
    */

    public JSONPoet booleanNode(boolean v) {
        append(om.getNodeFactory().booleanNode(v));
        return this;
    }

    public JSONPoet numberNode(byte v) {
        append(om.getNodeFactory().numberNode(v));
        return this;
    }

    public JSONPoet numberNode(Byte v) {
        append(om.getNodeFactory().numberNode(v));
        return this;
    }

    public JSONPoet numberNode(short v) {
        append(om.getNodeFactory().numberNode(v));
        return this;
    }

    public JSONPoet numberNode(Short value) {
        append(om.getNodeFactory().numberNode(value));
        return this;
    }

    public JSONPoet numberNode(int v) {
        append(om.getNodeFactory().numberNode(v));
        return this;
    }

    public JSONPoet numberNode(Integer v) {
        append(om.getNodeFactory().numberNode(v));
        return this;
    }

    public JSONPoet numberNode(long v) {
        append(om.getNodeFactory().numberNode(v));
        return this;
    }

    public JSONPoet numberNode(Long v) {
        append(om.getNodeFactory().numberNode(v));
        return this;
    }

    public JSONPoet numberNode(BigInteger v) {
        append(om.getNodeFactory().numberNode(v));
        return this;
    }

    public JSONPoet numberNode(float v) {
        append(om.getNodeFactory().numberNode(v));
        return this;
    }

    public JSONPoet numberNode(Float v) {
        append(om.getNodeFactory().numberNode(v));
        return this;
    }

    public JSONPoet numberNode(double v) {
        append(om.getNodeFactory().numberNode(v));
        return this;
    }

    public JSONPoet numberNode(Double v) {
        append(om.getNodeFactory().numberNode(v));
        return this;
    }

    public JSONPoet numberNode(BigDecimal v) {
        append(om.getNodeFactory().numberNode(v));
        return this;
    }

    public JSONPoet textNode(String v) {
        append(om.getNodeFactory().textNode(v));
        return this;
    }

    public JSONPoet binaryNode(byte[] v) {
        append(om.getNodeFactory().binaryNode(v));
        return this;
    }

    public JSONPoet binaryNode(byte[] v, int offset, int length) {
        append(om.getNodeFactory().binaryNode(v, offset, length));
        return this;
    }

    public JSONPoet nullNode() {
        append(om.getNodeFactory().nullNode());
        return this;
    }

    public JSONPoet pojo(Object fromValue) {
        append(om.valueToTree(fromValue));
        return this;
    }

    public JSONPoet name(String name) {
        if (name == null) {
            throw new NullPointerException("name == null");
        }
        this.current.nextName = name;
        return this;
    }

    private static class NestedJsonNode {
        NestedJsonNode parent;
        final JsonNode value;
        String nextName;

        NestedJsonNode(JsonNode element) {
            this.value = element;
        }

        NestedJsonNode(NestedJsonNode parent, JsonNode element) {
            this.parent = parent;
            this.value = element;
        }

        NestedJsonNode(NestedJsonNode parent, JsonNode element, String nextName) {
            this.parent = parent;
            this.nextName = nextName;
            this.value = element;
        }

        String resetName() {
            String tmp = this.nextName;
            this.nextName = null;
            return tmp;
        }

        boolean isRoot() {
            return this.parent == null;
        }
    }


}
