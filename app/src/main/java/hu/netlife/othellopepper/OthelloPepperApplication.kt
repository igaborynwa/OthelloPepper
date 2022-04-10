package hu.netlife.othellopepper

import android.app.Activity
import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.HiltAndroidApp
import hu.netlife.othellopepper.grpc.GrpcServer
import javax.inject.Inject

@HiltAndroidApp
class OthelloPepperApplication: Application() {
    var currentActivity: AppCompatActivity? = null

    @Inject
    lateinit var grpcServer: GrpcServer

    override fun onCreate() {
        super.onCreate()
        grpcServer.startServer()
    }
}