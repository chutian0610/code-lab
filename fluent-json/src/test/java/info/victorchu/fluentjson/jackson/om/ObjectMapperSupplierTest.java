package info.victorchu.fluentjson.jackson.om;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
class ObjectMapperSupplierTest {


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class User {
        private String id;
        private String name;
        private String desc;
    }

    @Data
    @ToString(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    static class SuperUser extends User{
        private Boolean isSuper;

        private Date time1;

        private LocalDateTime time2;

        @ToString.Include(name = "time1")
        private String formatTime1() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(time1.getTime());
        }
    }
    static final ObjectMapper MAPPER_E = new ObjectMapper();
    static final ObjectMapper MAPPER_C = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    static final ObjectMapper MAPPER_CT = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
            ;
    static final ObjectMapper MAPPER_CT_TIME = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
            .registerModule(new JavaTimeModule());
    static final ObjectMapper MAPPER_CTL = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
            .activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE, JsonTypeInfo.As.PROPERTY)
            ;
    static JacksonJsonSerializer getJackson2JsonSerializer(ObjectMapper objectMapper){
        if(objectMapper != null) {
            return new JacksonJsonSerializer(objectMapper);
        }
        return new JacksonJsonSerializer();
    }

    @Test
    public void testJsonWithDefault(){
        JacksonJsonSerializer serializer = getJackson2JsonSerializer(null);
        User user = new User();
        user.setId("1");
        user.setName("test");
        user.setDesc("common");
        String result = new String(serializer.serialize(user));
        log.info("generate:{}",result);
        String less = "{\"@class\":\"info.victorchu.fluentjackson.om.ObjectMapperSupplierTest$User\",\"id\":\"1\",\"name\":\"test\"}";
        log.info("check with less fields:{}",less);
        User lessUser = serializer.deserialize(less.getBytes(),User.class);
        log.info("check with less fields,result:{}",lessUser);
        String more = "{\"@class\":\"info.victorchu.fluentjackson.om.ObjectMapperSupplierTest$User\",\"id\":\"1\",\"name\":\"test\",\"desc\":\"common\",\"desc1\":\"common\"}";
        log.info("check with more fields:{}",more);
        User moreUser = serializer.deserialize(more.getBytes(),User.class);
        log.info("check with more fields,result:{}",moreUser);
    }

    @Test
    public void testJsonWithSimple(){
        JacksonJsonSerializer serializer = getJackson2JsonSerializer(MAPPER_E);
        User user = new User();
        user.setId("1");
        user.setName("test");
        user.setDesc("common");
        String result = new String(serializer.serialize(user));
        log.info("generate:{}",result);
        String less = "{\"id\":\"1\",\"name\":\"test\"}";
        log.info("check with less fields:{}",less);
        User lessUser = serializer.deserialize(less.getBytes(),User.class);
        log.info("check with less fields,result:{}",lessUser);
        String more = "{\"id\":\"1\",\"name\":\"test\",\"desc\":\"common\",\"desc1\":\"common\"}";
        log.info("check with more fields:{}",more);
        User moreUser = serializer.deserialize(more.getBytes(),User.class);
        log.info("check with more fields,result:{}",moreUser);
    }

    @Test
    public void testJsonWithConfig(){
        JacksonJsonSerializer serializer = getJackson2JsonSerializer(MAPPER_C);
        User user = new User();
        user.setId("1");
        user.setName("test");
        user.setDesc("common");
        String result = new String(serializer.serialize(user));
        log.info("generate:{}",result);
        String less = "{\"id\":\"1\",\"name\":\"test\"}";
        log.info("check with less fields:{}",less);
        User lessUser = serializer.deserialize(less.getBytes(),User.class);
        log.info("check with less fields,result:{}",lessUser);
        String more = "{\"id\":\"1\",\"name\":\"test\",\"desc\":\"common\",\"desc1\":\"common\"}";
        log.info("check with more fields:{}",more);
        User moreUser = serializer.deserialize(more.getBytes(),User.class);
        log.info("check with more fields,result:{}",moreUser);
    }

    @Test
    public void testJsonWithConfigForExtend(){
        JacksonJsonSerializer serializer = getJackson2JsonSerializer(MAPPER_C);
        SuperUser superUser = new SuperUser();
        superUser.setId("1");
        superUser.setName("test");
        superUser.setDesc("common");
        String result = new String(serializer.serialize(superUser));
        log.info("generate:{}",result);
        String less = "{\"id\":\"1\",\"name\":\"test\",\"isSuper\":null}";
        log.info("check with less fields:{}",less);
        User lessUser = serializer.deserialize(less.getBytes(),User.class);
        log.info("check with less fields,result:{}",lessUser);
        String more = "{\"id\":\"1\",\"name\":\"test\",\"desc\":\"common\",\"isSuper\":null,\"desc2\":\"common\"}";
        log.info("check with more fields:{}",more);
        User moreUser = serializer.deserialize(more.getBytes(),User.class);
        log.info("check with more fields,result:{}",moreUser);
        Assertions.assertTrue(moreUser instanceof SuperUser);
    }


    @Test
    public void testJsonWithConfigAndTypeForExtend(){
        JacksonJsonSerializer serializer = getJackson2JsonSerializer(MAPPER_CT);
        SuperUser superUser = new SuperUser();
        superUser.setId("1");
        superUser.setName("test");
        superUser.setDesc("common");
        String result = new String(serializer.serialize(superUser));
        log.info("generate:{}",result);
        String less = "{\"@class\":\"info.victorchu.fluentjackson.om.ObjectMapperSupplierTest$SuperUser\",\"id\":\"1\",\"name\":\"test\",\"isSuper\":null}";
        log.info("check with less fields:{}",less);
        User lessUser = serializer.deserialize(less.getBytes(),User.class);
        log.info("check with less fields,result:{}",lessUser);
        String more = "{\"@class\":\"info.victorchu.fluentjackson.om.ObjectMapperSupplierTest$SuperUser\",\"id\":\"1\",\"name\":\"test\",\"desc\":\"common\",\"isSuper\":null,\"desc2\":\"common\"}";
        log.info("check with more fields:{}",more);
        User moreUser = serializer.deserialize(more.getBytes(),User.class);
        log.info("check with more fields,result:{}",moreUser);
        Assertions.assertTrue(moreUser instanceof SuperUser);
    }

    @Test
    public void testJsonWithConfigAndTypeForExtend2(){
        JacksonJsonSerializer serializer = getJackson2JsonSerializer(MAPPER_CTL);
        SuperUser superUser = new SuperUser();
        superUser.setId("1");
        superUser.setName("test");
        superUser.setDesc("common");
        String result = new String(serializer.serialize(superUser));
        log.info("generate:{}",result);
        String less = "{\"id\":\"1\",\"name\":\"test\",\"isSuper\":null}";
        log.info("check with less fields:{}",less);
        User lessUser = serializer.deserialize(less.getBytes(),User.class);
        log.info("check with less fields,result:{}",lessUser);
        String more = "{\"id\":\"1\",\"name\":\"test\",\"desc\":\"common\",\"isSuper\":null,\"desc2\":\"common\"}";
        log.info("check with more fields:{}",more);
        User moreUser = serializer.deserialize(more.getBytes(),User.class);
        log.info("check with more fields,result:{}",moreUser);
        Assertions.assertTrue(moreUser instanceof SuperUser);
    }
    @Test
    public void testJsonWithConfigAndTypeAndDateForExtend(){
        JacksonJsonSerializer serializer = getJackson2JsonSerializer(MAPPER_CT);
        SuperUser superUser = new SuperUser();
        superUser.setId("1");
        superUser.setName("test");
        superUser.setDesc("common");
        superUser.setTime1(new Date());
        superUser.setTime2(LocalDateTime.now());
        String result = new String(serializer.serialize(superUser));
        log.info("generate:{}",result);
        String less = "{\"@class\":\"info.victorchu.fluentjackson.om.ObjectMapperSupplierTest$SuperUser\",\"id\":\"1\",\"name\":\"test\",\"isSuper\":null,\"time1\":[\"java.util.Date\",1646992908232],\"time2\":{\"year\":2022,\"month\":\"MARCH\",\"nano\":237000000,\"monthValue\":3,\"dayOfMonth\":11,\"hour\":18,\"minute\":1,\"second\":48,\"dayOfYear\":70,\"dayOfWeek\":\"FRIDAY\",\"chronology\":{\"@class\":\"java.time.chrono.IsoChronology\",\"id\":\"ISO\",\"calendarType\":\"iso8601\"}}}";
        log.info("check with less fields:{}",less);
        User lessUser = serializer.deserialize(less.getBytes(),User.class);
        log.info("check with less fields,result:{}",lessUser);
        String more = "{\"@class\":\"info.victorchu.fluentjackson.om.ObjectMapperSupplierTest$SuperUser\",\"id\":\"1\",\"name\":\"test\",\"desc\":\"common\",\"desc2\":\"common\",\"isSuper\":null,\"time1\":[\"java.util.Date\",1646992908232],\"time2\":{\"year\":2022,\"month\":\"MARCH\",\"nano\":237000000,\"monthValue\":3,\"dayOfMonth\":11,\"hour\":18,\"minute\":1,\"second\":48,\"dayOfYear\":70,\"dayOfWeek\":\"FRIDAY\",\"chronology\":{\"@class\":\"java.time.chrono.IsoChronology\",\"id\":\"ISO\",\"calendarType\":\"iso8601\"}}}";
        log.info("check with more fields:{}",more);
        User moreUser = serializer.deserialize(more.getBytes(),User.class);
        log.info("check with more fields,result:{}",moreUser);
        Assertions.assertTrue(moreUser instanceof SuperUser);
    }

    @Test
    public void testJsonWithConfigAndTypeAndDateForExtend2(){
        JacksonJsonSerializer serializer = getJackson2JsonSerializer(MAPPER_CT_TIME);
        SuperUser superUser = new SuperUser();
        superUser.setId("1");
        superUser.setName("test");
        superUser.setDesc("common");
        superUser.setTime1(new Date());
        superUser.setTime2(LocalDateTime.now());
        String result = new String(serializer.serialize(superUser));
        log.info("generate:{}",result);
        String less = "{\"@class\":\"info.victorchu.fluentjackson.om.ObjectMapperSupplierTest$SuperUser\",\"id\":\"1\",\"name\":\"test\",\"isSuper\":null,\"time1\":[\"java.util.Date\",1646992734766],\"time2\":[2022,3,11,17,58,54,782000000]}";
        log.info("check with less fields:{}",less);
        User lessUser = serializer.deserialize(less.getBytes(),User.class);
        log.info("check with less fields,result:{}",lessUser);
        String more = "{\"@class\":\"info.victorchu.fluentjackson.om.ObjectMapperSupplierTest$SuperUser\",\"id\":\"1\",\"name\":\"test\",\"desc\":\"common\",\"desc1\":\"common\",\"isSuper\":null,\"time1\":[\"java.util.Date\",1646992734766],\"time2\":[2022,3,11,17,58,54,782000000]}";
        log.info("check with more fields:{}",more);
        User moreUser = serializer.deserialize(more.getBytes(),User.class);
        log.info("check with more fields,result:{}",moreUser);
        Assertions.assertTrue(moreUser instanceof SuperUser);
    }
}