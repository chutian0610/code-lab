package info.victorchu.mermaidjsjava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlowChartLinkDrawTest {

    FlowChartNode from;
    FlowChartNode to;
    FlowChartLink link;

    @BeforeEach
    public void onBeforeEach(){
        from= new FlowChartNode();
        from.setId("id1");
        from.setText("this is node content");

        to = new FlowChartNode();
        to.setId("id2");
        to.setText("this is another node content");

        link = new FlowChartLink();
        link.setFrom(from);
        link.setTo(to);
        link.setLevel(1);
        link.setText("this is an test");
        link.setConfig(FlowChartLinkConfig.defaultConfig);
    }

    @Test
    public void testDrawLink1(){
        Assertions.assertEquals("id1---|\"this is an test\"|id2",link.drawLink());
    }

    @Test
    public void testDrawLink2(){
        link.getConfig().setArrowType(FlowChartLinkConfig.ArrowType.Arrow);
        link.getConfig().setShapeDrawerSupplier(()->FlowChartLinkConfig.LinkShapeDrawer.dotLink);
        Assertions.assertEquals("id1-.->|\"this is an test\"|id2",link.drawLink());
    }
}
