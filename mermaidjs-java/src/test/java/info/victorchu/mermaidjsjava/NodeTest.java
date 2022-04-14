package info.victorchu.mermaidjsjava;

import info.victorchu.mermaidjsjava.flow.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @Copyright: www.xiaojukeji.com Inc. All rights reserved.
 * @Description:
 * @Date:2022/2/18 11:48 上午
 * @Author:victorchutian
 */
class NodeTest {

    @Test
    void testGetQuotedText001() {
        Node node = new Node();
        node.setText("A double quote:\"");
        Assertions.assertEquals(node.getQuotedText(),"A double quote:#quot;");
    }
}