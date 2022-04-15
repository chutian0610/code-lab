package info.victorchu.mermaidjsjava.flow;

import info.victorchu.mermaidjsjava.Graph;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static info.victorchu.mermaidjsjava.Constant.new_line;
import static info.victorchu.mermaidjsjava.Constant.wb;

/**
 *
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlowChartGraph implements Graph {

    @Builder.Default
    private FlowChartGraphConfig config = FlowChartGraphConfig.defaultConfig;
    private List<FlowNode> nodes;
    private List<FlowLink> links;
    private List<SubGraph> subGraphs;


    @Override
    public String drawGraph() {
        StringBuilder sb = new StringBuilder();
        sb.append(graphType()).append(wb).append(config.getDirection().name()).append(new_line);
        if(nodes != null) {
            for (FlowNode node : nodes) {
                sb.append(node.drawNode()).append(new_line);
            }
        }
        if(subGraphs != null){
            for (SubGraph subGraph:subGraphs){
                sb.append(subGraph.drawNode()).append(new_line);
            }
        }

        // 先产生节点，再产生线
        if(links != null) {
            for (FlowLink link : links) {
                sb.append(link.drawLink()).append(new_line);
            }
        }

        return sb.toString();
    }

    @Override
    public String graphType() {
        return "flowchart";
    }
}
