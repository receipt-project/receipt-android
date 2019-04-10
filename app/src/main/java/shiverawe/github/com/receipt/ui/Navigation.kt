package shiverawe.github.com.receipt.ui

interface Navigation {
    fun openNavigationDrawable()
    fun closeNavigationDrawable()
    fun openHistory()
    fun openSettings()
    fun openQr()
    fun openReceipt(receiptId: Long)
    fun updateHistory(date: Long)
}