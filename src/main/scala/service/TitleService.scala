package service

import cats.effect.Async
import cats.syntax.all._
import org.typelevel.log4cats.slf4j.Slf4jLogger
import scala.util.control.NonFatal
import model.TitleResult
import org.jsoup.Jsoup

// Сервис для извлечения title из HTML страниц
trait TitleService[F[_]] {
  def extractTitle(url: String): F[TitleResult]
}

object TitleService {
  def apply[F[_] : Async](htmlFetcher: HtmlFetcherService[F]): TitleService[F] =
    new TitleServiceImpl(htmlFetcher)

  private final class TitleServiceImpl[F[_] : Async](htmlFetcher: HtmlFetcherService[F]) extends TitleService[F] {

    private val logger = Slf4jLogger.getLogger[F]

    override def extractTitle(url: String): F[TitleResult]  = {
      logger.info(s"Extracting title from: $url") *>
        htmlFetcher.fetchHtml(url) // Получаем HTML через отдельный сервис
          .flatMap(parseHtml)
          .map(title => TitleResult(url, true, title))
          .handleErrorWith { e =>
            logger.error(e)(s"Failed to extract title from: $url") *>
              Async[F].pure(TitleResult(url, false, e.getMessage))
          }
          .flatTap(_ => logger.info(s"Completed extracting title from: $url"))
    }

    // Фукция для парсинга HTML и получения title
    private def parseHtml(html: String): F[String] = {
      Async[F].blocking(extractTitleFromHtml(html))
    }

    // Вспомогательная функция, парсящая HTML
    private def extractTitleFromHtml(html: String): String = {
      try Option(Jsoup.parse(html).title()).filter(_.nonEmpty).getOrElse("No Title")
      catch
        case NonFatal(e) => throw new RuntimeException(s"Parse Error: ${e.getMessage}")
    }
  }
}