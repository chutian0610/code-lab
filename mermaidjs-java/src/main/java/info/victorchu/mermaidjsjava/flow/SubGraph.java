package info.victorchu.mermaidjsjava.flow;

import info.victorchu.mermaidjsjava.Node;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.List;

import static info.victorchu.mermaidjsjava.Constant.new_line;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubGraph implements Node {
    @NonNull
    private String id;
    private String name;
    @Builder.Default
    private SubGraphConfig config = SubGraphConfig.defaultConfig;
    private List<FlowLink> links;
    private List<SubGraph> subGraphs;

    @Override
    public String drawNode() {
        StringBuilder sb = new StringBuilder();
        sb.append("subgraph ").append(getId());
        if(name != null && !name.isEmpty()){
            sb.append(" [").append(name).append("]");
        }
        sb.append(new_line);

        if(config != null && config.getDirection() != null){
            sb.append("direction ").append(config.getDirection().name()).append(new_line);
        }

        // subgraph body
        if(subGraphs!=null) {
            for (SubGraph subGraph : subGraphs) {
                sb.append(subGraph.drawNode()).append(new_line);
            }
        }
        if(links!= null) {
            for (FlowLink link : links) {
                sb.append(link.drawLink()).append(new_line);
            }
        }

        sb.append("end").append(new_line);
        return sb.toString();
    }
}
