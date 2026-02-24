package com.soloist.telegram.model.entity.user.state.transfer;

import com.soloist.telegram.localization.LocalizationCode;
import com.soloist.telegram.localization.StateCode;
import com.soloist.telegram.model.entity.user.state.BotSessionState;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public record TransferRecipientState(long amount) implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return StateCode.TRANSFER_RECIPIENT;
  }

  @Override
  public BotSessionState nextState(Message message) {
    if (!message.hasText() || !message.getText().startsWith("@")) {
      return this;
    }

    var text = message.getText();
    var recipient = text.substring(1);
    if (recipient.isEmpty()) {
      return this;
    }

    return new TransferConfirmationState(amount, recipient);
  }
}
