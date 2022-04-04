package hu.netlife.othellopepper.game

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OthelloModel @Inject constructor() {
    companion object {
        const val EMPTY: Int = 0
        const val WHITE: Int = 1
        const val BLACK: Int = 2
    }

    var nextPlayer: Int = 1

    private var model: Array<IntArray> = arrayOf(
        intArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        intArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        intArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        intArrayOf(EMPTY, EMPTY, EMPTY, WHITE, BLACK, EMPTY, EMPTY, EMPTY),
        intArrayOf(EMPTY, EMPTY, EMPTY, BLACK, WHITE, EMPTY, EMPTY, EMPTY),
        intArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        intArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        intArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY))

    fun getState(): ArrayList<Int>{
        val ret = ArrayList<Int>()
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                ret.add(model[i][j])
            }
        }
        return ret
    }


    fun resetModel() {
        nextPlayer = 1
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                model[i][j] = EMPTY
            }
        }
        model[3][3] = WHITE
        model[3][4] = BLACK
        model[4][3] = BLACK
        model[4][4] = WHITE
    }

    fun getPoints(): Pair<Int, Int>{
        var w=0; var b=0
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                if(model[i][j]==1) w++
                if(model[i][j]==2) b++
            }
        }
        return Pair(w,b)
    }

    fun changePlayer(){
        nextPlayer = if(nextPlayer == 1) 2 else 1
    }

    fun setFieldContent(x: Int, y: Int, content: Int): Int{
        doReverses(x, y, content)
        model[x][y] = content
        changePlayer()
        return content
    }

    fun getFieldContent(x: Int, y: Int): Int{
        return model[x][y]
    }

    fun checkPossibleSteps(): ArrayList<Pair<Int, Int>>{
        val possibilities: ArrayList<Pair<Int, Int>> = arrayListOf()
        val other = if(nextPlayer ==1) 2 else 1
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                if (model[i][j]== EMPTY){
                    if(possibleToStep(i, j, other))
                    {
                        possibilities.add(Pair(i,j))
                    }
                }
            }
        }
        return possibilities
    }

    private fun left(i: Int, j: Int, other: Int): Boolean{
        if(i>0 && model[i-1][j]==other){
            for(x in i-1 downTo 0){
                if(model[x][j]== EMPTY) break
                if (model[x][j] == nextPlayer) return true
            }
        }
        return false
    }

    private fun up(i: Int, j: Int, other: Int): Boolean{
        if(j>0 && model[i][j-1]==other){
            for(x in j-1 downTo 0){
                if(model[i][x]== EMPTY) break
                if (model[i][x] == nextPlayer) return true
            }
        }
        return false
    }

    private fun right(i: Int, j: Int, other: Int): Boolean{
        if(i<7 && model[i+1][j] == other){
            for(x in i+1 until 8){
                if(model[x][j]== EMPTY) break
                if (model[x][j] == nextPlayer) return true
            }
        }
        return false
    }

    private fun down(i: Int, j: Int, other: Int): Boolean{
        if(j<7 && model[i][j+1]==other) {
            for(x in j+1 until 8){
                if(model[i][x]== EMPTY) break
                if (model[i][x] == nextPlayer) return true
            }
        }
        return false
    }

    private fun upLeft(i: Int, j: Int, other: Int): Boolean{
        if(i>0 && j>0 &&model[i-1][j-1]==other){
            var x=i-1; var y=j-1
            while(x>0&&y>0){
                if(model[x][y] == EMPTY) break
                if(model[x][y]==nextPlayer) return true
                x--; y--
            }
        }
        return false
    }

    private fun upRight(i: Int, j: Int, other: Int): Boolean{
        if(i<7 && j>0 &&model[i+1][j-1]==other) {
            var x=i+1; var y=j-1
            while(x<8&&y>0){
                if(model[x][y] == EMPTY) break
                if(model[x][y]==nextPlayer) return true
                x++; y--
            }
        }
        return false
    }

    private fun downLeft(i: Int, j: Int, other: Int): Boolean{
        if(i>0 && j<7 &&model[i-1][j+1]==other) {
            var x=i-1; var y=j+1
            while(x>0&&y<8){
                if(model[x][y] == EMPTY) break
                if(model[x][y]==nextPlayer) return true
                x--; y++
            }
        }
        return false
    }

    private fun downRight(i: Int, j: Int, other: Int): Boolean{
        if(i<7 && j<7 &&model[i+1][j+1]==other) {
            var x=i+1; var y=j+1
            while(x<8&&y<8){
                if(model[x][y] == EMPTY) break
                if(model[x][y]==nextPlayer) return true
                x++; y++
            }
        }
        return false
    }

    private fun possibleToStep(i: Int, j: Int, other: Int): Boolean {
        return up(i,j, other) || down(i, j, other) || right(i, j, other) || left(i, j, other)
                || upLeft(i, j, other) || upRight(i, j, other) || downLeft(i, j, other) || downRight(i, j, other)
    }

    private fun doReverses(i: Int, j: Int, content: Int) {
        var x=0
        var y=0
        val other = if(content==1) 2 else 1
        if(left(i, j, other)){
            x=i-1
            while(x>=0 && model[x][j]==other) {
                model[x][j]=content
                x--
            }
        }
        if(right(i, j, other)){
            x=i+1
            while(x<8 && model[x][j]==other) {
                model[x][j]=content
                x++
            }
        }
        if(up(i, j, other)){
            y=j-1
            while(y>=0 && model[i][y]==other) {
                model[i][y]=content
                y--
            }
        }
        if(down(i, j, other)){
            y=j+1
            while(y<8 && model[i][y]==other) {
                model[i][y]=content
                y++
            }
        }
        if(upLeft(i, j, other)){
            x=i-1; y=j-1
            while(x>0 && y>0 && model[x][y]==other){
                model[x][y]=content
                x--; y--
            }
        }
        if(upRight(i, j, other)){
            x=i+1; y=j-1
            while(x<8 && y>0 &&model[x][y]==other){
                model[x][y]=content
                x++; y--
            }
        }
        if(downLeft(i, j, other)){
            x=i-1; y=j+1
            while(x>0 && y<8 &&model[x][y]==other){
                model[x][y]=content
                x--; y++
            }
        }
        if(downRight(i, j, other)){
            x=i+1; y=j+1
            while(x<8 && y<8 && model[x][y]==other){
                model[x][y]=content
                x++; y++
            }
        }


    }



}
