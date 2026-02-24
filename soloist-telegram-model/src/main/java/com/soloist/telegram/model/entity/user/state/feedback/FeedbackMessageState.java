package com.soloist.telegram.model.entity.user.state.feedback;

import com.soloist.telegram.localization.LocalizationCode;
import com.soloist.telegram.localization.StateCode;
import com.soloist.telegram.model.entity.user.state.BotSessionState;

public record FeedbackMessageState() implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return StateCode.FEEDBACK_ENTER;
  }

  @Override
  public LocalizationCode onExitMessageCode() {
    return StateCode.FEEDBACK_EXIT;
  }
}
