import org.pircbotx.PircBotX
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.PrivateMessageEvent

object Help extends ListenerAdapter[PircBotX] {
  type Message = PrivateMessageEvent[PircBotX]

  override def onPrivateMessage(event: Message) = {
    (event.getMessage().split(" ").toList: @unchecked) match {
      case "help" :: query => {
        query match {

          case "admin" :: param => {
            event.respond("Syntax: admin password command [parameter ...]")
            param match {
              case "shutdown" :: _ => {
                event.respond("shutdown: quit and exit bot")
              }
              case "setpw" :: _ => {
                event.respond("setpw: set a new password for administration (without spaces)")
              }
              case _ => {
                event.respond("Available commands for admin: shutdown, setpw")
                event.respond("Query 'help admin parameter' for more")
              }
            }
          }

          case _ => {
            event.respond("Syntax: help command [parameter ...]")
            event.respond("Available commands: admin")
          }

        }
      }
    }
  }
}
