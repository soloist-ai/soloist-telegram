package com.soloist.telegram.model.entity.user.state.transfer;

import com.soloist.telegram.localization.LocalizationCode;
import com.soloist.telegram.localization.StateCode;
import com.soloist.telegram.model.entity.user.state.BotSessionState;

import java.util.List;

public record TransferConfirmationState(long amount, String recipientUsername)
    implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return StateCode.TRANSFER_CONFIRMATION;
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
