package com.soloist.telegram.model.interceptor;

import com.soloist.telegram.model.entity.Auditable;
import com.soloist.telegram.model.entity.AuditableDraft;
import com.soloist.telegram.model.entity.AuditableProps;
import java.time.Instant;
import org.babyfish.jimmer.ImmutableObjects;
import org.babyfish.jimmer.sql.DraftInterceptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
public class AuditableDraftInterceptor implements DraftInterceptor<Auditable, AuditableDraft> {

  @Override
  public void beforeSave(@NotNull AuditableDraft draft, @Nullable Auditable original) {
    if (original == null) {
      if (!ImmutableObjects.isLoaded(draft, AuditableProps.CREATED_AT)) {
        draft.setCreatedAt(Instant.now());
      }
    }
  }
}
