package com.soloist.telegram.model.entity.user.state.newsletter;

import com.soloist.telegram.localization.LocalizationCode;
import com.soloist.telegram.localization.StateCode;
import com.soloist.telegram.model.entity.user.state.BotSessionState;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public record NewsletterPhotoState(
    String name,
    List<LocalizedMessageDto> localizations
) implements BotSessionState {

  private static final String SKIP_PHOTO_PLACEHOLDER = "skip";

  @Override
  public LocalizationCode onEnterMessageCode() {
    return StateCode.NEWSLETTER_PHOTO_ENTER;
  }

  @Override
  public BotSessionState nextState(Message message) {
    if (message.hasText() && message.getText().trim().equalsIgnoreCase(SKIP_PHOTO_PLACEHOLDER)) {
      return new NewsletterDateTimeState(name, localizations, null);
    }

    if (message.hasPhoto()) {
      var fileId = message.getPhoto().getLast().getFileId();
      return new NewsletterDateTimeState(name, localizations, fileId);
    }

    return this;
  }
}
