package info.victorchu.mermaidjsjava;

/**
 * @Copyright: www.xiaojukeji.com Inc. All rights reserved.
 * @Description:
 * @Date:2022/2/18 4:47 下午
 * @Author:victorchutian
 */
public class Utils {
    // 统一处理异常字符
    public static String getQuotedText(String text){
        text = text.replaceAll("\"","#quot;");
        text = "\""+text+"\"";
        return text;
    }

    // link 拼接
    public static String repeat(String begin,String end,String text,Integer level){
        StringBuilder sb = new StringBuilder(begin);
        for(int i=0;i<level;i++){
            sb.append(text);
        }
        sb.append(end);
        return sb.toString();
    }
}
