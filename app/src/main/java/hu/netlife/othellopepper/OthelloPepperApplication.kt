package hu.netlife.othellopepper

import android.app.Activity
import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import hu.netlife.othellopepper.grpc.GrpcServer
import javax.inject.Inject

@HiltAndroidApp
class OthelloPepperApplication: Application() {
    var currentActivity: Activity? = null

    @Inject
    lateinit var grpcServer: GrpcServer

    override fun onCreate() {
        super.onCreate()
        grpcServer.startServer()
    }
}