package info.victorchu.tool.mermaidjsjava;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MermaidJsConfig {

    public static final MermaidJsConfig defaultConfig = new MermaidJsConfig();
   private String cdn = "https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.esm.min.mjs"; //jsdelivr cdn

}
