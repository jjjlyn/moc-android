package app.moc.android.ui.common

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController

class CancelAlertDialogFragment: CommonTwoButtonDialogFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        leftButton = "취소"
        rightButton = "나가기"
        message = "지금까지 입력하신 정보가 모두 삭제됩니다.\n그래도 나가시겠어요?"
        onLeftClick = {
            dismiss()
        }
        onRightClick = {
            findNavController().navigateUp()
        }
        super.onViewCreated(view, savedInstanceState)
    }
}