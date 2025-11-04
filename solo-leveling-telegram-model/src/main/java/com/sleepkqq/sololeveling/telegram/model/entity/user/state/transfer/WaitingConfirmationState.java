package com.sleepkqq.sololeveling.telegram.model.entity.user.state.transfer;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;

@JsonTypeName("WaitingConfirmationState")
public record WaitingConfirmationState(int amount, String recipientUsername)
    implements BotSessionState {

  @Override
  public String message() {
    return String.format("Получатель: %s\nСумма: %d\nВcё правильно?", recipientUsername, amount);
  }
}
