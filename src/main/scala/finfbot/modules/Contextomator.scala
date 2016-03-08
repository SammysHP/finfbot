import java.net.{HttpURLConnection, URL}

import org.pircbotx.PircBotX
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.{MessageEvent, PrivateMessageEvent}

object Contextomator extends ListenerAdapter[PircBotX] {

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

  override def onMessage(event: MessageEvent[PircBotX]): Unit = {
    val message = event.getMessage.toLowerCase
    val user = event.getUser.getNick

    // Direct interaction with the bot
    if (message startsWith event.getBot.getNick.toLowerCase) {
      if (message contains "ricklink") {
        getRicklink foreach(link => event.getChannel.send.message(link))
      } else {
        // Default fallback
        event.getChannel.send.message("I'm sorry " + user + ", I'm afraid I can't do that...")
      }
      return
    }

    // special autoricklink for rick
    if ((user.toLowerCase contains "rick") && (message contains "ircz.de/p")) {
      getRicklink foreach(link => event.getChannel.send.message("Nope! " + link))
    }
  }

  override def onPrivateMessage(event: PrivateMessageEvent[PircBotX]): Unit = {
    val message = event.getMessage.toLowerCase
    val user = event.getUser.getNick

    // Generic context sensitive actions
    if (message contains "ricklink") {
      getRicklink foreach(link => event.respond(link))
    }
  }
}
