package info.victorchu.snippets.tasks;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class TaskFactory {
    private final String taskId;
    private final TaskContext context;
    private final ExecutorService executor;
    private final AtomicInteger stageIdSeq = new AtomicInteger(0);
    private final Map<String, Stage> stages = new HashMap<>();
    private final List<Stage> rootStages = new ArrayList<>();
    private final List<TaskListener> listeners = new ArrayList<>();

    public TaskFactory(String taskId, ExecutorService executor,TaskContext context) {
        this.taskId = taskId;
        this.executor = executor;
        this.context = context;
    }

    public TaskFactory addTaskListener(TaskListener listener){
        this.listeners.add(listener);
        return this;
    }

    private String newStageId() {
        int sid = stageIdSeq.incrementAndGet();
        return String.format("%s.%d", taskId, sid);
    }

    public Stage newStage(Runnable runnable,String desc) {
        return new Stage(newStageId(), runnable,desc,context);
    }

    public TaskFactory addStages(Stage... stageArray) {
        for (Stage stage : stageArray) {
            stages.put(stage.getId(), stage);
            if (stage.getDependencies().isEmpty()) {
                // 没有依赖的 stage 就是 root stage
                rootStages.add(stage);
            }
        }
        return this;
    }
    public Task build() {
        if(!validateAcyclic()){
            String graph = printMermaidFlowGraph();
            String errorMsg = String.format("task构建异常, task %s has cycle, graph: %s", taskId, graph);
            throw new RuntimeException(errorMsg);
        }
        return new Task(executor, taskId, new ArrayList<>(stages.values()),listeners,context);
    }

    public String printMermaidFlowGraph(){
        StringBuilder sb = new StringBuilder();
        Set<Stage> visited = new HashSet<>();
        sb.append("flowchart TD\n");
        LinkedList<Stage> recursionStack = new LinkedList<>(rootStages);
        while (!recursionStack.isEmpty()){
            Stage stage = recursionStack.pop();
            if(visited.contains(stage)){
                continue;
            }
            visited.add(stage);
            printMermaidFlowLinesForStage(stage, sb, visited, recursionStack);
        }
        return sb.toString();
    }

    private void printMermaidFlowLinesForStage(Stage stage, StringBuilder sb,Set<Stage> visited, LinkedList<Stage> recursionStack) {
        String curNode= stage.printMermaidFlowNode();
        for (Stage dependent : stage.getDependents()) {
            sb.append(String.format("%s --> %s\n", curNode, dependent.printMermaidFlowNode()));
            recursionStack.push(dependent);
        }
    }

    /**
     * 验证没有循环依赖
     */
    private boolean validateAcyclic() {
        // 已经访问过的节点
        Set<Stage> visited = new HashSet<>();
        // 递归栈
        Set<Stage> recursionStack = new HashSet<>();

        for (Stage stage : rootStages) {
            if (hasCycle(stage, visited, recursionStack)) {
                // 存在环，返回异常
                return false;
            }
        }
        return true;
    }

    /**
     * 递归检查是否有环
     * @param stage
     * @param visited
     * @param recursionStack
     * @return
     */
    private boolean hasCycle(Stage stage, Set<Stage> visited, Set<Stage> recursionStack) {
        if (recursionStack.contains(stage)) {
            return true;
        }

        if (visited.contains(stage)) {
            return false;
        }

        visited.add(stage);
        recursionStack.add(stage);

        for (Stage dependent : stage.getDependents()) {
            if (hasCycle(dependent, visited, recursionStack)) {
                return true;
            }
        }
        recursionStack.remove(stage);
        return false;
    }
}
