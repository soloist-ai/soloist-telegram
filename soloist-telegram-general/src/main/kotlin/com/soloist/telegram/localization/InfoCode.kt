package com.soloist.telegram.localization

enum class InfoCode(override val path: String) : LocalizationCode {
	ACTION_CANCELED("info.action.canceled"),
	PLAYER_RESET("info.player.reset"),
	PROOF_APPROVED("info.proof.approved"),
	PROOF_COMPLETED("info.proof.completed"),
	PROOF_REJECTED("info.proof.rejected"),
	PROOF_ERROR("info.proof.error")
}
