package info.victorchu.mermaidjsjava.flow;

import info.victorchu.mermaidjsjava.Node;
import info.victorchu.mermaidjsjava.flow.FlowChartGraphConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubGraph implements Node {
    private String id;
    private Direction direction;
}
