package info.victorchu.mermaidjsjava;

import static info.victorchu.mermaidjsjava.Constant.left_bracket;
import static info.victorchu.mermaidjsjava.Constant.left_square_brackets;
import static info.victorchu.mermaidjsjava.Constant.right_bracket;
import static info.victorchu.mermaidjsjava.Constant.right_square_brackets;

@FunctionalInterface
public interface FlowChartNodeDrawer {
    String drawNode(FlowChartNode node);

    FlowChartNodeDrawer defaultFlowChartNodeDrawer=
            node -> {
                if(node.getText() != null && node.getText().length()>0){
                    return node.getId() + left_square_brackets + node.getText() + right_square_brackets;
                }else {
                    return node.getId();
                }
            };

    FlowChartNodeDrawer nodeWithRoundEdge=
            node-> node.getId() + left_bracket + node.getText() + right_bracket;

    FlowChartNodeDrawer stadiumShapedNode =
            node-> node.getId() + left_bracket + left_square_brackets + node.getText() + right_square_brackets +right_bracket;
}
