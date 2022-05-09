package app.moc.android.util

inline fun <reified R> tryOrDefault(default: R, block: () -> R): R = try {
    block()
} catch(_: Exception) {
    default
}