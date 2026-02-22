package com.sleepkqq.sololeveling.telegram.model.entity.user.state.task;

import com.sleepkqq.sololeveling.proto.player.TaskTopic;
import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard;
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.localization.StateCode;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;
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
