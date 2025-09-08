package info.victorchu.snippets.tasks.simple;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Stage {
    @Getter
    private final String id;
    @Getter
    private final String desc;
    @Getter
    private final TaskContext context;

    private final Runnable runnable;
    @Getter
    private final List<StageListener> stageListeners = new ArrayList<>();
    /**
     * 当前 stage 的依赖stage
     */
    private final List<Stage> dependencies = new ArrayList<>();
    /**
     * 当前 stage 的被依赖stage
     */
    private final List<Stage> dependents = new ArrayList<>();

    public Stage(String id, Runnable runnable,String desc,TaskContext context) {
        this.id = id;
        this.runnable = runnable;
        this.desc = desc;
        this.context = context;
    }
    public void addDependency(Stage stage) {
        dependencies.add(stage);
        stage.dependents.add(this);
    }

    public void addStageListener(StageListener ... stageListenerArray){
        stageListeners.addAll(Arrays.asList(stageListenerArray));
    }

    public List<Stage> getDependencies() {
        return Collections.unmodifiableList(dependencies);
    }

    public List<Stage> getDependents() {
        return Collections.unmodifiableList(dependents);
    }

    public String printMermaidFlowNode(){
        return String.format("%s[%s]",id,desc);
    }
    @Override
    public String toString(){
        return String.format("[%s](%s)",id,desc);
    }

    public void execute() {
        runnable.run();
    }
}
