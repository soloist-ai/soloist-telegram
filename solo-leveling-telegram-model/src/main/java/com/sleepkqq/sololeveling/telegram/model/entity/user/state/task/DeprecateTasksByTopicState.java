package com.sleepkqq.sololeveling.telegram.model.entity.user.state.task;

import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;
import com.sleepkqq.sololeveling.telegram.task.TaskTopic;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public record DeprecateTasksByTopicState() implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return LocalizationCode.STATE_TASKS_DEPRECATE_BY_TOPIC_ENTER;
  }

  @Override
  public BotSessionState nextState(Message message) {
    try {
      var taskTopic = TaskTopic.valueOf(message.getText());
      return new DeprecateTasksByTopicConfirmationState(taskTopic);

    } catch (IllegalArgumentException e) {
      return this;
    }
  }
}
