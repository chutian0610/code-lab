package info.victorchu.commontool.json.jackson.om;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.function.Supplier;

/**
 * mapper 配置
 */
public class ObjectMapperSupplier implements Supplier<ObjectMapper> {

    @Override
    public ObjectMapper get() {
        return new ObjectMapper()
                //反序列化时候遇到不匹配的属性并不抛出异常
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                //序列化时候遇到空对象不抛出异常
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                //反序列化的时候如果是无效子类型,不抛出异常
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
                //启用反序列化所需的类型信息,在属性中添加@class
                .activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
                // 序列化LocalDateTIme和LocalDate的必要配置,由Jackson-data-JSR310实现
                .registerModule(new JavaTimeModule())
                // 兼容 Optional
                .registerModule(new Jdk8Module());
    }
}
