package info.victorchu.mermaidjsjava.flow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlowChartGraphConfig {
    private Direction direction;
    public static FlowChartGraphConfig defaultConfig = getDefaultConfig();

    private static FlowChartGraphConfig getDefaultConfig() {
        return FlowChartGraphConfig.builder().direction(Direction.LR).build();
    }
}
