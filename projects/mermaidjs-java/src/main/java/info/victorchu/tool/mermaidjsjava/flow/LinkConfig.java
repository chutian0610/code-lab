package info.victorchu.tool.mermaidjsjava.flow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.Supplier;

import static info.victorchu.tool.mermaidjsjava.flow.LinkShapeDrawer.defaultLink;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
