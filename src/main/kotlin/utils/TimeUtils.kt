package utils

import java.text.SimpleDateFormat
import java.util.*

public fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

public fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}