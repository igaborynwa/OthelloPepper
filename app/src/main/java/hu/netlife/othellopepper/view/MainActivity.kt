package hu.netlife.othellopepper.view

import android.app.AlertDialog
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import hu.netlife.othellopepper.OthelloPepperApplication
import hu.netlife.othellopepper.R
import hu.netlife.othellopepper.databinding.ActivityMainBinding
import hu.netlife.othellopepper.game.OthelloModel
import hu.netlife.othellopepper.grpc.GrpcEvent
import hu.netlife.othellopepper.proto.OthelloPepper
import hu.netlife.othellopepper.util.hideSystemUI
import hu.netlife.othellopepper.view.customView.OthelloView
import hu.netlife.othellopepper.viewModel.MainActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var othelloModel: OthelloModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as OthelloPepperApplication).currentActivity = this
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        binding= ActivityMainBinding.inflate(layoutInflater)
        initGame()
        setContentView(binding.root)
        window.hideSystemUI()
    }

    override fun onStart() {
        super.onStart()
        window.hideSystemUI()
    }

    private fun initGame(){
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.othelloView.setOthelloViewListener(object : OthelloView.OthelloViewListener{
            override fun onNextPlayerChanged(nextPlayer: Int) {
                viewModel.changeNextPlayer(nextPlayer)
            }
            override fun onGameEnded(winner: Int, white: Int, black: Int) {
                /*val winnerColor = if(winner == 1) getString(R.string.white) else getString(R.string.black)
                val title = if(winner<3) String.format(getString(R.string.othello_result_title), winnerColor) else getString(
                    R.string.othello_draw)
                AlertDialog.Builder(applicationContext)
                    .setTitle(title)
                    .setMessage(String.format(getString(R.string.othello_result_message), white, black))
                    .setPositiveButton(getString(R.string.othello_new_game)) { _, _ ->
                        restartGame()
                    }
                    .setNegativeButton(getString(R.string.othello_exit)) { _, _ ->

                    }
                    .create().show()*/
                viewModel.sendWinner(winner)

            }
            override fun onPointsChanged(white: Int, black: Int) {
                binding.tvBlackPointsOthello.text = black.toString()
                binding.tvWhitePointsOthello.text = white.toString()
            }
        })
        binding.restartOthelloButton.setOnClickListener{
            restartGame()
        }
        othelloModel.resetModel()
    }

    fun setAction(x: Int, y: Int, number: Int){
        if(number!=othelloModel.nextPlayer) throw IllegalStateException("It is not your turn!")
        if(!othelloModel.checkPossibleSteps().contains(Pair(x,y))) throw IllegalStateException("Move is not possible!")
        lifecycleScope.launch (Dispatchers.Main){
            binding.othelloView.setAction(x,y, number)
        }

    }

    private fun restartGame(){
        othelloModel.resetModel()
        binding.othelloView.invalidate()
        viewModel.changeNextPlayer(1)
        binding.tvBlackPointsOthello.text = "2"
        binding.tvWhitePointsOthello.text = "2"
    }

    override fun onResume() {
        super.onResume()
        window.hideSystemUI()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        window.hideSystemUI()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        window.hideSystemUI()
    }

}