package app.moc.android.ui.talk

import app.moc.android.ui.home.MocTalkItemUIModel

interface TalkActionHandler {
    fun navigateToDetail(uiModel: MocTalkItemUIModel)
}