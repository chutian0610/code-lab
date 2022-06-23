package info.victorchu.mermaidjsjava;

import info.victorchu.mermaidjsjava.flow.FlowNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NodeTest {

    @Test
    void testGetQuotedText001() {
        FlowNode node = new FlowNode();
        node.setText("A double quote:\"");
        Assertions.assertEquals("\"A double quote:#quot;\"",node.getQuotedText());
    }
}