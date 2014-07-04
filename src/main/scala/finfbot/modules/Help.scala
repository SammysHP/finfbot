import org.pircbotx.PircBotX
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.PrivateMessageEvent

object Help extends ListenerAdapter[PircBotX] {
  type Message = PrivateMessageEvent[PircBotX]

  override def onPrivateMessage(event: Message) = {
    (event.getMessage().split(" ").toList) match {
      case "help" :: Nil => {
        event.respond("Commands: about, logs, mensa, stats")
        event.respond("See \"help command\" for more details.")
      }

      case _ => //Nothing to do
    }
  }
}
