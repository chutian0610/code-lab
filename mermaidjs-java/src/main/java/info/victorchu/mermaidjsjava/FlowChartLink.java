package info.victorchu.mermaidjsjava;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;


@Getter
@Setter
public class FlowChartLink {
    @Nonnull
    private FlowChartNode from;
    @Nonnull
    private FlowChartNode to;

    @Nullable
    private String text;


    private Integer level = 1;

    private Boolean multiDirection = false;

    @Nonnull
    private FlowChartLinkConfig config;

    public String getQuotedText() {
        return Utils.getQuotedText(text);
    }

    public String drawLink(){
        return drawLink(Optional.ofNullable(getConfig()).orElse(FlowChartLinkConfig.defaultConfig));
    }

    public String drawLink(FlowChartLinkConfig config){
        return config.getShapeDrawerSupplier().get().drawLink(this);
    }
}
