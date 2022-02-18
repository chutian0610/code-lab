package info.victorchu.mermaidjsjava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Copyright: www.xiaojukeji.com Inc. All rights reserved.
 * @Description:
 * @Date:2022/2/18 11:48 上午
 * @Author:victorchutian
 */
class FlowChartNodeTest {

    @Test
    void testGetQuotedText001() {
        FlowChartNode node = new FlowChartNode();
        node.setText("A double quote:\"");
        Assertions.assertEquals(node.getQuotedText(),"A double quote:#quot;");
    }
}