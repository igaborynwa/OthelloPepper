package hu.netlife.othellopepper.grpc

import android.util.Log
import io.grpc.Server
import io.grpc.netty.NettyServerBuilder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GrpcServer @Inject constructor(
    private val grpcService: GrpcService,
    private val exceptionInterceptor: ExceptionInterceptor,
    private val permissionInterceptor: PermissionInterceptor
) {
    private var server: Server? = null
    private val serverPort = 5050
    private val TAG = "GrpcServer"

    fun startServer() {
        synchronized(this) {
            if (server == null) {
                server = NettyServerBuilder.forPort(serverPort)
                    .addService(grpcService)
                    .intercept(exceptionInterceptor)
                    .intercept(permissionInterceptor)
                    .build()
                    .start()
                Log.d(TAG, "Server started")
            } else {
                Log.w(TAG, "Server already started")
            }
        }
    }

    fun stopServer() {
        synchronized(this) {
            if (server != null) {
                server!!.shutdownNow()
                server = null
                Log.d(TAG, "Server shutdown called")
            } else {
                Log.w(TAG, "Server not yet initialized, shutdown not possible")
            }
        }
    }
}