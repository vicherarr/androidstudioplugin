package \${packageName}.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("body") val description: String
)