package com.sleepkqq.sololeveling.telegram.model.entity.user.state.task;

import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard;
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;
import com.sleepkqq.sololeveling.telegram.task.TaskTopic;
import java.util.List;

public record DeprecateTasksByTopicConfirmationState(TaskTopic taskTopic)
    implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return LocalizationCode.STATE_TASKS_DEPRECATE_BY_TOPIC_CONFIRMATION;
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
    return LocalizationCode.STATE_TASKS_DEPRECATE_EXIT;
  }
}
