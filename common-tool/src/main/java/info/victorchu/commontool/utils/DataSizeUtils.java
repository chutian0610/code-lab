package info.victorchu.commontool.utils;

/**
 * @Description:
 * @Date:2022/12/5 23:26
 * @Author:victorchutian
 */
public class DataSizeUtils {
    private static final long K = 1000;
    private static final long M = K * K;
    private static final long G = M * K;
    private static final long T = G * K;
    private static final long P = T * K;
    private static final long E = P * K;
    private static final long Z = E * K;
    private static final long Y = Z * K;
    private static final long[] METRIC_INTERVAL_LIST = { 1, K, M, G, T, P, E, Z, Y };
    private static final String[] METRIC_UNIT_LIST = { "B", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };

    private static final long Ki = 1024;
    private static final long Mi = Ki * Ki;
    private static final long Gi = Mi * Ki;
    private static final long Ti = Gi * Ki;
    private static final long Pi = Ti * Ki;
    private static final long Ei = Pi * Ki;
    private static final long Zi = Ei * Ki;
    private static final long Yi = Zi * Ki;
    private static final long[] NEO_INTERVAL_LIST = { 1, Ki, Mi, Gi, Ti, Pi, Ei, Zi, Yi };
    private static final String[] NEO_UNIT_LIST = { "B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB", "YiB" };

    /**
     * Formatting with neo unit symbols KiB, MiB, and so on, which are clearly
     * defined to use 1024 as conversion factor for different units.
     */
    public static String formatNeo(long size) {
        return format(size, NEO_INTERVAL_LIST, NEO_UNIT_LIST);
    }

    /**
     * Correct metric formatting with 1000 as conversion factor and metric units kB,
     * MB, and so on, which is however too easy to confuse with classic formatting,
     * which uses the same unit symbols. Many storage device producers use this
     * metric format to make their devices look bigger.
     */
    public static String formatMetric(long size) {
        return format(size, METRIC_INTERVAL_LIST, METRIC_UNIT_LIST);
    }

    /**
     * Classic, but outdated fashion to format with seemingly metric units kB, MB,
     * and so on, but with an conversion rate of 1024 instead of the metric 1000.
     */
    public static String formatClassic(long size) {
        return format(size, NEO_INTERVAL_LIST, METRIC_UNIT_LIST);
    }

    private static String format(long size, long[] intervalList, String[] unitList) {
        String signStr;
        if (size < 0) {
            size = -size;
            signStr = "-";
        } else {
            signStr = "";
        }

        int interval = getInterval(size, intervalList);
        double converted = ((double) size) / intervalList[interval];

        String unitStr = unitList[interval];

        String convertedStr = toRoundedString(converted);

        return signStr + convertedStr + " " + unitStr;
    }

    private static int getInterval(long size, long[] intervals) {
        if (size < intervals[1]) {
            return 0;
        } else if (size < intervals[2]) {
            return 1;
        } else if (size < intervals[3]) {
            return 2;
        } else if (size < intervals[4]) {
            return 3;
        } else if (size < intervals[5]) {
            return 4;
        } else if (size < intervals[6]) {
            return 5;
        } else if (size < intervals[7]) {
            return 6;
        } else if (size < intervals[8]) {
            return 7;
        } else {
            return 8;
        }
    }

    private static String toRoundedString(double converted) {
        String convertedStr;

        if (converted < 10) {
            convertedStr = Double.toString(Math.round(converted * 10.0D) / 10.0D);
        } else {
            convertedStr = Long.toString(Math.round(converted));
        }

        return convertedStr;
    }
}