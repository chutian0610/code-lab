# mermaidjs-java

mermaidjs的java api.用于生成mermaidjs的html。

## flow graph

```java
class Test {
    public void drawGraph1() throws IOException {
        FlowNode a = FlowNode.builder().id("a").text("a node").build();
        FlowNode b = FlowNode.builder().id("b").text("b node").build();
        FlowNode c = FlowNode.builder().id("c").text("c node").build();
        List<FlowNode> nodes = new ArrayList<>();
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

        MermaidJsWriter.generateGraph(graph, MermaidJsConfig.defaultConfig, "./mermaid.html", true);
    }
}
```