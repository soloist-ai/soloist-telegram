package com.sleepkqq.sololeveling.telegram.model.entity.user.state.transfer;

import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public record TransferAmountState() implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return LocalizationCode.STATE_TRANSFER_AMOUNT;
  }

  @Override
  public BotSessionState nextState(Message message) {
    try {
      var amount = Long.parseLong(message.getText());
      if (amount <= 0) {
        return this;
      }
      return new TransferRecipientState(amount);
    } catch (NumberFormatException e) {
      return this;
    }
  }
}
