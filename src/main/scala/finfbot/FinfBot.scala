import java.io.File

import org.ini4j.Ini
import org.pircbotx.{PircBotX, Configuration, Channel}

object FinfBot extends App {
  val configFile = "config.ini"
  val ini = new Ini(new File(configFile));

  val ircConfig = ini.get("irc")
  val configuration = new Configuration.Builder()
    .setName(ircConfig.get("name"))
    .setLogin(ircConfig.get("name"))
    .setRealName(ircConfig.get("realname"))
    .setAutoNickChange(true)
    .setAutoReconnect(true)
    .setServerHostname(ircConfig.get("server"))
    .addAutoJoinChannel(ircConfig.get("channel"))
    .setEncoding(java.nio.charset.Charset.forName("UTF-8"))
    .buildConfiguration()
  val bot = new PircBotX(configuration)

  Announce.start(
      ini.get("announce").get("url"),
      ini.get("announce").get("interval").toInt,
      ini.get("irc").get("channel"),
      bot,
      ini.get("bitly").get("user"),
      ini.get("bitly").get("key")
    )

  val listenerManager = bot.getConfiguration().getListenerManager()
  listenerManager.addListener(Mensaplan)
  listenerManager.addListener(About)
  listenerManager.addListener(Stats)
  listenerManager.addListener(Help)
  ini.get("logger").get("directory") match {
    case path: String if !path.isEmpty => listenerManager.addListener(Logger(path))
    case _ => // Do nothing
  }

  while (true) {
    try {
      bot.startBot()
    } catch {
      case e: Throwable => {
        Thread.sleep(10000)
        println("Error! Restarting...")
      }
    }
  }
}
