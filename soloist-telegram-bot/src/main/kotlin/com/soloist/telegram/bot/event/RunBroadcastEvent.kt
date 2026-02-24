package com.soloist.telegram.bot.event

import com.soloist.telegram.model.entity.broadcast.dto.ScheduledBroadcastView

data class RunBroadcastEvent(
	val broadcast: ScheduledBroadcastView
)
