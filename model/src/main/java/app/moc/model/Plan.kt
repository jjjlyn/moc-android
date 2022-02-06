package app.moc.model

data class Plan(
    val id: Int,
    val categoryId: String,
    val title: String,
    val startDate: Long,
    val endDate: Long,
)