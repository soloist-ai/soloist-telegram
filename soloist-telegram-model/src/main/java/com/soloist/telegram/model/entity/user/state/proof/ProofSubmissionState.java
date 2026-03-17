package com.soloist.telegram.model.entity.user.state.proof;

import com.soloist.telegram.localization.LocalizationCode;
import com.soloist.telegram.localization.StateCode;
import com.soloist.telegram.model.entity.user.state.BotSessionState;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public record ProofSubmissionState(
    String taskId,
    String proofType,
    @Nullable String secretWord
) implements BotSessionState {

  @Override
  public LocalizationCode onEnterMessageCode() {
    return switch (proofType) {
      case "PHOTO" -> StateCode.PROOF_SUBMISSION_PHOTO;
      case "VIDEO" -> StateCode.PROOF_SUBMISSION_VIDEO;
      default -> StateCode.PROOF_SUBMISSION_TEXT;
    };
  }

  @Override
  public List<Object> onEnterMessageParams() {
    if ("VIDEO".equals(proofType) && secretWord != null && !secretWord.isBlank()) {
      return List.of(secretWord);
    }
    return List.of();
  }
}
