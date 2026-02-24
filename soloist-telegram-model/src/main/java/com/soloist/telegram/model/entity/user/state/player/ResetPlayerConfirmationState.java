package com.soloist.telegram.model.entity.user.state.player;

import com.soloist.telegram.keyboard.Keyboard;
import com.soloist.telegram.localization.LocalizationCode;
import com.soloist.telegram.localization.StateCode;
import com.soloist.telegram.model.entity.user.state.BotSessionState;
import java.util.List;

public record ResetPlayerConfirmationState(long id) implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return StateCode.RESET_PLAYER_CONFIRMATION;
  }

  @Override
  public List<Object> onEnterMessageParams() {
    return List.of(id);
  }

  @Override
  public Keyboard onEnterMessageKeyboard() {
    return Keyboard.RESET_PLAYER_CONFIRMATION;
  }

  @Override
  public LocalizationCode onExitMessageCode() {
    return StateCode.RESET_PLAYER_EXIT;
  }

  @Override
  public List<Object> onExitMessageParams() {
    return List.of(id);
  }
}
