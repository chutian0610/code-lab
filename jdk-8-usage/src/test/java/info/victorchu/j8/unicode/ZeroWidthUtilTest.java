package info.victorchu.j8.unicode;

import org.junit.jupiter.api.Test;

class ZeroWidthUtilTest {

    @Test
    void zeroWidthToBinary() {
        String result =  ZeroWidthUtil.zeroWidthToBinary("ηδΈθ§ζ");
        System.out.println("="+result+"=");
        System.out.println(ZeroWidthUtil.zeroWidthToText(result));
    }
}