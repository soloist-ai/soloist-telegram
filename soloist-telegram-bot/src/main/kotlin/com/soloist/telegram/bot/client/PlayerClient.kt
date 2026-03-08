package com.soloist.telegram.bot.client

import com.soloist.proto.player.PlayerServiceGrpc.PlayerServiceBlockingStub
import com.soloist.proto.player.ResetPlayerRequest
import org.springframework.stereotype.Service

@Service
class PlayerClient(
	private val playerStub: PlayerServiceBlockingStub
) {

	fun resetPlayer(id: Long) {
		playerStub.resetPlayer(ResetPlayerRequest.newBuilder().setPlayerId(id).build())
	}
}