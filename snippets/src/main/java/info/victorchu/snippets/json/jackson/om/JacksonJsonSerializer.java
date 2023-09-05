package info.victorchu.snippets.json.jackson.om;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Nullable;

/**
 * Jackson json 序列化器
 * @author victorchu

 */
public class JacksonJsonSerializer {
    private static final byte[] EMPTY_ARRAY = new byte[0];
    private final ObjectMapper mapper;

    public JacksonJsonSerializer(){
        this(new ObjectMapper());
    }
    public JacksonJsonSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    static boolean isEmpty(@Nullable byte[] data) {
        return (data == null || data.length == 0);
    }

    public byte[] serialize(@Nullable Object source) {

        if (source == null) {
            return EMPTY_ARRAY;
        }

        try {
            return mapper.writeValueAsBytes(source);
        } catch (JsonProcessingException e) {
            throw new JacksonJsonSerializerException("Could not write JSON: " + e.getMessage(), e);
        }
    }

    public Object deserialize(@Nullable byte[] source) {
        return deserialize(source, Object.class);
    }

    @Nullable
    public <T> T deserialize(@Nullable byte[] source, Class<T> type){

        if(type== null){
            throw new IllegalArgumentException("Deserialization type must not be null! Please provide Object.class to make use of Jackson2 default typing.");
        }

        if (isEmpty(source)) {
            return null;
        }

        try {
            return mapper.readValue(source, type);
        } catch (Exception ex) {
            throw new JacksonJsonSerializerException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }
}
