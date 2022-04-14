package info.victorchu.mermaidjsjava.flow;

import info.victorchu.mermaidjsjava.Constant;

import static info.victorchu.mermaidjsjava.Constant.dot;
import static info.victorchu.mermaidjsjava.Constant.thick_link;
import static info.victorchu.mermaidjsjava.Constant.vertical_bar;
import static info.victorchu.mermaidjsjava.Utils.repeat;

public interface LinkShapeDrawer {
    default String drawLinkWithText(FlowLink flowLink){
        return flowLink.getFrom().getId() + getLink(flowLink, flowLink.getLevel(), flowLink.getMultiDirection())
                + vertical_bar + flowLink.getQuotedText() + vertical_bar + flowLink.getTo().getId();
    }
    default String drawLinkWithOutText(FlowLink flowLink){
        return flowLink.getFrom().getId() + getLink(flowLink, flowLink.getLevel(), flowLink.getMultiDirection()) + flowLink.getTo().getId();
    }

    String getLink(FlowLink flowLink, int level, boolean multiDirection);

    default String drawLink(FlowLink flowLink) {
        if (flowLink.getText() != null && flowLink.getText().length() > 0) {
            return drawLinkWithText(flowLink);
        } else {
            return drawLinkWithOutText(flowLink);
        }
    }
    LinkShapeDrawer defaultLink = new LinkShapeDrawer() {
        @Override
        public String getLink(FlowLink flowLink, int level, boolean multiDirection) {
            String body = repeat(Constant.link,Constant.link,Constant.link,level);
            ArrowType arrowType = flowLink.getConfig().getArrowType();
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
        public String getLink(FlowLink flowLink, int level, boolean multiDirection) {
            String body = repeat(thick_link,Constant.thick_link, thick_link,level);
            ArrowType arrowType = flowLink.getConfig().getArrowType();
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
        public String getLink(FlowLink flowLink, int level, boolean multiDirection) {
            String body = repeat(Constant.link,Constant.link, dot,level);
            ArrowType arrowType = flowLink.getConfig().getArrowType();
            if(multiDirection){
                return arrowType.getLeft() + body + arrowType.getRight();
            }else {
                return body+arrowType.getRight();
            }
        }
    };
}
