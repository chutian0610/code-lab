package info.victorchu.mermaidjsjava.flow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.Supplier;

/**
 * 流程图-点-配置
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NodeConfig {

    public static NodeConfig defaultConfig = getDefaultConfig();

    private static NodeConfig getDefaultConfig() {
        NodeConfig config = new NodeConfig();
        config.shapeDrawerSupplier = () -> NodeShapeDrawer.nodeWithRoundEdge;
        return config;
    }

    private Supplier<NodeShapeDrawer> shapeDrawerSupplier;
}
