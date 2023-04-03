package info.victorchu.commontool.option;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ByteOptionTest {

    @Test
    public void test1(){
        ByteOption byteOption = new ByteOption();
        byteOption.enableOption(ByteOption.Option.OPTION_1);
        byteOption.enableOption(ByteOption.Option.OPTION_2);
        byteOption.enableOption(ByteOption.Option.OPTION_3);

        byteOption.disableOption(ByteOption.Option.OPTION_2);

        Assertions.assertTrue(byteOption.enable(ByteOption.Option.OPTION_1));
        Assertions.assertTrue(byteOption.enable(ByteOption.Option.OPTION_3));
        Assertions.assertFalse(byteOption.enable(ByteOption.Option.OPTION_2));

    }
}