package info.victorchu.tool.mermaidjsjava;

import info.victorchu.tool.mermaidjsjava.flow.ArrowType;
import info.victorchu.tool.mermaidjsjava.flow.FlowLink;
import info.victorchu.tool.mermaidjsjava.flow.FlowNode;
import info.victorchu.tool.mermaidjsjava.flow.LinkConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static info.victorchu.tool.mermaidjsjava.flow.LinkShapeDrawer.dotLink;

class FlowLinkDrawTest {

    FlowNode from;
    FlowNode to;
    FlowLink flowLink;

    @BeforeEach
    public void onBeforeEach(){
        from= new FlowNode();
        from.setId("id1");
        from.setText("this is node content");

        to = new FlowNode();
        to.setId("id2");
        to.setText("this is another node content");

        flowLink = new FlowLink();
        flowLink.setFrom(from);
        flowLink.setTo(to);
        flowLink.setLevel(1);
        flowLink.setText("this is an test");
        flowLink.setConfig(LinkConfig.defaultConfig);
    }

    @Test
    public void testDrawLink1(){
        Assertions.assertEquals("id1---|\"this is an test\"|id2", flowLink.drawLink());
    }

    @Test
    public void testDrawLink2(){
        flowLink.getConfig().setArrowType(ArrowType.Arrow);
        flowLink.getConfig().setShapeDrawerSupplier(()-> dotLink);
        Assertions.assertEquals("id1-.->|\"this is an test\"|id2", flowLink.drawLink());
    }
}
