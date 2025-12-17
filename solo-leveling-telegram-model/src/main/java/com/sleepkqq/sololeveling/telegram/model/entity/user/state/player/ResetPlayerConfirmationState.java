package com.sleepkqq.sololeveling.telegram.model.entity.user.state.player;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard;
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;
import java.util.Map;

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
  public Map<String, Object> onEnterMessageParams() {
    return Map.of(
        "0", id,
        "1", username != null ? username : "N/A",
        "2", firstName != null ? firstName : "N/A",
        "3", lastName != null ? lastName : "N/A"
    );
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
  public Map<String, Object> onExitMessageParams() {
    return Map.of(
        "0", id,
        "1", username != null ? username : "N/A",
        "2", firstName != null ? firstName : "N/A",
        "3", lastName != null ? lastName : "N/A"
    );
  }
}
