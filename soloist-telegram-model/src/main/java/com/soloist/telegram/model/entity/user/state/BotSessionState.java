package com.soloist.telegram.model.entity.user.state;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.soloist.telegram.keyboard.Keyboard;
import com.soloist.telegram.localization.Localized;
import com.soloist.telegram.localization.LocalizationCode;
import com.soloist.telegram.localization.Suggestions;
import com.soloist.telegram.model.entity.user.state.feedback.FeedbackMessageState;
import com.soloist.telegram.model.entity.user.state.newsletter.NewsletterConfirmationState;
import com.soloist.telegram.model.entity.user.state.newsletter.NewsletterDateTimeState;
import com.soloist.telegram.model.entity.user.state.newsletter.NewsletterMessageState;
import com.soloist.telegram.model.entity.user.state.newsletter.NewsletterNameState;
import com.soloist.telegram.model.entity.user.state.newsletter.NewsletterPhotoState;
import com.soloist.telegram.model.entity.user.state.player.ResetPlayerConfirmationState;
import com.soloist.telegram.model.entity.user.state.player.ResetPlayerIdState;
import com.soloist.telegram.model.entity.user.state.task.DeprecateAllTasksConfirmationState;
import com.soloist.telegram.model.entity.user.state.task.DeprecateTasksByTopicConfirmationState;
import com.soloist.telegram.model.entity.user.state.task.DeprecateTasksByTopicState;
import com.soloist.telegram.model.entity.user.state.transfer.TransferAmountState;
import com.soloist.telegram.model.entity.user.state.transfer.TransferConfirmationState;
import com.soloist.telegram.model.entity.user.state.transfer.TransferRecipientState;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.objects.message.Message;

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
    @JsonSubTypes.Type(value = ResetPlayerIdState.class, name = "ResetPlayerIdState"),
    @JsonSubTypes.Type(value = ResetPlayerConfirmationState.class, name = "ResetPlayerConfirmationState"),
    @JsonSubTypes.Type(value = NewsletterNameState.class, name = "NewsletterNameState"),
    @JsonSubTypes.Type(value = NewsletterMessageState.class, name = "NewsletterMessageState"),
    @JsonSubTypes.Type(value = NewsletterPhotoState.class, name = "NewsletterPhotoState"),
    @JsonSubTypes.Type(value = NewsletterDateTimeState.class, name = "NewsletterDateTimeState"),
    @JsonSubTypes.Type(value = NewsletterConfirmationState.class, name = "NewsletterConfirmationState")
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
      public List<Object> getParams() {
        return self.onEnterMessageParams();
      }

      @Nullable
      @Override
      public Keyboard getKeyboard() {
        return self.onEnterMessageKeyboard();
      }

      @Nullable
      @Override
      public Suggestions<?> getSuggestions() {
        return self.onEnterMessageSuggestions();
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
      public List<Object> getParams() {
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
  default List<Object> onEnterMessageParams() {
    return List.of();
  }

  /**
   * Клавиатура для сообщения при входе
   */
  @Nullable
  default Keyboard onEnterMessageKeyboard() {
    return null;
  }

  @Nullable
  default Suggestions<?> onEnterMessageSuggestions() {
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
  default List<Object> onExitMessageParams() {
    return List.of();
  }

  /**
   * Обработка пользовательского ввода и переход в следующее состояние
   */
  default BotSessionState nextState(Message message) {
    return new IdleState();
  }
}
