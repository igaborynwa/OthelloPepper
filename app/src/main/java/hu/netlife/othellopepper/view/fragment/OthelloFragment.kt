package hu.netlife.othellopepper.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import hu.netlife.othellopepper.R
import hu.netlife.othellopepper.databinding.FragmentOthelloBinding
import hu.netlife.othellopepper.game.OthelloModel
import hu.netlife.othellopepper.view.customView.OthelloView
import hu.netlife.othellopepper.viewModel.OthelloFragmentViewModel
import javax.inject.Inject

@AndroidEntryPoint
class OthelloFragment: Fragment() {
    private lateinit var binding : FragmentOthelloBinding
    private val viewModel: OthelloFragmentViewModel by viewModels()

    @Inject
    lateinit var othelloModel: OthelloModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentOthelloBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.othelloView.setOthelloViewListener(object : OthelloView.OthelloViewListener{
            override fun onNextPlayerChanged(nextPlayer: Int) {
                viewModel.changeNextPlayer(nextPlayer)
            }
            override fun onGameEnded(winner: Int, white: Int, black: Int) {
                val winnerColor = if(winner == 1) getString(R.string.white) else getString(R.string.black)
                val title = if(winner<3) String.format(getString(R.string.othello_result_title), winnerColor) else getString(R.string.othello_draw)
                AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(String.format(getString(R.string.othello_result_message), white, black))
                    .setPositiveButton(getString(R.string.othello_new_game)) { _, _ ->
                        restartGame()
                    }
                    .setNegativeButton(getString(R.string.othello_exit)) { _, _ ->
                        findNavController().popBackStack()
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
        binding.exitFromOthelloButton.setOnClickListener {
            findNavController().popBackStack()
        }
        othelloModel.resetModel()
        return binding.root

    }

    private fun restartGame(){
        othelloModel.resetModel()
        binding.othelloView.invalidate()
        viewModel.changeNextPlayer(1)
        binding.tvBlackPointsOthello.text = "2"
        binding.tvWhitePointsOthello.text = "2"
    }
}
