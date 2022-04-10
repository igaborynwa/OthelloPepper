package hu.netlife.othellopepper.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.netlife.othellopepper.game.ConnectHelper
import hu.netlife.othellopepper.grpc.GrpcEvent
import hu.netlife.othellopepper.grpc.GrpcEventHandler
import hu.netlife.othellopepper.proto.OthelloPepper
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val grpcEventHandler: GrpcEventHandler,
    private val connectHelper: ConnectHelper
): ViewModel() {
    private val _nextPlayer = MutableLiveData(1)
    val nextPlayer: LiveData<Int>
        get () = _nextPlayer

    fun changeNextPlayer(nextPlayer: Int) {
        _nextPlayer.postValue(nextPlayer)
        val id = connectHelper.getPlayerIdByNumber(nextPlayer)
        sendEvent(GrpcEvent(OthelloPepper.MessageType.NEXT, id ?: 0))
    }

    fun sendWinner(winner: Int){
        val id = connectHelper.getPlayerIdByNumber(winner)
        sendEvent(GrpcEvent(OthelloPepper.MessageType.END, id ?: 0))
    }

    fun sendEvent(event: GrpcEvent){
        grpcEventHandler.addEvent(event)
    }

}
