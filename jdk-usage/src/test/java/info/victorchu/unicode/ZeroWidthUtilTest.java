package info.victorchu.unicode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZeroWidthUtilTest {

    @Test
    void zeroWidthToBinary() {
        String result =  ZeroWidthUtil.zeroWidthToBinary("楼下小黑哥");
        System.out.println(result);
        System.out.println(ZeroWidthUtil.zeroWidthToText(result));
    }
}