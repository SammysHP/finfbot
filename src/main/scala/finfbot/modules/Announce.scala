import org.pircbotx.PircBotX

object Announce {
  def start(url: String, interval: Int, channel: String, bot: PircBotX, user: String = "", key: String = "") {
    // URL shortener
    val urlFilter = (user, key) match {
      case (u: String, k: String) if (!u.isEmpty && !k.isEmpty) => {
        val urlShortener = new BitlyUrlShortener(u, k)
        (s: String) => { urlShortener.shorten(s) }
      }
      case _ => {
        (s: String) => s
      }
    }

    // Announce
    new PeriodicRssFetcher(
        url,
        interval * 1000,
        (s: String) => { bot.sendIRC().message(channel, s) },
        urlFilter
      ).start()
  }
}
