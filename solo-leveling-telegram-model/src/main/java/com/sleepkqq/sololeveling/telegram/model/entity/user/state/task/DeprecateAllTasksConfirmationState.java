package com.sleepkqq.sololeveling.telegram.model.entity.user.state.task;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard;
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;

@JsonTypeName("DeprecateAllTasksConfirmationState")
public record DeprecateAllTasksConfirmationState() implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return LocalizationCode.STATE_TASKS_DEPRECATE_ALL_CONFIRMATION;
  }

  @Override
  public Keyboard onEnterMessageKeyboard() {
    return Keyboard.DEPRECATE_ALL_TASKS_CONFIRMATION;
  }

  @Override
  public LocalizationCode onExitMessageCode() {
    return LocalizationCode.STATE_TASKS_DEPRECATE_EXIT;
  }
}
