package info.victorchu.mermaidjsjava;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlowChartGraphConfig {

    private  Direction direction;

    public static enum Direction {
        /**
         * top-down same as TB
         */
        TD,
        /**
         * top to bottom
         */
        TB,
        /**
         * bottom to top
         */
        BT,
        /**
         * right to left
         */
        RL,
        /**
         * left to right
         */
        LR;
    }


}
