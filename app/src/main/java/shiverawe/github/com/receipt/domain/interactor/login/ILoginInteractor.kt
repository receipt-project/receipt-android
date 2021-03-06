package shiverawe.github.com.receipt.domain.interactor.login

import shiverawe.github.com.receipt.domain.entity.BaseResult

interface ILoginInteractor {

    suspend fun login(phone: String, password: String): BaseResult<Boolean>

    suspend fun resetPassword(phone: String): BaseResult<Boolean>
}