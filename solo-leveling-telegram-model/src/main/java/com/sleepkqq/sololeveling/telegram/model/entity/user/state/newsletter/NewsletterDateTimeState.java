package com.sleepkqq.sololeveling.telegram.model.entity.user.state.newsletter;

import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public record NewsletterDateTimeState(
    String name,
    List<LocalizedMessageDto> localizations,
    String fileId
) implements BotSessionState {

  private static final int MINIMUM_MINUTES_RANGE = 1;

  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withZone(ZoneOffset.UTC);

  @Override
  public LocalizationCode onEnterMessageCode() {
    return LocalizationCode.STATE_NEWSLETTER_DATE_TIME_ENTER;
  }

  @Override
  public List<Object> onEnterMessageParams() {
    return List.of(MINIMUM_MINUTES_RANGE);
  }

  @Override
  public BotSessionState nextState(Message message) {
    if (!message.hasText()) {
      return this;
    }

    try {
      var userInstant = LocalDateTime.parse(message.getText(), FORMATTER)
          .toInstant(ZoneOffset.UTC);

      if (userInstant.isBefore(Instant.now().plus(MINIMUM_MINUTES_RANGE, ChronoUnit.MINUTES))) {
        return this;
      }

      return new NewsletterConfirmationState(name, localizations, fileId, userInstant);

    } catch (Exception e) {
      return this;
    }
  }
}
