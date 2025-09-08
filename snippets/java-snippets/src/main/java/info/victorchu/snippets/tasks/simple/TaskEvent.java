package info.victorchu.snippets.tasks.simple;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public interface TaskEvent{
    @Getter
    @Setter
    @AllArgsConstructor
    class TaskStartEvent implements TaskEvent{
        private Task task;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    class TaskSuccessEvent implements TaskEvent{
        private Task task;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    class TaskAbortEvent implements TaskEvent{
        private Task task;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    class TaskFailedEvent implements TaskEvent{
        private Task task;
        private Throwable t;
    }
}
