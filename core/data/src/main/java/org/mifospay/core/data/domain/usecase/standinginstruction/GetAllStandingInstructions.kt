package org.mifospay.core.data.domain.usecase.standinginstruction

import org.mifospay.core.data.base.UseCase
import com.mifospay.core.model.entity.Page
import com.mifospay.core.model.entity.standinginstruction.StandingInstruction
import org.mifospay.core.data.fineract.repository.FineractRepository
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Devansh 08/06/2020
 */

class GetAllStandingInstructions @Inject constructor(private val apiRepository: FineractRepository) :
        UseCase<GetAllStandingInstructions.RequestValues, GetAllStandingInstructions.ResponseValue>() {


    override fun executeUseCase(requestValues: RequestValues) {
        apiRepository.getAllStandingInstructions(requestValues.clientId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Page<StandingInstruction>>() {

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.message?.let { useCaseCallback.onError(it) }
                    }


                    override fun onNext(standingInstructionPage: Page<StandingInstruction>)
                            = useCaseCallback.onSuccess(ResponseValue(standingInstructionPage.pageItems))

                })
    }

    class RequestValues(val clientId: Long) : UseCase.RequestValues

    class ResponseValue(val standingInstructionsList: List<StandingInstruction>)
        : UseCase.ResponseValue

}