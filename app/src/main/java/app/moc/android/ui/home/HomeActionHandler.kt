package app.moc.android.ui.home

import app.moc.android.ui.career.CareerItemUIModel

interface HomeActionHandler {
    fun navigateToCareerDetail()
    fun navigateToCareerHistory(uiModel: CareerItemUIModel)
}