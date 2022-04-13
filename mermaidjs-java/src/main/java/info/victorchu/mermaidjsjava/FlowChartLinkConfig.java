package info.victorchu.mermaidjsjava;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

import static info.victorchu.mermaidjsjava.Constant.link_with_arrow;
import static info.victorchu.mermaidjsjava.Constant.vertical_bar;

@Setter
@Getter
public class FlowChartLinkConfig {
    private Supplier<FlowChartLinkConfig.LinkShapeDrawer> shapeDrawerSupplier;

    interface LinkShapeDrawer{
        String drawNodeWithText(FlowChartLink link);
        String drawNodeWithOutText(FlowChartLink link);

        default String drawNode(FlowChartLink link) {
            if (link.getText() != null && link.getText().length() > 0) {
                return drawNodeWithText(link);
            } else {
                return drawNodeWithOutText(link);
            }
        }

        LinkShapeDrawer linkWithArrowHead = new LinkShapeDrawer() {
            @Override
            public String drawNodeWithText(FlowChartLink link) {
                return link.getFrom().getId() + link_with_arrow+ vertical_bar
                        +link.getQuotedText() + vertical_bar + link.getTo().getId();
            }

            @Override
            public String drawNodeWithOutText(FlowChartLink link) {
                return link.getFrom().getId() + link_with_arrow +link.getTo().getId();
            }
        };

    }
}
