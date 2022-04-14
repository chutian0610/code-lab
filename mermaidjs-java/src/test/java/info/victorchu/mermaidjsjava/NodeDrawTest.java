package info.victorchu.mermaidjsjava;

import info.victorchu.mermaidjsjava.flow.FlowNode;
import info.victorchu.mermaidjsjava.flow.NodeConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static info.victorchu.mermaidjsjava.flow.NodeShapeDrawer.stadiumShape;


class NodeDrawTest {
    private static FlowNode node;

    @BeforeEach
    public void onBeforeEach(){
        node= new FlowNode();
        node.setId("id1");
        node.setText("this is node content");
    }

    @Test
    void drawNode1() {
        Assertions.assertEquals("id1(\"this is node content\")",node.drawNode());
    }

    @Test
    void drawNode2() {
        NodeConfig cfg = new NodeConfig(() -> stadiumShape);
        node.setConfig(cfg);
        Assertions.assertEquals("id1([\"this is node content\"])",node.drawNode());
    }

}