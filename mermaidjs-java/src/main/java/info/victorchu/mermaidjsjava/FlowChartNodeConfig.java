package info.victorchu.mermaidjsjava;

import lombok.Getter;
import lombok.Setter;

import static info.victorchu.mermaidjsjava.FlowChartNodeDrawer.defaultFlowChartNodeDrawer;
import static info.victorchu.mermaidjsjava.FlowChartNodeDrawer.nodeWithRoundEdge;
import static info.victorchu.mermaidjsjava.FlowChartNodeDrawer.stadiumShapedNode;


@Getter
@Setter
public class FlowChartNodeConfig {
    /**
     * 节点的形状
     */
    @Getter
    public enum Shape {
        Default(defaultFlowChartNodeDrawer),
        Node_With_Round_Edges(nodeWithRoundEdge),
        Stadium_Shaped_Node(stadiumShapedNode),

        ;
        private FlowChartNodeDrawer fDrawer;

        Shape(FlowChartNodeDrawer flowChartNodeDrawer) {
            this.fDrawer = flowChartNodeDrawer;
        }
    }
}
