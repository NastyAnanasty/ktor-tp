package tp

import org.joda.time.DateTime
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

fun Timestamp.toStringExt(): String {
    val date = DateTime(this.time)
    return date.toString()
}