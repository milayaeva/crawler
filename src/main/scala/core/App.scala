package core

import cats.effect.*
import org.http4s.ember.server.EmberServerBuilder
import routes.TitleRoutes
import service.{HttpClient, TitleService}
import com.comcast.ip4s._


object App :
  
  def run[F[_]](implicit async: Async[F], parallel: cats.Parallel[F]): F[Nothing] = {
    
    HttpClient.make[F].use { client => {
      val routes = TitleRoutes.routes(TitleService(client)).orNotFound

      EmberServerBuilder.default[F]
        .withHost(host"localhost")
        .withPort(port"9797")
        .withHttpApp(routes)
        .build
        .useForever
     }
    
    }
  }