package hu.netlife.othellopepper.game

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class ConnectHelper @Inject constructor(

) {
    private var player1: Player? = null
    private var player2: Player? = null

    fun connectPlayer(name: String): Int{
        if(player1==null){
            player1 = Player(name, generateId(), 1)
            return player1!!.id!!
        }
        else if(player2 == null){
            player2 = Player(name, generateId(), 2)
            return player2!!.id!!
        }
        throw IllegalStateException("Game is full")
    }

    fun disconnect(name: String, id: Int){
        if((player1!!.id == id) && (player1!!.name == name)){
            player1 = null
            return
        }
        if((player2!!.id == id) && (player2!!.name == name)){
            player2 = null
            return
        }
        throw IllegalStateException("Invalid Name or ID")
    }

    private fun generateId(): Int{
        var id: Int
        do{
            id = Random.nextInt(1,1000)
        } while (id == player1?.id || id== player2?.id)

        return id
    }

    fun getPlayerNumberById(playerId: Int): Int{
        if(player1?.id == playerId) return 1
        else if (player2?.id== playerId) return 2
        throw IllegalStateException("Invalid player id")
    }

    fun getPlayerIdByNumber(number: Int): Int?{
        return if (number == 1) player1?.id else player2?.id
    }
}