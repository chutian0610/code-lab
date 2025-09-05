package info.victorchu.snippets.tasks;

public interface TaskListener {
    default void onTaskEvent(TaskEvent event){

    }
}
