package hu.netlife.othellopepper.grpc

import io.grpc.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExceptionInterceptor @Inject constructor() : ServerInterceptor {
    override fun <ReqT : Any?, RespT : Any?> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata?,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        return next.startCall(object : ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
            override fun close(status: Status, trailers: Metadata?) {
                status.cause?.printStackTrace()
                if (!status.isOk) {
                    super.close(status.withDescription(status.cause?.message ?: "Unknown Exception"), trailers)
                } else {
                    super.close(status, trailers)
                }
            }
        }, headers)
    }
}