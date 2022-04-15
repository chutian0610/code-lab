package info.victorchu.mermaidjsjava.flow;

import info.victorchu.mermaidjsjava.Node;
import info.victorchu.mermaidjsjava.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class FlowNode implements Node {
    @Nonnull
    private String id;
    @Nullable
    private String text;

    @Builder.Default
    private NodeConfig config = NodeConfig.defaultConfig;

    public String getQuotedText() {
       return Utils.getQuotedText(text);
    }

    public String drawNode(){
        return drawNode(getConfig());
    }

    /**
     * 使用配置绘制流程图-点
     * @param config
     * @return
     */
    public final String drawNode(NodeConfig config){
        return config.getShapeDrawerSupplier().get().drawNode(this);
    }
}
