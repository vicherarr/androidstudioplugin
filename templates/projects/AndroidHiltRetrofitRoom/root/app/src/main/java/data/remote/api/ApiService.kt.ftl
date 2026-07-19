package \${packageName}.data.remote.api

import \${packageName}.data.remote.dto.ItemDto
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getItems(): List<ItemDto>
}