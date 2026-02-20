package com.sleepkqq.sololeveling.telegram.model.entity.user.state.feedback;

import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;

public record FeedbackMessageState() implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return LocalizationCode.STATE_FEEDBACK_ENTER;
  }

  @Override
  public LocalizationCode onExitMessageCode() {
    return LocalizationCode.STATE_FEEDBACK_EXIT;
  }
}
