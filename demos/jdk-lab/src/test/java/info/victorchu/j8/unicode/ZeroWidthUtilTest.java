package info.victorchu.j8.unicode;

import info.victorchu.jdk.lab.usage.unicode.ZeroWidthUtil;
import org.junit.jupiter.api.Test;

class ZeroWidthUtilTest {

    @Test
    void zeroWidthToBinary() {
        String result =  ZeroWidthUtil.zeroWidthToBinary("看不见我");
        System.out.println("="+result+"=");
        System.out.println(ZeroWidthUtil.zeroWidthToText(result));
    }
}