package info.victorchu.mermaidjsjava;

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
public class FlowChartNode {
    @Nonnull
    private String id;
    @Nullable
    private String text;

    @Nullable
    private FlowChartNodeConfig config;

    public String getQuotedText() {
       return Utils.getQuotedText(text);
    }

    public String drawNode(){
        return drawNode(Optional.ofNullable(getConfig()).orElse(FlowChartNodeConfig.defaultConfig));
    }

    /**
     * 使用配置绘制流程图-点
     * @param config
     * @return
     */
    public String drawNode(FlowChartNodeConfig config){
        return config.getShapeDrawerSupplier().get().drawNode(this);
    }
}
