package info.victorchu.mermaidjsjava.flow;

import static info.victorchu.mermaidjsjava.Constant.back_slash;
import static info.victorchu.mermaidjsjava.Constant.left_bracket;
import static info.victorchu.mermaidjsjava.Constant.left_curly_bracket;
import static info.victorchu.mermaidjsjava.Constant.left_square_bracket;
import static info.victorchu.mermaidjsjava.Constant.right_arrow;
import static info.victorchu.mermaidjsjava.Constant.right_bracket;
import static info.victorchu.mermaidjsjava.Constant.right_curly_bracket;
import static info.victorchu.mermaidjsjava.Constant.right_square_bracket;
import static info.victorchu.mermaidjsjava.Constant.slash;

/**
 * 流程图-点-绘制器集合
 */

public interface NodeShapeDrawer {
    String drawNodeWithText(Node node);

    default String drawNodeWithOutText(Node node) {
        return node.getId();
    }

    default String drawNode(Node node) {
        if (node.getText() != null && node.getText().length() > 0) {
            return drawNodeWithText(node);
        } else {
            return drawNodeWithOutText(node);
        }
    }

    // id1(This is the text in the box)
    NodeShapeDrawer nodeWithRoundEdge =
            node -> node.getId() + left_bracket + node.getQuotedText() + right_bracket;

    // id1([This is the text in the box])
    NodeShapeDrawer stadiumShape =
            node -> node.getId() + left_bracket + left_square_bracket + node.getQuotedText() + right_square_bracket + right_bracket;

    // id1[[This is the text in the box]]
    NodeShapeDrawer subroutineShape =
            node -> node.getId() + left_square_bracket + left_square_bracket + node.getQuotedText() + right_square_bracket + right_square_bracket;

    //id1[(Database)]
    NodeShapeDrawer cylindricalShape =
            node -> node.getId() + left_square_bracket + left_bracket + node.getQuotedText() + right_bracket + right_square_bracket;

    // id1((This is the text in the circle))
    NodeShapeDrawer circleFormShape =
            node -> node.getId() + left_bracket + left_bracket + node.getQuotedText() + right_bracket + right_bracket;
    // id1>This is the text in the box]

    // id1>This is the text in the box]
    NodeShapeDrawer asymmetricShape =
            node -> node.getId() + right_arrow + node.getQuotedText() + right_square_bracket;

    // id1{This is the text in the box}
    NodeShapeDrawer rhombusShape =
            node -> node.getId() + left_curly_bracket + node.getQuotedText() + right_curly_bracket;
    //id1{{This is the text in the box}}
    NodeShapeDrawer hexagonShape =
            node -> node.getId() + left_curly_bracket + left_curly_bracket + node.getQuotedText() + right_curly_bracket + right_curly_bracket;

    // id1[/This is the text in the box/]
    NodeShapeDrawer parallelogramShape =
            node -> node.getId() + left_square_bracket + slash + node.getQuotedText() + slash + right_square_bracket;

    // id1[\This is the text in the box\]
    NodeShapeDrawer parallelogramAltShape =
            node -> node.getId() + left_square_bracket + back_slash + node.getQuotedText() + back_slash + right_square_bracket;
    // A[/Christmas\]
    NodeShapeDrawer trapezoidShape =
            node -> node.getId() + left_square_bracket + slash + node.getQuotedText() + back_slash + right_square_bracket;
    // B[\Go shopping/]
    NodeShapeDrawer trapezoidAltShape =
            node -> node.getId() + left_square_bracket + back_slash + node.getQuotedText() + slash + right_square_bracket;

    // id1(((This is the text in the circle)))
    NodeShapeDrawer doubleCircleShape =
            node -> node.getId() + left_bracket + left_bracket + left_bracket
                    + node.getQuotedText() + right_bracket + right_bracket + right_bracket;
}
