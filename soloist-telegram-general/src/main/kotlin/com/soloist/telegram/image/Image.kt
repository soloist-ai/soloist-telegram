package com.soloist.telegram.image

enum class Image(
	val fileName: String
) {
	RESET_PLAYER("reset_player.jpg");

	val filePath: String = "images/$fileName"
}