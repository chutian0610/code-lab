package info.victorchu.tool.mermaidjsjava;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MermaidJsConfig {

    public static final MermaidJsConfig defaultConfig = new MermaidJsConfig();
//
//    private String cdn = "https://cdn.jsdelivr.net/npm/mermaid/dist/mermaid.min.js"; //jsdelivr cdn
    private String cdn = "https://npm.elemecdn.com/mermaid@8.13.10/dist/mermaid.min.js"; // 饿了么 unpkg镜像

}
