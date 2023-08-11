package info.victorchu.snippets.option;

/**
 * 使用位运算实现组合开关
 */
public class ByteOption {

    public enum Option {
        /*
          option
         */
        OPTION_1(op1),
        OPTION_2(op2),
        OPTION_3(op3),
        OPTION_4(op4),
        OPTION_5(op5),
        OPTION_6(op6),
        OPTION_7(op7)
        ;

        byte value;

        Option(byte i) {
            value = i;
        }
    }
    public static byte op1 =0b00000001;
    public static byte op2 =0b00000010;
    public static byte op3 =0b00000100;
    public static byte op4 =0b00001000;
    public static byte op5 =0b00010000;
    public static byte op6 =0b00100000;
    public static byte op7 =0b01000000;
    private static int ALL = 0b11111111;


    private byte op = 0b00000000;

    public void enableOption(Option option){
        op = (byte) (op | option.value);
    }

    public void disableOption(Option option){
        op = (byte) (op & (option.value^ALL));
    }

    public boolean isEnable(Option option){
        return option.value == (op & option.value);
    }

}
