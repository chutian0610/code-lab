package info.victorchu.snippets.utils;

/**
 * @author victorchu
 */
public class ByteUtils
{
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     *  byte 转16进制String
     * @param bytes
     * @return
     */
    private static String bytesToHexFun(byte[] bytes)
    {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) {
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }

        return new String(buf);
    }

    /**
     * byte 转16进制String,两位之间空格隔开
     *
     * @param bytes
     * @return
     */
    private static String bytesToHexFunPretty(byte[] bytes)
    {
        char[] buf = new char[bytes.length * 3];
        int index = 0;
        for (byte b : bytes) {
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
            buf[index++] = ' ';
        }

        return new String(buf).substring(0, buf.length - 1);
    }

    /**
     * 十六进制数转bytes
     *
     * @param str
     * @return
     */
    public static byte[] hexToBytes(String str)
    {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

    /**
     * int 转 byte array
     *
     * @param n
     * @return
     */
    public static byte[] IntToByteArray(int n)
    {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * byte Array 转 int
     *
     * @param bArr
     * @return
     */
    public static int ByteArrayToInt(byte[] bArr)
    {
        int result = 0;
        for (int i = 0; i < bArr.length; i++) {
            result = (bArr[i] & 0xff) + (result << 8);
        }
        return result;
    }

    /**
     * byte array 转 long
     *
     * @param bArr
     * @return
     */
    public static long ByteArrayToLong(byte[] bArr)
    {
        long result = 0;
        for (int i = 0; i < bArr.length; i++) {
            result = ((long) bArr[i] & 0xffL) + (result << 8);
        }
        return result;
    }
}
