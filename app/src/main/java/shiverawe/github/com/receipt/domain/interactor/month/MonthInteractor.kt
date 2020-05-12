package shiverawe.github.com.receipt.domain.interactor.month

import retrofit2.HttpException
import shiverawe.github.com.receipt.data.network.utils.isOnline
import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.domain.entity.ReceiptError
import shiverawe.github.com.receipt.domain.entity.ReceiptResult
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.repository.IMonthRepository
import java.lang.Exception
import java.util.concurrent.CancellationException

class MonthInteractor(private val repository: IMonthRepository) : IMonthInteractor {

    override suspend fun getMonthReceipt(dateFrom: Long, dateTo: Long): ReceiptResult<List<ReceiptHeader>> {
        if (!isOnline()) {
            return ReceiptResult(error = ReceiptError(type = ErrorType.OFFLINE))
        }

        return try {
            ReceiptResult(repository.getMonthReceipt(dateFrom, dateTo))
        } catch (e: CancellationException) {
            ReceiptResult(isCancel = true)
        } catch (e: Exception) {
            if (e is HttpException || !isOnline()) {
                // Get db receipts if network error
                try {
                    val dbReceipts = repository.getMonthReceiptFromDb(dateFrom, dateTo)
                    ReceiptResult(dbReceipts, ReceiptError(error = e, type = ErrorType.OFFLINE))
                } catch (e: Exception) {
                    // error while getting data from db. Return error without data
                    ReceiptResult(error = ReceiptError(error = e, type = ErrorType.ERROR))
                }
            } else {
                // return error without data
                ReceiptResult(error = ReceiptError(error = e, type = ErrorType.ERROR))
            }
        }
    }
}