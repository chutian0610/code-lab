package info.victorchu.commontool.utils;

import lombok.Cleanup;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author victorchu
 * @date 2022/8/5 22:05
 */
public class NetUtils {
    /**
     * 获取本地IP
     * @return ip
     */
    public static String getLocalIp() {
        String defaultIp = "127.0.0.1";
        try {
            String[] cmd = {"/bin/sh", "-c",
                    "ifconfig -a|grep inet|grep -v 127.0.0.1|grep -v inet6|awk '{print $2}'|tr -d \"addr:\""};
            Process ps = Runtime.getRuntime().exec(cmd);
            ps.waitFor();
            @Cleanup
            InputStream inputStream = ps.getInputStream();
            if (inputStream == null) {
                return defaultIp;
            }
            List<String> ips = (IOUtils.readLines(inputStream, StandardCharsets.UTF_8));
            if (CollectionUtils.isEmpty(ips)) {
                return defaultIp;
            }
            return ips.get(0);
        } catch (Exception e) {
            return defaultIp;
        }
    }

    /**
     * 验证 ip 合法性
     * @param ip
     * @return
     */
    public static boolean verifyIP(String ip){
        return verifyIPV4(ip) || verifyIPV6(ip);
    }

    /**
     * 验证 ipv4 合法性
     * @param ip
     * @return
     */
    public static boolean verifyIPV4(String ip){
        return sun.net.util.IPAddressUtil.isIPv4LiteralAddress(ip);
    }

    /**
     * 验证 ipv6 合法性
     * @param ip
     * @return
     */
    public static boolean verifyIPV6(String ip){
        return sun.net.util.IPAddressUtil.isIPv6LiteralAddress(ip);
    }

    /**
     * 获取本机mac地址
     * @return
     * @throws SocketException
     * @throws UnknownHostException
     */
    private static String getLocalMac() throws SocketException, UnknownHostException {
        //本机 host
        InetAddress ia = InetAddress.getLocalHost();
        //获取网卡，获取地址
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        StringBuffer sb = new StringBuffer("");
        for(int i=0; i<mac.length; i++) {
            if(i!=0) {
                sb.append("-");
            }
            //字节转换为整数
            int temp = mac[i]&0xff;
            String str = Integer.toHexString(temp);
            if(str.length()==1) {
                sb.append("0"+str);
            }else {
                sb.append(str);
            }
        }
        return sb.toString().toUpperCase();
    }
}
