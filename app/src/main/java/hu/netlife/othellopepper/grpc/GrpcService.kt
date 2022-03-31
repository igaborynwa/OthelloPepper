package hu.netlife.othellopepper.grpc

import hu.netlife.othellopepper.proto.OthelloPepper
import hu.netlife.othellopepper.proto.OthelloServiceGrpcKt
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GrpcService @Inject constructor(

): OthelloServiceGrpcKt.OthelloServiceCoroutineImplBase() {
    private val defaultResponse: OthelloPepper.Response by lazy{
        OthelloPepper.Response.newBuilder().setResult("OK").build()
    }

    override suspend fun hello(request: OthelloPepper.Void): OthelloPepper.Response {
        return defaultResponse
    }

    override suspend fun connect(request: OthelloPepper.Player): OthelloPepper.PlayerId {
        return super.connect(request)
    }

    override suspend fun disconnect(request: OthelloPepper.PlayerId): OthelloPepper.Response {
        return super.disconnect(request)
    }

    override suspend fun getState(request: OthelloPepper.Void): OthelloPepper.State {
        return super.getState(request)
    }

    override suspend fun setAction(request: OthelloPepper.Action): OthelloPepper.State {
        return super.setAction(request)
    }

    override fun streamEvents(request: OthelloPepper.Void): Flow<OthelloPepper.Event> {
        return super.streamEvents(request)
    }
}