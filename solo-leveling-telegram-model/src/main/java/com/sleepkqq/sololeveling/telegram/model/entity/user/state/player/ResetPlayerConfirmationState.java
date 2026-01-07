package com.sleepkqq.sololeveling.telegram.model.entity.user.state.player;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard;
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;
import java.util.List;

@JsonTypeName("ResetPlayerConfirmationState")
public record ResetPlayerConfirmationState(
    long id,
    String username,
    String firstName,
    String lastName
) implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return LocalizationCode.STATE_RESET_PLAYER_ENTER;
  }

  @Override
  public List<Object> onEnterMessageParams() {
    return List.of(id, username, firstName, lastName);
  }

  @Override
  public Keyboard onEnterMessageKeyboard() {
    return Keyboard.RESET_PLAYER_CONFIRMATION;
  }

  @Override
  public LocalizationCode onExitMessageCode() {
    return LocalizationCode.STATE_RESET_PLAYER_EXIT;
  }

  @Override
  public List<Object> onExitMessageParams() {
    return List.of(id, username, firstName, lastName);
  }
}
