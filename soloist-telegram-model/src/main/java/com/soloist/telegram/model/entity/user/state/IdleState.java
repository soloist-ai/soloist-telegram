package com.soloist.telegram.model.entity.user.state;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.soloist.telegram.localization.LocalizationCode;
import com.soloist.telegram.localization.StateCode;

@JsonTypeName("IdleState")
public record IdleState() implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return StateCode.IDLE;
  }
}
