package hu.netlife.othellopepper.grpc

import android.util.Log
import io.grpc.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionInterceptor @Inject constructor(
    private val grpcEventHandler: GrpcEventHandler
) : ServerInterceptor {
    private val TAG = "PermissionInterceptor"
    private var allowed = true

    @Synchronized
    fun setAllowance(allowed: Boolean) {
        this.allowed = allowed
        sendEvent(allowed)
    }

    override fun <ReqT, RespT> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata?,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        return if (allowed) {
            next.startCall(call, headers)
        } else {
            Log.w(TAG, "Grpc call not allowed due to PermissionInterceptor")
            call.close(Status.ABORTED.withDescription("Unable to process request due to gRPC calls are denied."), headers)
            object : ServerCall.Listener<ReqT>() {}
        }
    }

    private fun sendEvent(allowed: Boolean) {
        val event = GrpcEvent("${System.currentTimeMillis()}", MessageTypes.GRPC_CALLS_ALLOWED, "$allowed")
        grpcEventHandler.addEvent(event)
    }
}