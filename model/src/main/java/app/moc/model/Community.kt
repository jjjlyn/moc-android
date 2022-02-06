package app.moc.model

data class Community(
    val boardID: Int,
    val userID: Int,
    val nickName: String,
    val category: Int,
    val business: Int,
    val title: String,
    val content: String,
    val likes: Int,
    val hits: Int,
    val tag: String?,
    val imageTag: String,
    val createDate: Long,
    val modifyDate: Long
)