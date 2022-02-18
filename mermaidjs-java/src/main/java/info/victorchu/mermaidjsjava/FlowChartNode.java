package info.victorchu.mermaidjsjava;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Getter
@Setter
public class FlowChartNode {
    @Nonnull
    private String id;
    @Nullable
    private String text;

    public String getQuotedText() {
       return Utils.getQuotedText(text);
    }


}
