package info.victorchu.tool.mermaidjsjava;

public class Utils {
    // 统一处理异常字符
    public static String getQuotedText(String text){
        if(text ==null ||text.length()==0){
            return "";
        }
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
