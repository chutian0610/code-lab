package info.victorchu.tool.mermaidjsjava.flow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubGraphConfig {
    public static SubGraphConfig defaultConfig = getDefaultConfig();

    private static SubGraphConfig getDefaultConfig() {

        return SubGraphConfig.builder().direction(Direction.LR).build();
    }
    private Direction direction = Direction.LR;
}
