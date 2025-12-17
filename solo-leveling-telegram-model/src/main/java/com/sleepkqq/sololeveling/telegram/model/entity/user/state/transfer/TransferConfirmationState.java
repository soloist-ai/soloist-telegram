package com.sleepkqq.sololeveling.telegram.model.entity.user.state.transfer;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;

import java.util.Map;

@JsonTypeName("TransferConfirmationState")
public record TransferConfirmationState(long amount, String recipientUsername)
    implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return LocalizationCode.STATE_TRANSFER_CONFIRMATION;
  }

  @Override
  public Map<String, Object> onEnterMessageParams() {
    return Map.of(
        "0", amount,
        "1", recipientUsername
    );
  }

  @Override
  public Map<String, Object> onExitMessageParams() {
    return Map.of(
        "0", amount,
        "1", recipientUsername
    );
  }
}
