package com.sleepkqq.sololeveling.telegram.model.entity.user.state.newsletter;

import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard;
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import one.util.streamex.StreamEx;

public record NewsletterConfirmationState(
    String name,
    List<LocalizedMessageDto> localizations,
    String fileId,
    Instant scheduledAt
) implements BotSessionState {

  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withZone(ZoneOffset.UTC);

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
    var formattedLocalizations = StreamEx.of(localizations)
        .map(l -> "\n[%s]: %s".formatted(l.locale(), l.text()))
        .joining();
    var formattedFileId = fileId != null ? "✅" : "❌";
    var formattedDate = FORMATTER.format(scheduledAt) + " (UTC)";

    return List.of(name, formattedLocalizations, formattedFileId, formattedDate);
  }

  @Override
  public LocalizationCode onExitMessageCode() {
    return LocalizationCode.STATE_NEWSLETTER_EXIT;
  }

  @Override
  public List<Object> onExitMessageParams() {
    return List.of(name, FORMATTER.format(scheduledAt) + " (UTC)");
  }
}
