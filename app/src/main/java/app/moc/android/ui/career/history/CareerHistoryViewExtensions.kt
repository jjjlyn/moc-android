package app.moc.android.ui.career.history

import androidx.appcompat.widget.Toolbar
import app.moc.android.R
import app.moc.android.ui.career.CareerHistoryActionHandler
import app.moc.android.ui.career.CareerItemUIModel

fun Toolbar.setupCareerHistoryMenuItem(
    uiModel: CareerItemUIModel,
    actionHandler: CareerHistoryActionHandler
) {
    inflateMenu(R.menu.career_history_menu)
    if(uiModel.status != "INPRG"){
        menu.removeItem(R.id.action_modify)
        menu.removeItem(R.id.action_done)
    } else {
        val modify = menu.findItem(R.id.action_modify) ?: return
        modify.setOnMenuItemClickListener {
            actionHandler.navigateToModifyCareerDetail()
            true
        }
        val done = menu.findItem(R.id.action_done) ?: return
        done.setOnMenuItemClickListener {
            actionHandler.setDone()
            true
        }
    }
    val delete = menu.findItem(R.id.action_delete) ?: return
    delete.setOnMenuItemClickListener {
        actionHandler.delete()
        true
    }
}