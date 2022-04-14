package info.victorchu.mermaidjsjava;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

import static info.victorchu.mermaidjsjava.Constant.*;
import static info.victorchu.mermaidjsjava.Utils.repeat;

@Setter
@Getter
public class FlowChartLinkConfig {
    public static FlowChartLinkConfig defaultConfig = getDefaultConfig();

    private static FlowChartLinkConfig getDefaultConfig() {
        FlowChartLinkConfig config = new FlowChartLinkConfig();
        config.shapeDrawerSupplier = () -> LinkShapeDrawer.defaultLink;
        config.arrowType = ArrowType.None;
        return config;
    }
    private Supplier<FlowChartLinkConfig.LinkShapeDrawer> shapeDrawerSupplier;
    private ArrowType arrowType;

    @Getter
    public static enum ArrowType{
        None("", ""),
        Fork("x","x"),
        Circle("o","o"),
        Arrow("<",">");
        private String left;
        private String right;

        ArrowType(String left, String right) {
            this.left = left;
            this.right = right;
        }
    }
    interface LinkShapeDrawer{
        default String drawNodeWithText(FlowChartLink link){
            return link.getFrom().getId() + getLink(link,link.getLevel(),link.getMultiDirection())
                    + vertical_bar +link.getQuotedText() + vertical_bar + link.getTo().getId();
        }
        default String drawNodeWithOutText(FlowChartLink link){
            return link.getFrom().getId() + getLink(link,link.getLevel(),link.getMultiDirection()) +link.getTo().getId();
        }

        String getLink(FlowChartLink link,int level,boolean multiDirection);

        default String drawNode(FlowChartLink link) {
            if (link.getText() != null && link.getText().length() > 0) {
                return drawNodeWithText(link);
            } else {
                return drawNodeWithOutText(link);
            }
        }

        LinkShapeDrawer defaultLink = new LinkShapeDrawer() {
            @Override
            public String getLink(FlowChartLink link, int level, boolean multiDirection) {
                String body = repeat(Constant.link,Constant.link,Constant.link,level);
                ArrowType arrowType = link.getConfig().getArrowType();
                if(multiDirection){
                    if (!arrowType.equals(ArrowType.None)) {
                        body = body.substring(0, body.length() - 1);
                    }
                    return arrowType.getLeft() + body + arrowType.getRight();
                }else {
                    if (!arrowType.equals(ArrowType.None)) {
                        body = body.substring(0, body.length() - 1);
                    }
                    return body+arrowType.getRight();
                }
            }
        };

        LinkShapeDrawer thickLink = new LinkShapeDrawer() {
            @Override
            public String getLink(FlowChartLink link, int level, boolean multiDirection) {
                String body = repeat(thick_link,Constant.thick_link, thick_link,level);
                ArrowType arrowType = link.getConfig().getArrowType();
                if(multiDirection){
                    if (!arrowType.equals(ArrowType.None)) {
                        body = body.substring(0, body.length() - 1);
                    }
                    return arrowType.getLeft() + body + arrowType.getRight();
                }else {
                    if (!arrowType.equals(ArrowType.None)) {
                        body = body.substring(0, body.length() - 1);
                    }
                    return body+arrowType.getRight();
                }
            }
        };

        LinkShapeDrawer dotLink = new LinkShapeDrawer() {
            @Override
            public String getLink(FlowChartLink link, int level, boolean multiDirection) {
                String body = repeat(Constant.link,Constant.link, dot,level);
                ArrowType arrowType = link.getConfig().getArrowType();
                if(multiDirection){
                    return arrowType.getLeft() + body + arrowType.getRight();
                }else {
                    return body+arrowType.getRight();
                }
            }
        };

    }
}
