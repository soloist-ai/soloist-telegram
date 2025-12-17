package com.sleepkqq.sololeveling.telegram.model.entity.user.state;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sleepkqq.sololeveling.telegram.keyboard.Keyboard;
import com.sleepkqq.sololeveling.telegram.localization.Localized;
import com.sleepkqq.sololeveling.telegram.localization.LocalizationCode;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.feedback.FeedbackMessageState;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.player.ResetPlayerConfirmationState;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.task.DeprecateAllTasksConfirmationState;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.task.DeprecateTasksByTopicConfirmationState;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.task.DeprecateTasksByTopicState;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.transfer.TransferAmountState;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.transfer.TransferConfirmationState;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.transfer.TransferRecipientState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = IdleState.class, name = "IdleState"),
    @JsonSubTypes.Type(value = TransferAmountState.class, name = "TransferAmountState"),
    @JsonSubTypes.Type(value = TransferRecipientState.class, name = "TransferRecipientState"),
    @JsonSubTypes.Type(value = TransferConfirmationState.class, name = "TransferConfirmationState"),
    @JsonSubTypes.Type(value = FeedbackMessageState.class, name = "FeedbackMessageState"),
    @JsonSubTypes.Type(value = DeprecateTasksByTopicConfirmationState.class, name = "DeprecateTasksByTopicConfirmationState"),
    @JsonSubTypes.Type(value = DeprecateTasksByTopicState.class, name = "DeprecateTasksByTopicState"),
    @JsonSubTypes.Type(value = DeprecateAllTasksConfirmationState.class, name = "DeprecateAllTasksConfirmationState"),
    @JsonSubTypes.Type(value = ResetPlayerConfirmationState.class, name = "ResetPlayerConfirmationState")
})
public interface BotSessionState {

  /**
   * Локализованное сообщение при ВХОДЕ в состояние (с возможной клавиатурой)
   */
  default Localized onEnterLocalized() {
    final BotSessionState self = this;
    return new Localized() {
      @NotNull
      @Override
      public LocalizationCode getLocalizationCode() {
        return self.onEnterMessageCode();
      }

      @NotNull
      @Override
      public Map<String, Object> getParams() {
        return self.onEnterMessageParams();
      }

      @Nullable
      @Override
      public Keyboard getKeyboard() {
        return self.onEnterMessageKeyboard();
      }
    };
  }

  /**
   * Локализованное сообщение при ВЫХОДЕ из состояния (без клавиатуры)
   */
  @Nullable
  default Localized onExitLocalized() {
    LocalizationCode exitCode = onExitMessageCode();
    if (exitCode == null) {
      return null;
    }

    final BotSessionState self = this;
    return new Localized() {
      @NotNull
      @Override
      public LocalizationCode getLocalizationCode() {
        return exitCode;
      }

      @NotNull
      @Override
      public Map<String, Object> getParams() {
        return self.onExitMessageParams();
      }
    };
  }

  /**
   * Локализационный код для сообщения при ВХОДЕ в состояние
   */
  LocalizationCode onEnterMessageCode();

  /**
   * Параметры для сообщения при входе
   */
  default Map<String, Object> onEnterMessageParams() {
    return Map.of();
  }

  /**
   * Клавиатура для сообщения при входе
   */
  @Nullable
  default Keyboard onEnterMessageKeyboard() {
    return null;
  }

  /**
   * Локализационный код для сообщения при ВЫХОДЕ из состояния
   *
   * @return null если не нужно отправлять сообщение при выходе
   */
  @Nullable
  default LocalizationCode onExitMessageCode() {
    return null;
  }

  /**
   * Параметры для сообщения при выходе
   */
  default Map<String, Object> onExitMessageParams() {
    return Map.of();
  }

  /**
   * Обработка пользовательского ввода и переход в следующее состояние
   */
  default BotSessionState nextState(String userInput) {
    return new IdleState();
  }
}
