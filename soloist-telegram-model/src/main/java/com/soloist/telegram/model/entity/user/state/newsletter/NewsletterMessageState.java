package com.soloist.telegram.model.entity.user.state.newsletter;

import com.soloist.telegram.localization.LocalizationCode;
import com.soloist.telegram.localization.StateCode;
import com.soloist.telegram.model.entity.localization.enums.MessageLocale;
import com.soloist.telegram.model.entity.user.state.BotSessionState;
import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public record NewsletterMessageState(
    String name,
    List<MessageLocale> remaining,
    List<LocalizedMessageDto> collected
) implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return StateCode.NEWSLETTER_MESSAGE_ENTER;
  }

  @Override
  public List<Object> onEnterMessageParams() {
    return List.of(remaining.getFirst());
  }

  @Override
  public BotSessionState nextState(Message message) {
    if (!message.hasText()) {
      return this;
    }

    var currentLocale = remaining.getFirst();
    var newCollected = new ArrayList<>(collected);
    newCollected.add(new LocalizedMessageDto(currentLocale, message.getText()));

    var newRemaining = remaining.subList(1, remaining.size());

    if (!newRemaining.isEmpty()) {
      return new NewsletterMessageState(name, List.copyOf(newRemaining), List.copyOf(newCollected));
    }

    return new NewsletterPhotoState(name, List.copyOf(newCollected));
  }
}
