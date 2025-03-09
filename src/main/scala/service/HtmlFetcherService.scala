package service

import cats.effect.Async
import org.http4s.client.Client
import org.typelevel.log4cats.slf4j.Slf4jLogger
import scala.util.control.NonFatal
import cats.syntax.all._

// Сервис для загрузки HTML по URL
trait HtmlFetcherService[F[_]] {
  def fetchHtml(url: String): F[String]
}

object HtmlFetcherService {
  def apply[F[_]: Async](client: Client[F]): HtmlFetcherService[F] =
    new HtmlFetcherServiceImpl(client)

  private final class HtmlFetcherServiceImpl[F[_]: Async](client: Client[F]) extends HtmlFetcherService[F] {
    private val logger = Slf4jLogger.getLogger[F]

    override def fetchHtml(url: String): F[String] = {
      logger.info(s"Fetching HTML from: $url") *>
        client.expect[String](url).handleErrorWith { e =>
          logger.error(e)(s"Failed to fetch HTML from: $url") *>
            Async[F].raiseError(e)
        }
    }
  }
}
