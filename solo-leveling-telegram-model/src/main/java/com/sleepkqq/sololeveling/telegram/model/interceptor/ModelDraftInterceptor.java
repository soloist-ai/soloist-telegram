package com.sleepkqq.sololeveling.telegram.model.interceptor;

import com.sleepkqq.sololeveling.telegram.model.entity.Model;
import com.sleepkqq.sololeveling.telegram.model.entity.ModelDraft;
import com.sleepkqq.sololeveling.telegram.model.entity.ModelProps;
import java.time.Instant;
import org.babyfish.jimmer.ImmutableObjects;
import org.babyfish.jimmer.sql.DraftInterceptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
public class ModelDraftInterceptor implements DraftInterceptor<Model, ModelDraft> {

  @Override
  public void beforeSave(@NotNull ModelDraft draft, @Nullable Model original) {
    var now = Instant.now();
    draft.setUpdatedAt(now);
    if (original == null) {
      if (!ImmutableObjects.isLoaded(draft, ModelProps.CREATED_AT)) {
        draft.setCreatedAt(now);
      }
      if (!ImmutableObjects.isLoaded(draft, ModelProps.VERSION)) {
        draft.setVersion(0);
      }
    }
  }
}
