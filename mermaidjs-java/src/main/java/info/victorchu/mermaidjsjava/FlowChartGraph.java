package info.victorchu.mermaidjsjava;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FlowChartGraph {

    private FlowChartGraphConfig config;

    private List<FlowChartNode> nodes;

    private List<FlowChartLink> links;
}
