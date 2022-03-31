package hu.netlife.othellopepper.view

import android.app.AlertDialog
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import hu.netlife.othellopepper.R
import hu.netlife.othellopepper.databinding.ActivityMainBinding
import hu.netlife.othellopepper.game.OthelloModel
import hu.netlife.othellopepper.util.hideSystemUI
import hu.netlife.othellopepper.view.customView.OthelloView
import hu.netlife.othellopepper.viewModel.MainActivityViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var othelloModel: OthelloModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        binding= ActivityMainBinding.inflate(layoutInflater)
        initGame()
        setContentView(binding.root)
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
                val winnerColor = if(winner == 1) getString(R.string.white) else getString(R.string.black)
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
                    .create().show()

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