import scala.io.Source

import org.pircbotx.PircBotX
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.PrivateMessageEvent

object Mensaplan extends ListenerAdapter[PircBotX] {
  type Message = PrivateMessageEvent[PircBotX]

  override def onPrivateMessage(event: Message) = {
    (event.getMessage().split(" ").toList) match {

      case "help" :: "mensa" :: _ => {
        event.respond("Syntax: mensa [wo]")
        event.respond("wo: hauptmensa (default), contine")
      }

      case "mensa" :: query => {
        val place = query match {
          case "hauptmensa" :: _  => 2
          case "contine" :: _ => 3
          case _ => 2
        }

        val raw = Source.fromURL("http://www.stwh-portal.de/mensa/index.php?wo=" + place + "&wann=1&format=txt").mkString

        for (line <- raw.split('\n') if line.startsWith(">") || line.startsWith("#")) {
          event.respond(line.replaceAll("# |> ", "").replace(" #", ":"))
        }
      }

      case _ => //Nothing to do
    }
  }
}
