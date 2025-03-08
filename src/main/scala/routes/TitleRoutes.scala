package routes

import cats.effect.Async
import org.http4s.{EntityDecoder, HttpApp, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import org.http4s.ember.client.EmberClientBuilder
import cats.effect.Async
import cats.syntax.all.*
import model.UrlRequest
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import service.TitleService
import org.http4s.circe.CirceEntityCodec._
import io.circe.generic.auto._

object TitleRoutes {
  def routes[F[_]: Async: cats.Parallel](titleService: TitleService[F]): HttpRoutes[F] = {

    // HTTP маршрутов например GET, POST
    val dsl = Http4sDsl[F]
    import dsl._

    // HTTP маршруты
    HttpRoutes.of[F] {

      // Обрабатываем POST-запрос по пути "/title"
      case req @ POST -> Root / "api" / "title" =>
        for {
          // Парсим JSON запрос в модель UrlRequest
          urlRequest <- req.as[UrlRequest]

          // Для каждого URL параллельно получаем title через сервис
          titles <- urlRequest.urls.parTraverse(titleService.fetchTitle)

          // Отвечаем JSON объектом, где ключ — URL, а значение — результат (успех/ошибка и title)
          response <- Ok(titles.toMap)
        } yield response
    }
  }
}
