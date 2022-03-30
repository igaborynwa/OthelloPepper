package hu.netlife.othellopepper.grpc

import hu.netlife.othellopepper.proto.OthelloPepper
import hu.netlife.othellopepper.proto.OthelloServiceGrpcKt
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
}