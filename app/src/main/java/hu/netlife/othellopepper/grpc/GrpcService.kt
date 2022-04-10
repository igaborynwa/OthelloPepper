package hu.netlife.othellopepper.grpc

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.netlife.othellopepper.OthelloPepperApplication
import hu.netlife.othellopepper.OthelloPepperApplication_GeneratedInjector
import hu.netlife.othellopepper.game.ConnectHelper
import hu.netlife.othellopepper.game.OthelloModel
import hu.netlife.othellopepper.proto.OthelloPepper
import hu.netlife.othellopepper.proto.OthelloServiceGrpcKt
import hu.netlife.othellopepper.view.MainActivity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GrpcService @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val othelloModel: OthelloModel,
    private val connectHelper: ConnectHelper,
    private val grpcEventHandler: GrpcEventHandler

): OthelloServiceGrpcKt.OthelloServiceCoroutineImplBase() {
    private fun getCurrentActivity(): AppCompatActivity? {
        return (appContext as OthelloPepperApplication).currentActivity
    }

    private fun mainActivity(): MainActivity? {
        return getCurrentActivity() as? MainActivity
    }

    private val defaultResponse: OthelloPepper.Response by lazy{
        OthelloPepper.Response.newBuilder().setResult("OK").build()
    }

    override suspend fun hello(request: OthelloPepper.Void): OthelloPepper.Response {
        return defaultResponse
    }

    override suspend fun connect(request: OthelloPepper.Player): OthelloPepper.PlayerId {
        val id = connectHelper.connectPlayer(request.name)
        return OthelloPepper.PlayerId.newBuilder().setName(request.name).setId(id).build()
    }

    override suspend fun disconnect(request: OthelloPepper.PlayerId): OthelloPepper.Response {
        connectHelper.disconnect(request.name, request.id)
        return defaultResponse
    }

    override suspend fun getState(request: OthelloPepper.Void): OthelloPepper.State {
        val currentState = othelloModel.getState()
        return OthelloPepper.State.newBuilder().addAllState(currentState).build()
    }

    override suspend fun setAction(request: OthelloPepper.Action): OthelloPepper.Response {
        val number = connectHelper.getPlayerNumberById(request.playerId)
        Log.d("Hello", mainActivity().toString())
        mainActivity()!!.setAction(request.x, request.y,number)
        return defaultResponse
    }

    override fun streamEvents(request: OthelloPepper.Void): Flow<OthelloPepper.Event> {
        return grpcEventHandler.readEventAsFlow()
    }
}