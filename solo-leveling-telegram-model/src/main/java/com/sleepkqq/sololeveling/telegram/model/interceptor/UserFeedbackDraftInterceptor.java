package com.sleepkqq.sololeveling.telegram.model.interceptor;

import com.sleepkqq.sololeveling.telegram.model.entity.user.UserFeedback;
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserFeedbackDraft;
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserFeedbackProps;
import java.time.Instant;
import org.babyfish.jimmer.ImmutableObjects;
import org.babyfish.jimmer.sql.DraftInterceptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UserFeedbackDraftInterceptor implements
    DraftInterceptor<UserFeedback, UserFeedbackDraft> {

  @Override
  public void beforeSave(@NotNull UserFeedbackDraft draft, @Nullable UserFeedback original) {
    if (original == null) {
      if (!ImmutableObjects.isLoaded(draft, UserFeedbackProps.CREATED_AT)) {
        draft.setCreatedAt(Instant.now());
      }
    }
  }
}
