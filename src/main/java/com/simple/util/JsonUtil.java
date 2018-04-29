package com.simple.util;

import com.google.common.collect.Lists;
import com.simple.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Create by S I M P L E on 2018/03/10 13:10:52
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        //忽略空bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        //同一时间格式
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));
        //忽略在json字符串存在，在Java对象中不存在对应属性的错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error");
            return null;
        }
    }

    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error");
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            log.warn("Parse String to Object error");
            return null;
        }
    }

    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (Exception e) {
            log.warn("Parse String to Object error");
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<?> connectionClass, Class<?>...elementsClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(connectionClass,elementsClasses);

        try {
            return objectMapper.readValue(str,javaType);
        } catch (Exception e) {
            log.warn("Parse String to Object error");
            return null;
        }
    }

    public static void main(String[] args) {
        User u1 = new User();
        u1.setId(1);
        u1.setEmail("123@123.com");

        User u2 = new User();
        u2.setId(2);
        u2.setEmail("asd@asd.com");

        String userJson = JsonUtil.obj2String(u1);
        String userJsonPretty = JsonUtil.obj2StringPretty(u1);
        log.info("userJson:{}", userJson);
        log.info("userJsonPretty:{}", userJsonPretty);

        User user = JsonUtil.string2Obj(userJson, User.class);

        List<User> userList = Lists.newArrayList();
        userList.add(u1);
        userList.add(u2);

        String userListStr = JsonUtil.obj2String(userList);

        List<User> userList1 = JsonUtil.string2Obj(userListStr, new TypeReference<List<User>>() {
        });

        List<User> userList2 = JsonUtil.string2Obj(userListStr,List.class,User.class);
        System.out.println("end");
    }

}
