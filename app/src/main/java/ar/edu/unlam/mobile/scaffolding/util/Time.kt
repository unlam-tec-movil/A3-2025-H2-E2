package ar.edu.unlam.mobile.scaffolding.util

fun tiempoDePublicacionDelPost(timestamp: Long): String {
    val now = System.currentTimeMillis()

    if (timestamp > now) return "Ahora"

    val diff = now - timestamp

    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 60 -> "Ahora"
        minutes < 60 -> "$minutes min"
        hours < 24 -> "$hours hora(s)"
        days < 7 -> "$days día(s)"
        else -> {
            val date = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            "el ${date.format(java.util.Date(timestamp))}"
        }
    }
}
