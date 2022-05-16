package app.moc.android.ui.career

import javax.inject.Inject

interface CareerNavigationHandler {
    fun navigateToCareerDetail(uiModel: CareerItemUIModel, navigate: (CareerItemUIModel) -> Unit)
    fun navigateToRegisterCareerDetail(navigate: () -> Unit)
    fun navigateToCareerHistory(uiModel: CareerItemUIModel, navigate: (CareerItemUIModel) -> Unit)
}

class DefaultCareerNavigationHandler @Inject constructor(

) : CareerNavigationHandler {
    override fun navigateToCareerDetail(
        uiModel: CareerItemUIModel,
        navigate: (CareerItemUIModel) -> Unit
    ) {
        navigate(uiModel)
    }

    override fun navigateToRegisterCareerDetail(navigate: () -> Unit) {
        navigate()
    }

    override fun navigateToCareerHistory(
        uiModel: CareerItemUIModel,
        navigate: (CareerItemUIModel) -> Unit
    ) {
        navigate(uiModel)
    }
}

interface CareerManageItemActionHandler {
    fun showCareerCheck(uiModel: CareerItemUIModel)
}

interface CareerDetailActionHandler {
    fun showCancelDialog()
    fun showColorDialog()
    fun showStartDatePicker()
    fun showEndDatePicker()
    fun onConfirmClick()
}

interface CareerHistoryActionHandler {
    fun navigateToModifyCareerDetail()
    fun delete()
    fun setDone()
}

interface ColorActionHandler {
    fun chooseColor()
}