package com.soloist.telegram.model.entity.user.state.task;

import com.soloist.proto.player.TaskTopic;
import com.soloist.telegram.keyboard.Keyboard;
import com.soloist.telegram.localization.LocalizationCode;
import com.soloist.telegram.localization.StateCode;
import com.soloist.telegram.model.entity.user.state.BotSessionState;
import java.util.List;

public record DeprecateTasksByTopicConfirmationState(TaskTopic taskTopic)
    implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return StateCode.TASKS_DEPRECATE_BY_TOPIC_CONFIRMATION;
  }

  @Override
  public List<Object> onEnterMessageParams() {
    return List.of(taskTopic);
  }

  @Override
  public Keyboard onEnterMessageKeyboard() {
    return Keyboard.DEPRECATE_TASKS_BY_TOPIC_CONFIRMATION;
  }

  @Override
  public LocalizationCode onExitMessageCode() {
    return StateCode.TASKS_DEPRECATE_EXIT;
  }
}
