package hu.netlife.othellopepper.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OthelloFragmentViewModel @Inject constructor(): ViewModel() {
    private val _nextPlayer = MutableLiveData(1)
    val nextPlayer: LiveData<Int> = _nextPlayer

    fun changeNextPlayer(nextPlayer: Int) {
        _nextPlayer.postValue(nextPlayer)
    }

}