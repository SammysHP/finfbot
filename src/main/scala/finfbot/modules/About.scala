import org.pircbotx.PircBotX
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.PrivateMessageEvent

object About extends ListenerAdapter[PircBotX] {
  type Message = PrivateMessageEvent[PircBotX]

  override def onPrivateMessage(event: Message) = {
    (event.getMessage().split(" ").toList) match {
      case "help" :: "about" :: _ => {
        event.respond("about - Infos über den Bot")
      }

      case "about" :: Nil => {
        event.respond("finfbot - der Bot der Fachgruppe Informatik an der Uni Hannover")
        event.respond("Source: https://github.com/SammysHP/finfbot")
        event.respond("Autor: Sven Greiner (SammysHP) <sven@sammyshp.de>")
      }

      case _ => //Nothing to do
    }
  }
}
