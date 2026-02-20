package com.sleepkqq.sololeveling.telegram.model.entity.user.state.transfer;

import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public record TransferRecipientState(long amount) implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return LocalizationCode.STATE_TRANSFER_RECIPIENT;
  }

  @Override
  public BotSessionState nextState(Message message) {
    if (!message.getText().startsWith("@")) {
      return this;
    }

    return new TransferConfirmationState(amount, message.getText().substring(1));
  }
}
