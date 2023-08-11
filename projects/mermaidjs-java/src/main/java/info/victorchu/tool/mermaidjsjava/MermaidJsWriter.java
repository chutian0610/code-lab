package info.victorchu.tool.mermaidjsjava;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static info.victorchu.tool.mermaidjsjava.Constant.new_line;

public class MermaidJsWriter {

    /**
     * 生成 mermaid graph
     * @param graph
     * @return
     */
    public static String generateGraph(Graph graph,MermaidJsConfig mermaidJsConfig){
        StringBuilder sb = new StringBuilder();
        sb.append("<html>").append(new_line)
                .append("  <body>").append(new_line)
                .append("    <script type=\"module\">").append(new_line)
                .append("       import mermaid from '").append(mermaidJsConfig.getCdn()).append("';").append(new_line)
                .append("       mermaid.initialize({ startOnLoad: true });").append(new_line)
                .append("    </script>").append(new_line);
        sb.append("    <pre class=\"mermaid\">").append(new_line);
        sb.append(graph.drawGraph()).append(new_line);
        sb.append("    </pre>").append(new_line);
        sb.append("  </body>").append(new_line);
        sb.append("</html>");
        return sb.toString();
    }

    /**
     * 生成 mermaid graph,并写入文件
     * @param graph
     * @param path
     * @throws IOException
     */
    public static Path generateGraph(Graph graph, MermaidJsConfig mermaidJsConfig, String path, boolean write) throws IOException {
        File file = new File(path);
        boolean exists = file.exists();
        if(exists & !write) {
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
        if(!exists){
            if (!file.createNewFile()) {
                throw new IllegalArgumentException("创建文件" + path + "失败！");
            }
        }
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(generateGraph(graph,mermaidJsConfig));
        out.close();
        return file.toPath();
    }
}
