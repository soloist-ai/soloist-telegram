package com.sleepkqq.sololeveling.telegram.model.entity.user.state;

public sealed interface BotSessionState permits IdleState, TransferFlow {

  String message();
}

