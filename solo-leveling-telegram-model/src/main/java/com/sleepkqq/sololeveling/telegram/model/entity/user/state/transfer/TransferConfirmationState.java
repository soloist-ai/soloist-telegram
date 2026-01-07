package com.sleepkqq.sololeveling.telegram.model.entity.user.state.transfer;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;

import java.util.List;

@JsonTypeName("TransferConfirmationState")
public record TransferConfirmationState(long amount, String recipientUsername)
    implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return LocalizationCode.STATE_TRANSFER_CONFIRMATION;
  }

  @Override
  public List<Object> onEnterMessageParams() {
    return List.of(amount, recipientUsername);
  }

  @Override
  public List<Object> onExitMessageParams() {
    return List.of(amount, recipientUsername);
  }
}
