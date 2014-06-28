import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PeriodicRssFetcher(url: String, pollInterval: Long, callback: String => Unit, urlFilter: String => String) extends Thread {
  val dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
  var lastMessageTime: Long = 0;

  override def run() {
    while (true) {
      try {
        val xml = scala.xml.XML.load(new java.net.URL(url))

        val posts = (xml \\ "item")
          .map(x => (x \ "title" text, x \ "link" text, dateFormat.parse(x \ "pubDate" text).getTime))
          .filter(x => x._3 > lastMessageTime)
          .groupBy(x => x._1)
          .map(x => { val y = x._2(0); (y._1, y._2, y._3, x._2.size) })
          .toList
          .sortBy(x => x._3)

        if (lastMessageTime > 0) {
          posts.foreach(x => callback(
              (if (x._4 == 1)
                "Neuer Beitrag"
              else
                ("" + x._4 + " neue Beiträge"))
              + " im Thema \""
              + x._1
              + "\": "
              + urlFilter(x._2)))
        }

        if (posts.size > 0)
          lastMessageTime = posts.last._3
      } catch {
        case e: Throwable => None
      }

      Thread.sleep(pollInterval)
    }
  }
}
