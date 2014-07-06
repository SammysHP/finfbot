import java.net.{HttpURLConnection, URL}

import org.pircbotx.PircBotX
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.MessageEvent

object Contextomator extends ListenerAdapter[PircBotX] {
  type Message = MessageEvent[PircBotX]

  def getRicklink(): Option[String] = {
    try {
      val con: HttpURLConnection = new URL("http://ircz.de/random").openConnection().asInstanceOf[HttpURLConnection]
      con.setInstanceFollowRedirects(false)
      con.connect()
      Some(con.getHeaderField("Location").toString)
    } catch {
      case e: Throwable => None
    }
  }

  override def onMessage(event: Message): Unit = {
    if (event.getMessage.toLowerCase contains "ricklink") {
      getRicklink foreach(link => event.getChannel.send.message(link))
    }
  }
}
