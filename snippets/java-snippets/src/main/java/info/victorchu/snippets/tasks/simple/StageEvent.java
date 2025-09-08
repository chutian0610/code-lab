package info.victorchu.snippets.tasks.simple;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public interface StageEvent {
    @Getter
    @Setter
    @AllArgsConstructor
    class StageStartEvent implements StageEvent{
        private Stage stage;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    class StageSuccessEvent implements StageEvent{
        private Stage stage;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    class StageAbortEvent implements StageEvent{
        private Stage stage;
        private Throwable t;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    class StageFailedEvent implements StageEvent{
        private Stage stage;
        private Throwable t;
    }
}
