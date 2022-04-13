package info.victorchu.mermaidjsjava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static info.victorchu.mermaidjsjava.FlowChartNodeConfig.NodeShapeDrawer.stadiumShape;


class FlowChartNodeDrawTest {
    private static FlowChartNode node;

    @BeforeEach
    public void onBeforeEach(){
        node= new FlowChartNode();
        node.setId("id1");
        node.setText("this is node content");
    }

    @Test
    void drawNode1() {
        Assertions.assertEquals("id1(this is node content)",node.drawNode());
    }

    @Test
    void drawNode2() {
        FlowChartNodeConfig cfg = new FlowChartNodeConfig(() -> stadiumShape);
        node.setConfig(cfg);
        Assertions.assertEquals("id1([this is node content])",node.drawNode());
    }

}