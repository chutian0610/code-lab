package info.victorchu.compiler.simpleregex.util;

import info.victorchu.compiler.simpleregex.*;
import info.victorchu.compiler.simpleregex.util.Pair;
import info.victorchu.mermaidjsjava.MermaidJsConfig;
import info.victorchu.mermaidjsjava.MermaidJsWriter;
import info.victorchu.mermaidjsjava.flow.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * use mermaidjs to draw regex graph
 */
public class RegexNodeDrawVisitor implements RegexNodeVisitor<Pair<FlowNode,FlowNode>> {

    private static final String EMPTY = "ε";

    private final AtomicInteger ID = new AtomicInteger(1);

    List<FlowNode> nodes ;
    List<FlowLink> links ;
    FlowChartGraph graph;

    public RegexNodeDrawVisitor() {
        nodes =new ArrayList<>();
        links = new ArrayList<>();
        graph = new FlowChartGraph();
        graph.setConfig(FlowChartGraphConfig.builder().direction(Direction.LR).build());
        graph.setNodes(nodes);
        graph.setLinks(links);
    }


    @Override
    public Pair<FlowNode,FlowNode> visit(RegexCharNode node) {
        String nodeName = node.getNodeType().name();
        String id = String.valueOf(ID.getAndIncrement());
        FlowNode result=  FlowNode.builder().id(id).text(node.getCharacter().toString()).build();
        nodes.add(result);
        return Pair.of(result,result);
    }

    @Override
    public Pair<FlowNode,FlowNode> visit(RegexConcatNode node) {
        String nodeName = node.getNodeType().name();

        Pair<FlowNode,FlowNode> left = visit(node.getLeft());
        Pair<FlowNode,FlowNode> right = visit(node.getRight());
        links.add(FlowLink.builder().from(left.getRight()).to(right.getLeft()).text(EMPTY).build());
        return Pair.of(left.getLeft(),right.getRight());
    }

    @Override
    public Pair<FlowNode,FlowNode> visit(RegexOrNode node) {
        Pair<FlowNode,FlowNode> left = visit(node.getLeft());
        Pair<FlowNode,FlowNode> right = visit(node.getRight());

        String nodeName = node.getNodeType().name();
        String beginId = String.valueOf(ID.getAndIncrement());
        FlowNode begin=  FlowNode.builder().id(EMPTY+"_"+beginId).build();
        nodes.add(begin);

        String endId = String.valueOf(ID.getAndIncrement());
        FlowNode end=  FlowNode.builder().id(EMPTY+"_"+endId).build();
        nodes.add(end);

        links.add(FlowLink.builder().from(begin).to(left.getLeft()).text(EMPTY).build());
        links.add(FlowLink.builder().from(left.getRight()).to(end).text(EMPTY).build());

        links.add(FlowLink.builder().from(begin).to(right.getLeft()).text(EMPTY).build());
        links.add(FlowLink.builder().from(right.getRight()).to(end).text(EMPTY).build());

        return Pair.of(begin,end);
    }

    @Override
    public Pair<FlowNode,FlowNode> visit(RegexRepeatNode node) {
        Pair<FlowNode,FlowNode> inner = visit(node.getInnerNode());

        String nodeName = node.getNodeType().name();
        String beginId = String.valueOf(ID.getAndIncrement());
        FlowNode begin=  FlowNode.builder().id(EMPTY+"_"+beginId).build();
        nodes.add(begin);
        String endId = String.valueOf(ID.getAndIncrement());
        FlowNode end=  FlowNode.builder().id(EMPTY+"_"+endId).build();
        nodes.add(end);

        links.add(FlowLink.builder().from(begin).to(end).text(EMPTY).build());
        links.add(FlowLink.builder().from(begin).to(inner.getLeft()).text(EMPTY).build());
        links.add(FlowLink.builder().from(inner.getRight()).to(end).text(EMPTY).build());
        links.add(FlowLink.builder().from(inner.getRight()).to(inner.getLeft()).text(inner.getLeft().getQuotedText()).build());
        return Pair.of(begin,end);
    }

    public void draw() throws IOException {
        MermaidJsWriter.generateGraph(graph, MermaidJsConfig.defaultConfig,"./mermaid.html",true);
    }
}
