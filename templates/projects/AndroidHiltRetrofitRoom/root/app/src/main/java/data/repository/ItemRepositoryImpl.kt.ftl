package \${packageName}.data.repository

import \${packageName}.data.local.dao.ItemDao
import \${packageName}.data.local.entity.ItemEntity
import \${packageName}.data.remote.api.ApiService
import \${packageName}.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val itemDao: ItemDao
) : ItemRepository {

    override fun getItems(): Flow<List<Item>> {
        return itemDao.getItems().map { entities ->
            entities.map { Item(id = it.id, title = it.title, description = it.description) }
        }
    }

    override suspend fun refreshItems(): Result<Unit> {
        return try {
            val remoteItems = apiService.getItems()
            val entities = remoteItems.map { dto ->
                ItemEntity(id = dto.id, title = dto.title, description = dto.description)
            }
            itemDao.insertItems(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}