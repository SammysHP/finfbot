import org.pircbotx.PircBotX
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.{ActionEvent, JoinEvent, MessageEvent, NickChangeEvent, PartEvent, QuitEvent}

import java.io.FileWriter
import java.util.Date

case class Logger(logDirectoryPath: String) extends ListenerAdapter[PircBotX] {
  val fileDate = new java.text.SimpleDateFormat("yyyy-MM-dd")
  val logDate = new java.text.SimpleDateFormat("HH:mm:ss")

  override def onMessage(event: MessageEvent[PircBotX]) = {
    logMessage("<" + event.getUser.getNick + "> " + event.getMessage)
  }

  override def onAction(event: ActionEvent[PircBotX]) = {
    logMessage("* " + event.getUser.getNick + " " + event.getMessage)
  }

  override def onJoin(event: JoinEvent[PircBotX]) = {
    val user = event.getUser
    logMessage("--> " + user.getNick + " (" + user.getLogin + "@" + user.getHostmask + ") has joined " + event.getChannel.getName)
  }

  override def onPart(event: PartEvent[PircBotX]) = {
    logMessage("<-- " + event.getUser.getNick + " has left "+ event.getChannel.getName + " (" + event.getReason + ")")
  }

  override def onQuit(event: QuitEvent[PircBotX]) = {
    logMessage("<-- " + event.getUser.getNick + " has quit (" + event.getReason + ")")
  }

  override def onNickChange(event: NickChangeEvent[PircBotX]) = {
    logMessage("--- " + event.getOldNick + " is now known as " + event.getNewNick)
  }

  private def logMessage(message: String) = {
    try {
      val file = new FileWriter(logDirectoryPath + "/" + fileDate.format(new Date()) + ".txt", true)
      file.write(logDate.format(new Date()) + " " + message + "\n")
      file.close
    } catch {
      case e: Throwable => // Do nothing
    }
  }
}
