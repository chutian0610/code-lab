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
public class FlowLink {
    @Nonnull
    private Node from;
    @Nonnull
    private Node to;

    @Nullable
    private String text;


    @Nonnull
    @Builder.Default
    private Integer level = 1;

    @Nonnull
    @Builder.Default
    private Boolean multiDirection = false;

    @Builder.Default
    private LinkConfig config = LinkConfig.defaultConfig;

    /**
     * 获取转义处理后的文本
     * @return
     */
    public String getQuotedText() {
        return Utils.getQuotedText(text);
    }

    /**
     * 绘制link
     * @return
     */
    public String drawLink(){
        return drawLink(getConfig());
    }

    /**
     * 使用外部配置绘制link
     * @param config
     * @return
     */
    public final String drawLink(LinkConfig config){
        return config.getShapeDrawerSupplier().get().drawLink(this);
    }
}
