package info.victorchu.type.classload.simpleengine;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author victor
 * @Email victorchu0610@outlook.com
 * @Data 2019/6/20
 * @Version 1.0
 * @Description TODO
 */
public class ScriptFactory {

    public static boolean isStringEmpty(String str){
        if(str ==null || "".equals(str.trim())){
            return true;
        }
        return false;
    }

    public static String md5(String plainText) {
        //定义一个字节数组
        byte[] secretBytes = null;
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //对字符串进行加密
            md.update(plainText.getBytes());
            //获得加密后的数据
            secretBytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("未找到MD5实现！");
        }
        //将加密后的数据转换为16进制数字
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public static Script generateScriptFromExpression(String prefix, Script script, Map<String,Object> env){
        String name = prefix+md5(script.getOriginExp());
        script.setClassName(name);
        String java = SimpleClasswrapper.buildSimpleClass(name,script.getOriginExp(),env);
        script.setJavaClass(java);
        return script;
    }

    private static class SimpleClasswrapper{
        private static String classIdBegin = "public class ";
        private static String classBodyBegin = " implements info.victorchu.exp.Expression { ";
        private static String classBodyEnd = " } ";
        private static String funcIdBegin0 = " public Object run0 ( ";
        private static String funcIdBegin = " public Object run(java.util.Map<String,Object> env){ ";
        private static String funcIdEnd = " ) ";
        private static String funcBodyBegin = " { ";
        private static String funcBodyEnd = " } ";

        private static String buildSimpleClass(String className, String exp, Map<String,Object> env){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(classIdBegin)
                    .append(className).append(classBodyBegin).append("\n")
                    .append(funcIdBegin0);
            // param list
            List<String> args = new ArrayList<>();
            for (Map.Entry<String,Object> entry: env.entrySet()) {
                String name = entry.getKey();
                String type = entry.getValue().getClass().getCanonicalName();
                stringBuilder.append(type).append(" ").append(name).append(",");
                args.add(entry.getKey());
            }
            if(args.size()>0) {
                stringBuilder.setLength(stringBuilder.length() - 1);
            }
            stringBuilder.append(funcIdEnd).append(funcBodyBegin).append("\n")
                    .append(exp).append("\n")
                    .append(funcBodyEnd).append("\n");
            stringBuilder.append(funcIdBegin).append("\n").append("return run0(");
            for (String key:args) {
                key = key.replace("\"","\\\"");
                stringBuilder.append("env.get(\"").append(key).append("\")").append(",");
            }
            if(args.size()>0) {
                stringBuilder.setLength(stringBuilder.length() - 1);
            }
            stringBuilder.append(");").append(funcBodyEnd).append("\n");
            stringBuilder.append(classBodyEnd);
            return stringBuilder.toString();
        }
    }
}