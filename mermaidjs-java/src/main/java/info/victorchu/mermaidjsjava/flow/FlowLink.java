package info.victorchu.mermaidjsjava.flow;

import info.victorchu.mermaidjsjava.Node;
import info.victorchu.mermaidjsjava.Utils;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;


@Getter
@Setter
public class FlowLink {
    @Nonnull
    private Node from;
    @Nonnull
    private Node to;

    @Nullable
    private String text;


    private Integer level = 1;

    private Boolean multiDirection = false;

    @Nonnull
    private LinkConfig config;

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
        return drawLink(Optional.ofNullable(getConfig()).orElse(LinkConfig.defaultConfig));
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
