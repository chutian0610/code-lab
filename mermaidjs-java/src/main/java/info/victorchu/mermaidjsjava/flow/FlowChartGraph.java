package info.victorchu.mermaidjsjava.flow;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 */
@Getter
@Setter
public class FlowChartGraph {

    private FlowChartGraphConfig config;

    private List<FlowNode> nodes;

    private List<FlowLink> flowLinks;

}
