package com.sleepkqq.sololeveling.telegram.model.entity.user.state;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("IdleState")
public record IdleState() implements BotSessionState {

  @Override
  public String message() {
    return "Для списка команд введи /commands";
  }
}
