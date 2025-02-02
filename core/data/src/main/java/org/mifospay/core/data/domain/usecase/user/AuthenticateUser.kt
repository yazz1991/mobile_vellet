package org.mifospay.core.data.domain.usecase.user

import com.mifospay.core.model.domain.user.User
import com.mifospay.core.model.entity.authentication.AuthenticationPayload
import org.mifospay.core.data.base.UseCase
import org.mifospay.core.data.fineract.repository.FineractRepository
import org.mifospay.core.data.util.Constants
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class AuthenticateUser @Inject constructor(
    private val apiRepository: FineractRepository
) : UseCase<AuthenticateUser.RequestValues, AuthenticateUser.ResponseValue>() {

    override fun executeUseCase(requestValues: RequestValues) {
        apiRepository
            .loginSelf(AuthenticationPayload(requestValues.username, requestValues.password))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<User>() {
                override fun onCompleted() {}
                override fun onError(e: Throwable) {
                    useCaseCallback.onError(Constants.ERROR_LOGGING_IN)
                }

                override fun onNext(user: User) {
                    useCaseCallback.onSuccess(ResponseValue(user))
                }
            })
    }

    data class RequestValues(val username: String, val password: String) : UseCase.RequestValues
    data class ResponseValue(val user: User) : UseCase.ResponseValue
}
