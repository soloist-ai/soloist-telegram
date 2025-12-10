package com.sleepkqq.sololeveling.telegram.model.entity.user.state.task;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;
import com.sleepkqq.sololeveling.telegram.task.TaskTopic;

@JsonTypeName("DeprecateTasksByTopicState")
public record DeprecateTasksByTopicState() implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return LocalizationCode.STATE_TASKS_DEPRECATE_BY_TOPIC_ENTER;
  }

  @Override
  public BotSessionState nextState(String userInput) {
    try {
      var taskTopic = TaskTopic.valueOf(userInput);
      return new DeprecateTasksByTopicConfirmationState(taskTopic);

    } catch (IllegalArgumentException e) {
      return this;
    }
  }
}
