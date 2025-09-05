package info.victorchu.snippets.tasks;

public interface StageListener {
    default void onStageEvent(StageEvent event){
    }
}
