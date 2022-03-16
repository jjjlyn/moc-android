package app.moc.android.ui.signup

import androidx.annotation.Keep

@Keep
data class KeyWord(
    val id: Int,
    val content: String
)

val keyWords = listOf(
    KeyWord(0, "이직고민"),
    KeyWord(1, "잦은야근"),
    KeyWord(2, "낮은연봉"),
    KeyWord(3, "인간관계"),
    KeyWord(4, "업무과중"),
    KeyWord(5, "적성/진로"),
    KeyWord(6, "육아휴직"),
    KeyWord(7, "창업준비"),
    KeyWord(8, "기타"),
)