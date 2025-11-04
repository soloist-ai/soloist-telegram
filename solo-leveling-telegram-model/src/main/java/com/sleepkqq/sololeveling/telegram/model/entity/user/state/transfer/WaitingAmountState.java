package com.sleepkqq.sololeveling.telegram.model.entity.user.state.transfer;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;

@JsonTypeName("WaitingAmountState")
public record WaitingAmountState() implements BotSessionState {

  @Override
  public String message() {
    return "Введите сумму платежа";
  }

  @Override
  public BotSessionState nextState(String userInput) {
    try {
      return new WaitingRecipientState(Integer.parseInt(userInput));
    } catch (NumberFormatException e) {
      return this;
    }
  }
}
