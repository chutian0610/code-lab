package info.victorchu.snippets.tasks.simple;

public interface TaskListener {
    default void onTaskEvent(TaskEvent event){

    }
}
