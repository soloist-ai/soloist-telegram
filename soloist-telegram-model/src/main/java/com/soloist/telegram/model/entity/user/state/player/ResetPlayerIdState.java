package com.soloist.telegram.model.entity.user.state.player;

import com.soloist.telegram.localization.LocalizationCode;
import com.soloist.telegram.localization.StateCode;
import com.soloist.telegram.localization.Suggestions;
import com.soloist.telegram.model.entity.user.state.BotSessionState;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public record ResetPlayerIdState() implements BotSessionState {

  private static final String CURRENT_USER_ID_PLACEHOLDER = "me";

  @Override
  public LocalizationCode onEnterMessageCode() {
    return StateCode.RESET_PLAYER_ENTER;
  }

  @Override
  public Suggestions<?> onEnterMessageSuggestions() {
    return Suggestions.Companion.of(List.of(CURRENT_USER_ID_PLACEHOLDER), 1);
  }

  @Override
  public BotSessionState nextState(Message message) {
    if (!message.hasText()) {
      return this;
    }

    var text = message.getText().trim();

    if (text.equalsIgnoreCase(CURRENT_USER_ID_PLACEHOLDER)) {
      return new ResetPlayerConfirmationState(message.getFrom().getId());
    }

    try {
      var userId = Long.parseLong(text);
      return new ResetPlayerConfirmationState(userId);

    } catch (NumberFormatException _) {
      return this;
    }
  }
}
