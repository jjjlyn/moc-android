package app.moc.android.ui.career.history

import androidx.appcompat.widget.Toolbar
import app.moc.android.R
import app.moc.android.ui.career.CareerHistoryActionHandler

fun Toolbar.setupCareerHistoryMenuItem(
    actionHandler: CareerHistoryActionHandler
) {
    inflateMenu(R.menu.career_history_menu)
    val modify = menu.findItem(R.id.action_modify) ?: return
    val delete = menu.findItem(R.id.action_delete) ?: return
    val done = menu.findItem(R.id.action_done) ?: return
    modify.setOnMenuItemClickListener {
        actionHandler.modify()
        true
    }
    delete.setOnMenuItemClickListener {
        actionHandler.delete()
        true
    }
    done.setOnMenuItemClickListener {
        actionHandler.setDone()
        true
    }
}