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
public class Link {
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

    public String getQuotedText() {
        return Utils.getQuotedText(text);
    }

    public String drawLink(){
        return drawLink(Optional.ofNullable(getConfig()).orElse(LinkConfig.defaultConfig));
    }

    public String drawLink(LinkConfig config){
        return config.getShapeDrawerSupplier().get().drawLink(this);
    }
}
