package info.victorchu.mermaidjsjava.flow;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

import static info.victorchu.mermaidjsjava.flow.LinkShapeDrawer.defaultLink;

@Setter
@Getter
public class LinkConfig {
    public static LinkConfig defaultConfig = getDefaultConfig();

    private static LinkConfig getDefaultConfig() {
        LinkConfig config = new LinkConfig();
        config.shapeDrawerSupplier = () -> defaultLink;
        config.arrowType = ArrowType.None;
        return config;
    }
    private Supplier<LinkShapeDrawer> shapeDrawerSupplier;
    private ArrowType arrowType;

}
