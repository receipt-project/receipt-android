package shiverawe.github.com.receipt.data.network.datasource.receipt

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.api.Api
import shiverawe.github.com.receipt.data.network.entity.create.CreateRequest
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.data.network.entity.item.ItemRequest
import shiverawe.github.com.receipt.data.network.mapper.IMapperNetwork
import shiverawe.github.com.receipt.domain.entity.dto.Meta
import shiverawe.github.com.receipt.domain.entity.dto.Product
import shiverawe.github.com.receipt.domain.entity.dto.Receipt
import shiverawe.github.com.receipt.domain.entity.dto.Shop

class ReceiptNetwork(
    private val mapper: IMapperNetwork,
    private val api: Api) : IReceiptNetwork {

    private var parameters: Map<String, String>? = null

    override fun getReceipt(options: Map<String, String>): Single<Receipt?> {
        return Single.just(Receipt(0, Shop(0, "", ""), Meta(0, "", "", "", 0.0), ArrayList()))
    }

    override fun getProducts(id: Long): Single<ArrayList<Product>> =
        api.getReceipt(ItemRequest(listOf(id))).map { productResponse ->
            productResponse.map { mapper.toProduct(it) }.toCollection(ArrayList())
        }

    override fun saveReceipt(): Single<CreateResponce> {
        val fn = parameters?.get("fn") ?: ""
        val fp = parameters?.get("fn") ?: ""
        val i = parameters?.get("fn") ?: ""
        val s = parameters?.get("fn") ?: ""
        val t = parameters?.get("fn") ?: ""
        val createRequest = CreateRequest(fn, fp, i, s, t)
        return api.createReceipt(createRequest)
    }
}