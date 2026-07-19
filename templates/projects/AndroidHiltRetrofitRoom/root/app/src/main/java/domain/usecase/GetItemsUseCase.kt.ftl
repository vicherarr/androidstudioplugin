package \${packageName}.domain.usecase

import \${packageName}.data.repository.ItemRepository
import \${packageName}.domain.model.Item
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemsUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    operator fun invoke(): Flow<List<Item>> {
        return repository.getItems()
    }

    suspend fun refresh(): Result<Unit> {
        return repository.refreshItems()
    }
}