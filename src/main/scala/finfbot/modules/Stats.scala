import java.text.SimpleDateFormat
import java.util.Date

import org.pircbotx.PircBotX
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.PrivateMessageEvent

object Stats extends ListenerAdapter[PircBotX] {
  type Message = PrivateMessageEvent[PircBotX]

  class Counter(initialCount: Int = 0) {
    private var count = initialCount
    def value = count
    def ++() = count += 1
    def -- = count -= 1
    override def toString = count.toString
  }
  object Counter {
    implicit def counterToInt(c: Counter) = c.value
  }

  val DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  val started = System.currentTimeMillis()

  def startedString = {
    DATE_FORMAT.format(started)
  }

  def runtime = {
    System.currentTimeMillis() - started
  }

  def runtimeString = {
    val rt = runtime / 1000
    val s = rt % 60
    val m = (rt % 3600) / 60
    val h = (rt % 86400) / 3600
    val d = rt / 86400
    "%dd %dh %dm %ds".format(d, h, m, s)
  }

  val announces = new Counter()

  override def onPrivateMessage(event: Message) = {
    (event.getMessage().split(" ").toList) match {
      case "stats" :: Nil => {
        event.respond("Gestartet:             " + startedString)
        event.respond("Laufzeit:              " + runtimeString)
        event.respond("Angekündigte Beiträge: " + announces)
      }

      case _ => //Nothing to do
    }
  }
}
