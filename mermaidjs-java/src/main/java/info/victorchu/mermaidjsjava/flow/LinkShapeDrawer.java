package info.victorchu.mermaidjsjava.flow;

import info.victorchu.mermaidjsjava.Constant;

import static info.victorchu.mermaidjsjava.Constant.dot;
import static info.victorchu.mermaidjsjava.Constant.thick_link;
import static info.victorchu.mermaidjsjava.Constant.vertical_bar;
import static info.victorchu.mermaidjsjava.Utils.repeat;

public interface LinkShapeDrawer {
    default String drawLinkWithText(Link link){
        return link.getFrom().getId() + getLink(link,link.getLevel(),link.getMultiDirection())
                + vertical_bar +link.getQuotedText() + vertical_bar + link.getTo().getId();
    }
    default String drawLinkWithOutText(Link link){
        return link.getFrom().getId() + getLink(link,link.getLevel(),link.getMultiDirection()) +link.getTo().getId();
    }

    String getLink(Link link, int level, boolean multiDirection);

    default String drawLink(Link link) {
        if (link.getText() != null && link.getText().length() > 0) {
            return drawLinkWithText(link);
        } else {
            return drawLinkWithOutText(link);
        }
    }
    LinkShapeDrawer defaultLink = new LinkShapeDrawer() {
        @Override
        public String getLink(Link link, int level, boolean multiDirection) {
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
        public String getLink(Link link, int level, boolean multiDirection) {
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
        public String getLink(Link link, int level, boolean multiDirection) {
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
