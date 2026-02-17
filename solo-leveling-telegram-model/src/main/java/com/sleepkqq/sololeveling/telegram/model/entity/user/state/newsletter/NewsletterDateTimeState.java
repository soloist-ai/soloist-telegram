package com.sleepkqq.sololeveling.telegram.model.entity.user.state.newsletter;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@JsonTypeName("NewsletterDateTimeState")
public record NewsletterDateTimeState(List<LocalizedMessageDto> localizations)
    implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return LocalizationCode.STATE_NEWSLETTER_DATE_TIME_ENTER;
  }

  @Override
  public BotSessionState nextState(String userInput) {
    try {
      var afterFiveMinutes = Instant.now().plus(5, ChronoUnit.MINUTES);
      var userInstant = Instant.parse(userInput);

      if (userInstant.isBefore(afterFiveMinutes)) {
        return this;
      }

      return new NewsletterConfirmationState(localizations, userInstant);

    } catch (Exception e) {
      return this;
    }
  }
}
