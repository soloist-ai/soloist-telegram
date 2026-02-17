package com.sleepkqq.sololeveling.telegram.model.entity.user.state.newsletter;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;

@JsonTypeName("NewsletterMessageState")
public record NewsletterMessageState() implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return LocalizationCode.STATE_NEWSLETTER_MESSAGE_ENTER;
  }

  @Override
  public BotSessionState nextState(String userInput) {
    try {
      return new NewsletterDateTimeState(LocalizedMessageParser.parse(userInput));

    } catch (Exception e) {
      return this;
    }
  }
}
