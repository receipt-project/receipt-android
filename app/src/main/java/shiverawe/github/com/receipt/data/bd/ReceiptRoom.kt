package shiverawe.github.com.receipt.data.bd

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Transaction
import shiverawe.github.com.receipt.data.bd.product.ProductDao
import shiverawe.github.com.receipt.data.bd.product.ProductEntity
import shiverawe.github.com.receipt.data.bd.receipt.ReceiptDao
import shiverawe.github.com.receipt.data.bd.receipt.ReceiptEntity
import shiverawe.github.com.receipt.data.bd.utils.MapperDb
import shiverawe.github.com.receipt.entity.receipt.base.Receipt
import shiverawe.github.com.receipt.ui.App
import kotlin.collections.ArrayList

@Database(entities = [ReceiptEntity::class, ProductEntity::class], version = 1)
abstract class ReceiptRoom : RoomDatabase() {

    abstract fun receiptDao(): ReceiptDao
    abstract fun productDao(): ProductDao
    val mapper = MapperDb()

    companion object {
        private var instance: ReceiptRoom? = null
        fun getDb(): ReceiptRoom {
            if (instance == null) {
                synchronized(ReceiptRoom::class) {
                    instance = Room.databaseBuilder(App.appContext,
                            ReceiptRoom::class.java,
                            "receipt.db")
                            .build()
                }
            }
            return instance!!
        }
    }

    @Transaction
    fun saveReceipts(receipts: ArrayList<Receipt>): List<Long> {
        val receiptsDb = receipts.map { it -> mapper.receiptToDb(it) }
        val savedIds = receiptDao().addReceipts(receiptsDb)
        val savedProducts = ArrayList<ProductEntity>()
        for (receiptIndex in 0 until receipts.size) {
            receipts[receiptIndex].items.forEach { product ->
                savedProducts.add(mapper.productToDb(product, savedIds[receiptIndex]))
            }
        }
        productDao().addProducts(savedProducts)
        return savedIds
    }

    @Transaction
    fun getReceiptsWithProducts(dataFrom: Long, dataTo: Long): ArrayList<Receipt> {
        val receipts = ArrayList<Receipt>()
        var receiptsDb = receiptDao().getMonthReceipts(dataFrom, dataTo)
        receiptsDb = receiptsDb.sortedByDescending { it.date }
        val receiptIds = receiptsDb.map { it.id }.toTypedArray()
        val productsDb = productDao().getProductsForReceipts(receiptIds).sortedBy { it.receiptId }
        receiptsDb.forEach { receiptDb ->
            receipts.add(mapper.dbToReceipt(receiptDb, productsDb.filter { it.receiptId == receiptDb.id }))
        }
        return receipts
    }
}