package info.victorchu.calcite.quickstart.bean;

import java.util.Objects;

/**
 * 代码参考 apache info.victorchu.calcite [org.apache.info.victorchu.calcite.test.schemata.hr.Location].
 * @Description: 位置
 * @Date:2021/12/20 3:49 下午
 * @Author:victorchutian
 */
public class  Location {
    public final int x;
    public final int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override public String toString() {
        return "Location [x: " + x + ", y: " + y + "]";
    }

    @Override public boolean equals(Object obj) {
        return obj == this
                || obj instanceof Location
                && x == ((Location) obj).x
                && y == ((Location) obj).y;
    }

    @Override public int hashCode() {
        return Objects.hash(x, y);
    }
}
