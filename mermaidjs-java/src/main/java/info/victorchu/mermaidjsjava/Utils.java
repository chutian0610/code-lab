package info.victorchu.mermaidjsjava;

/**
 * @Copyright: www.xiaojukeji.com Inc. All rights reserved.
 * @Description:
 * @Date:2022/2/18 4:47 下午
 * @Author:victorchutian
 */
public class Utils {
    public static String getQuotedText(String text){
        return text.replaceAll("\"","#quot;");
    }
}
