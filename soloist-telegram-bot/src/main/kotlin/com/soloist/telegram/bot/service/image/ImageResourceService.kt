package com.soloist.telegram.bot.service.image

import com.soloist.telegram.image.Image
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class ImageResourceService {

	fun getPhotoStream(image: Image): InputStream = ClassPathResource(image.filePath).inputStream
}