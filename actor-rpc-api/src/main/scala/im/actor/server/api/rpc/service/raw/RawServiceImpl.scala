package im.actor.server.api.rpc.service.raw

import akka.actor.ActorSystem
import im.actor.api.rpc._
import im.actor.api.rpc.ClientData
import im.actor.api.rpc.collections.ApiRawValue
import im.actor.api.rpc.raw.{ ResponseRawRequest, RawService }
import im.actor.server.api.rpc.RawApiExtension

import scala.concurrent.{ ExecutionContext, Future }

final class RawServiceImpl(implicit system: ActorSystem) extends RawService {
  import FutureResultRpc._

  override implicit protected val ec: ExecutionContext = system.dispatcher

  private val rawApiExt = RawApiExtension(system)

  override def doHandleRawRequest(service: String, method: String, params: Option[ApiRawValue], clientData: ClientData): Future[HandlerResult[ResponseRawRequest]] =
    (for (result ← fromFutureXor(rawApiExt.handle(service, method, params, clientData)))
      yield ResponseRawRequest(result)).value
}