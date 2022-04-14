package info.victorchu.mermaidjsjava.flow;

import info.victorchu.mermaidjsjava.Grapha;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 */
@Getter
@Setter
public class FlowChartGraph implements Grapha {

    private FlowChartGraphConfig config;
    private List<FlowNode> nodes;
    private List<FlowLink> flowLinks;
    private List<SubGraph> subGraphs;


    @Override
    public String drawGrapha() {
        return null;
    }
}
