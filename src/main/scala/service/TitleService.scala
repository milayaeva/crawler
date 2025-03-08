package service

import cats.effect.Async
import cats.syntax.all._
import org.jsoup.Jsoup
import org.http4s.client.Client
import org.typelevel.log4cats.slf4j.Slf4jLogger
import scala.util.control.NonFatal
import model.TitleResult

// Сервис для получения title из HTML страниц
trait TitleService[F[_]] {
  def fetchTitle(url: String): F[(String, TitleResult)]
}

// Реализация сервиса
object TitleService {

  def apply[F[_] : Async](client: Client[F]): TitleService[F] =
    new TitleServiceImpl(client)

  private final class TitleServiceImpl[F[_] : Async](client: Client[F]) extends TitleService[F] {

    private val logger = Slf4jLogger.getLogger[F]
    override def fetchTitle(url: String): F[(String, TitleResult)] = {
      logFetching(url) *>
        fetchHtml(url)
          .flatMap(parseHtml)
          .map(title => url -> TitleResult.success(title))
          .handleErrorWith(e => Async[F].pure(url -> TitleResult.failure(e.getMessage)))
          .flatTap(_ => logCompleted(url))
    }

    // фукция для  HTTP запроса и получения HTML
    private def fetchHtml(url: String): F[String] = {
      client.expect[String](url)
    }

    // функция для парсинга HTML и получения title
    private def parseHtml(html: String): F[String] = {
      Async[F].blocking(parseTitle(html))
    }

    // Вспомогательная функция, парсящая HTML
    private def parseTitle(html: String): String = {
      try Option(Jsoup.parse(html).title()).filter(_.nonEmpty).getOrElse("No Title")
      catch
        case NonFatal(e) => throw new RuntimeException(s"Parse Error: ${e.getMessage}")
    }

    // Логирование начала обработки URL
    private def logFetching(url: String): F[Unit] = {
      logger.info(s"Fetching URL: $url")
    }

    // Логирование завершения обработки URL
    private def logCompleted(url: String): F[Unit] = {
      logger.info(s"Completed URL: $url")
    }
  }
}