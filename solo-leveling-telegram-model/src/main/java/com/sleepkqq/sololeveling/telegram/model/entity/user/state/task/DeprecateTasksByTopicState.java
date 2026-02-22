package com.sleepkqq.sololeveling.telegram.model.entity.user.state.task;

import com.sleepkqq.sololeveling.proto.player.TaskTopic;
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.localization.StateCode;
import com.sleepkqq.sololeveling.telegram.localization.Suggestions;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;
import java.util.List;
import one.util.streamex.StreamEx;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public record DeprecateTasksByTopicState() implements BotSessionState {

  private static final List<TaskTopic> AVAILABLE_TOPICS = StreamEx.of(TaskTopic.values())
      .remove(TaskTopic.UNRECOGNIZED::equals)
      .toList();

  @Override
  public LocalizationCode onEnterMessageCode() {
    return StateCode.TASKS_DEPRECATE_BY_TOPIC_ENTER;
  }

  @Override
  public Suggestions<?> onEnterMessageSuggestions() {
    return Suggestions.Companion.of(AVAILABLE_TOPICS, 2);
  }

  @Override
  public BotSessionState nextState(Message message) {
    try {
      var taskTopic = TaskTopic.valueOf(message.getText());
      return new DeprecateTasksByTopicConfirmationState(taskTopic);

    } catch (IllegalArgumentException _) {
      return this;
    }
  }
}
