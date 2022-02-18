package info.victorchu.mermaidjsjava;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


@Getter
@Setter
public class FlowChartLink {
    @Nonnull
    private FlowChartNode from;
    @Nonnull
    private FlowChartNode to;

    @Nullable
    private String text;

    @Nonnull
    private FlowChartLinkConfig config;
}
