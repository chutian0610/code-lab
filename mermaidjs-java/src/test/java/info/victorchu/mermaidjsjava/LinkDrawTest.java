package info.victorchu.mermaidjsjava;

import info.victorchu.mermaidjsjava.flow.ArrowType;
import info.victorchu.mermaidjsjava.flow.Link;
import info.victorchu.mermaidjsjava.flow.LinkConfig;
import info.victorchu.mermaidjsjava.flow.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static info.victorchu.mermaidjsjava.flow.LinkShapeDrawer.dotLink;

class LinkDrawTest {

    Node from;
    Node to;
    Link link;

    @BeforeEach
    public void onBeforeEach(){
        from= new Node();
        from.setId("id1");
        from.setText("this is node content");

        to = new Node();
        to.setId("id2");
        to.setText("this is another node content");

        link = new Link();
        link.setFrom(from);
        link.setTo(to);
        link.setLevel(1);
        link.setText("this is an test");
        link.setConfig(LinkConfig.defaultConfig);
    }

    @Test
    public void testDrawLink1(){
        Assertions.assertEquals("id1---|\"this is an test\"|id2",link.drawLink());
    }

    @Test
    public void testDrawLink2(){
        link.getConfig().setArrowType(ArrowType.Arrow);
        link.getConfig().setShapeDrawerSupplier(()-> dotLink);
        Assertions.assertEquals("id1-.->|\"this is an test\"|id2",link.drawLink());
    }
}
