package com.sleepkqq.sololeveling.telegram.model.entity.user.state.newsletter;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard;
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;
import java.time.Instant;
import java.util.List;

@JsonTypeName("NewsletterConfirmationState")
public record NewsletterConfirmationState(
    List<LocalizedMessageDto> localizations,
    Instant scheduledAt
) implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return LocalizationCode.STATE_NEWSLETTER_CONFIRMATION;
  }

  @Override
  public Keyboard onEnterMessageKeyboard() {
    return Keyboard.SEND_NEWSLETTER_CONFIRMATION;
  }

  @Override
  public List<Object> onEnterMessageParams() {
    return List.of(localizations, scheduledAt);
  }

  @Override
  public LocalizationCode onExitMessageCode() {
    return LocalizationCode.STATE_NEWSLETTER_EXIT;
  }

  @Override
  public List<Object> onExitMessageParams() {
    return List.of(localizations, scheduledAt);
  }
}
