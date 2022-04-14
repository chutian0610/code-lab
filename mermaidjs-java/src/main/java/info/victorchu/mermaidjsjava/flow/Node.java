package info.victorchu.mermaidjsjava.flow;

import info.victorchu.mermaidjsjava.Node;
import info.victorchu.mermaidjsjava.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Node implements info.victorchu.mermaidjsjava.Node {
    @Nonnull
    private String id;
    @Nullable
    private String text;

    @Nullable
    private NodeConfig config;

    public String getQuotedText() {
       return Utils.getQuotedText(text);
    }

    public String drawNode(){
        return drawNode(Optional.ofNullable(getConfig()).orElse(NodeConfig.defaultConfig));
    }

    /**
     * 使用配置绘制流程图-点
     * @param config
     * @return
     */
    public String drawNode(NodeConfig config){
        return config.getShapeDrawerSupplier().get().drawNode(this);
    }
}
