package \${packageName}.data.repository

import \${packageName}.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun getItems(): Flow<List<Item>>
    suspend fun refreshItems(): Result<Unit>
}