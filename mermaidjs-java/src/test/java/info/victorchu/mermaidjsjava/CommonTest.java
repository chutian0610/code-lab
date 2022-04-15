package info.victorchu.mermaidjsjava;

import info.victorchu.mermaidjsjava.flow.Direction;
import info.victorchu.mermaidjsjava.flow.FlowChartGraph;
import info.victorchu.mermaidjsjava.flow.FlowChartGraphConfig;
import info.victorchu.mermaidjsjava.flow.FlowLink;
import info.victorchu.mermaidjsjava.flow.FlowNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class CommonTest {
    @Test
    void drawNode1() {
       String text =  "---";
       System.out.println(text.substring(0,text.length() - 1));
    }

    @Test
    void drawGraph1() throws IOException {
        FlowNode a = FlowNode.builder().id("a").text("a node").build();
        FlowNode b =FlowNode.builder().id("b").text("b node").build();
        FlowNode c =FlowNode.builder().id("c").text("c node").build();
        List<FlowNode> nodes =new ArrayList<>();
        nodes.add(a);
        nodes.add(b);
        nodes.add(c);

        List<FlowLink> links = new ArrayList<>();
        links.add(FlowLink.builder().from(a).to(b).build());
        links.add(FlowLink.builder().from(a).to(c).build());
        links.add(FlowLink.builder().from(b).to(c).build());

        FlowChartGraph graph = new FlowChartGraph();
        graph.setConfig(FlowChartGraphConfig.builder().direction(Direction.LR).build());
        graph.setNodes(nodes);
        graph.setLinks(links);

        MermaidJsWriter.generateGraph(graph,MermaidJsConfig.defaultConfig,"./mermaid.html",true);
    }
}
