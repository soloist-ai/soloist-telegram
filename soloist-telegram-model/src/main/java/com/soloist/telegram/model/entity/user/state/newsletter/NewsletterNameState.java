package com.soloist.telegram.model.entity.user.state.newsletter;

import com.soloist.telegram.localization.LocalizationCode;
import com.soloist.telegram.localization.StateCode;
import com.soloist.telegram.model.entity.localization.enums.MessageLocale;
import com.soloist.telegram.model.entity.user.state.BotSessionState;
import java.util.List;
import one.util.streamex.StreamEx;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public record NewsletterNameState() implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return StateCode.NEWSLETTER_NAME_ENTER;
  }

  @Override
  public BotSessionState nextState(Message message) {
    if (!message.hasText()) {
      return this;
    }

    return new NewsletterMessageState(
        message.getText().trim(),
        StreamEx.of(MessageLocale.values()).toList(),
        List.of()
    );
  }
}
