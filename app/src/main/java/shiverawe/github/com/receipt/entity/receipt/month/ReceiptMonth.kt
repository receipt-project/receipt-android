package shiverawe.github.com.receipt.entity.receipt.month

import shiverawe.github.com.receipt.entity.receipt.base.Meta
import shiverawe.github.com.receipt.entity.receipt.base.Shop

data class ReceiptMonth(var receiptId: Long,
                        val shop: Shop,
                        val meta: Meta,
                        var viewType: Int = 0,
                        var separatorIsVisible: Boolean = true,
                        var isTopInDay: Boolean = true)