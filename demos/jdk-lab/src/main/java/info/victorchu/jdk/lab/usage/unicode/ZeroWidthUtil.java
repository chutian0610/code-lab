package info.victorchu.jdk.lab.usage.unicode;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ZeroWidthUtil {
    public static String zeroWidthToBinary(String input){
        if(input ==null){
            return "";
        }
        char[] chars = input.toCharArray();
        String[] array = new String[chars.length];
        for (int i = 0; i < chars.length; i++) {
            array[i] = Integer.toBinaryString(chars[i]);
        }
        char[] chars2= String.join(" ",array).toCharArray();
        String[] array2 = new String[chars2.length];
        for (int i = 0; i < chars2.length; i++) {
            if(chars2[i] == ' '){
                array2[i] = "\u200d";
            }else {
                if(chars2[i] == '1'){
                    array2[i] = "\u200b";
                }else {
                    array2[i] = "\u200c";
                }
            }
        }
        String result = String.join("\ufeff",array2);
        return result;
    }

    public static String zeroWidthToText(String input){
        String result = Arrays.stream(input.split("\ufeff")).map(x->{
            if(x.equals("\u200b")){
                return "1";
            }else if(x.equals("\u200c")){
                return "0";
            }else {
                return " ";
            }
        }).collect(Collectors.joining());
        String[] array = result.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            Character item = (char) Integer.parseUnsignedInt(String.valueOf(array[i]), 2);
            sb.append(item);
        }
        return sb.toString();
    }

}
