package info.victorchu.mermaidjsjava;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

@Getter
@Setter
public class MermaidJsWriter {
    private MermaidJsConfig mermaidJsConfig;

    /**
     * 生成 mermaid graph
     * @param grapha
     * @return
     */
    public String generateGrapha(Grapha grapha){
        StringBuilder sb = new StringBuilder();
        sb.append("<html>").append("\n")
                .append("  <body>").append("\n")
                .append("    <script src=\"").append(mermaidJsConfig.getCdn()).append("\"></script>\n")
                .append("    <script> mermaid.initialize({ startOnLoad: true }); </script>").append("\n");
        sb.append("    <div class=\"mermaid-graph\">\n");
        grapha.drawGrapha();
        sb.append("    </div>\n");
        sb.append("  </body>\n");
        sb.append("</html>");
        return sb.toString();
    }

    /**
     * 生成 mermaid graph,并写入文件
     * @param grapha
     * @param path
     * @throws IOException
     */
    public void generateGrapha(Grapha grapha,String path) throws IOException {
        File file = new File(path);
        if(file.exists()) {
            throw new IllegalArgumentException("创建文件" + path + "失败，目标文件已存在！");
        }
        if (path.endsWith(File.separator)) {
            throw new IllegalArgumentException("创建文件" + path + "失败，目标文件不能为目录！");
        }
        //判断目标文件所在的目录是否存在
        if(!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            if(!file.getParentFile().mkdirs()) {
                throw new IllegalArgumentException("创建目标文件所在目录失败！");
            }
        }
        if (file.createNewFile()) {
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(generateGrapha(grapha));
            out.close();
        } else {
            throw new IllegalArgumentException("创建文件" + path + "失败！");
        }
    }
}
