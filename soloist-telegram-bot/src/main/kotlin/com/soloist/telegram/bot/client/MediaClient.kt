package com.soloist.telegram.bot.client

import com.google.protobuf.ByteString
import com.soloist.proto.media.MediaServiceGrpc.MediaServiceBlockingStub
import com.soloist.proto.media.UploadAttachmentRequest
import org.springframework.stereotype.Service

@Service
class MediaClient(
	private val mediaStub: MediaServiceBlockingStub
) {

	fun uploadImage(imageBytes: ByteArray, filename: String): String {
		val response = mediaStub.uploadAttachment(
			UploadAttachmentRequest.newBuilder()
				.setData(ByteString.copyFrom(imageBytes))
				.setFilename(filename)
				.setContentType("image/png")
				.build()
		)
		return response.id
	}
}
