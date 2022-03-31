package hu.netlife.othellopepper.grpc

import android.util.Log
import hu.netlife.othellopepper.proto.OthelloPepper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GrpcEventHandler @Inject constructor() {

    private val TAG = "GrpcEventHandler"
    private var eventChannel = Channel<OthelloPepper.Event>(capacity = 128)

    fun addEvent(grpcEvent: GrpcEvent) {
        val event = OthelloPepper.Event.newBuilder()
            .setMessage(grpcEvent.messageType)
            .setPlayerId(grpcEvent.playerId)
            .build()
        try {
            val offerResult = eventChannel.offer(event)
            if (!offerResult)//Unsuccessful send event, channel is full
                Log.e(TAG ,"Can not add event message to channel, because channel is full. Message:$grpcEvent")
        } catch (e: Exception) {
            Log.e(TAG, "Can not add event message to channel: $e", e)
        }
    }


    fun readEventAsFlow(): Flow<OthelloPepper.Event> {
        return flow {
            while (true) {
                emit(eventChannel.receive())
            }
        }
    }


}