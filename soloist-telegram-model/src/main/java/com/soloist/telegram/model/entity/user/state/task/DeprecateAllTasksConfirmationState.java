package com.soloist.telegram.model.entity.user.state.task;

import com.soloist.telegram.keyboard.Keyboard;
import com.soloist.telegram.localization.LocalizationCode;
import com.soloist.telegram.localization.StateCode;
import com.soloist.telegram.model.entity.user.state.BotSessionState;

public record DeprecateAllTasksConfirmationState() implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return StateCode.TASKS_DEPRECATE_ALL_CONFIRMATION;
  }

  @Override
  public Keyboard onEnterMessageKeyboard() {
    return Keyboard.DEPRECATE_ALL_TASKS_CONFIRMATION;
  }

  @Override
  public LocalizationCode onExitMessageCode() {
    return StateCode.TASKS_DEPRECATE_EXIT;
  }
}
