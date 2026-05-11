package com.mycom.chan.data.mapper

import android.util.Log
import com.mycom.chan.data.remote.dto.model.MediaDTO
import com.mycom.chan.domain.model.Media
import java.time.Instant

fun MediaDTO.toMedia(): Media {
    return Media(
        url = this.url,
        type = this.type,
        uploadedAt = try {
            Instant.parse(this.uploadAt)
        } catch (e: Exception) {
            Log.e("Media mapping","Parse error", e)
            Instant.now()
        }
    )
}