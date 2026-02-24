package com.soloist.telegram.model.entity.broadcast.enums;

import org.babyfish.jimmer.sql.EnumItem;

public enum BroadcastStatus {
  @EnumItem(ordinal = 0)
  PENDING,
  @EnumItem(ordinal = 1)
  IN_PROGRESS,
  @EnumItem(ordinal = 2)
  COMPLETED,
  @EnumItem(ordinal = 3)
  FAILED,
  @EnumItem(ordinal = 4)
  CANCELLED
}
