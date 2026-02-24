package com.soloist.telegram.model.entity.user.state.transfer;

import com.soloist.telegram.localization.LocalizationCode;
import com.soloist.telegram.localization.StateCode;
import com.soloist.telegram.model.entity.user.state.BotSessionState;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public record TransferAmountState() implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return StateCode.TRANSFER_AMOUNT;
  }

  @Override
  public BotSessionState nextState(Message message) {
    if (!message.hasText()) {
      return this;
    }

    try {
      var amount = Long.parseLong(message.getText());
      if (amount <= 0) {
        return this;
      }

      return new TransferRecipientState(amount);

    } catch (NumberFormatException _) {
      return this;
    }
  }
}
