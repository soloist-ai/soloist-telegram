package com.soloist.telegram.model.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.babyfish.jimmer.sql.TypedTuple;

@TypedTuple
@Getter
@RequiredArgsConstructor
public class UserFeedbackCount {

  private final long userCount;
  private final long feedbackCount;

  public static UserFeedbackCount empty() {
    return new UserFeedbackCount(0, 0);
  }
}
