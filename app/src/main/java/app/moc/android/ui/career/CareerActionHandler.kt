package app.moc.android.ui.career

interface CareerManageActionHandler {
    fun navigateToCareerDetail()
}

interface CareerManageItemActionHandler {
    fun showCareerCheck(uiModel: CareerItemUIModel)
    fun navigateToCareerHistory(uiModel: CareerItemUIModel) // 커리어 달력
}

interface CareerDetailActionHandler {
    fun showCancelDialog()
    fun dismiss()
    fun showColorDialog()
    fun showStartDatePicker()
    fun showEndDatePicker()
    fun register()
}

interface CareerHistoryActionHandler {
    fun modify()
    fun delete()
    fun setDone()
}

interface ColorActionHandler {
    fun chooseColor()
}