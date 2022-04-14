package info.victorchu.mermaidjsjava.flow;

import info.victorchu.mermaidjsjava.Node;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubGraph implements Node {
    private String id;
    private Direction direction;
    private List<FlowLink> links;
    private List<SubGraph> subGraphs;
}
