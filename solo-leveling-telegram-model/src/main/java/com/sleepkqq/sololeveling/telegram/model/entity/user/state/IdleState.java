package com.sleepkqq.sololeveling.telegram.model.entity.user.state;

public record IdleState() implements BotSessionState {

  @Override
  public String message() {
    return "Для списка команд введи /commands";
  }
}
