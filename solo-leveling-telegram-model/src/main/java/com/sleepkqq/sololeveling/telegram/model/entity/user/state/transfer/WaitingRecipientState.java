package com.sleepkqq.sololeveling.telegram.model.entity.user.state.transfer;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;

@JsonTypeName("WaitingRecipientState")
public record WaitingRecipientState(int amount) implements BotSessionState {

  @Override
  public String message() {
    return "Введите тэг получателя";
  }

  @Override
  public BotSessionState nextState(String userInput) {
    return new WaitingConfirmationState(amount, userInput);
  }
}
