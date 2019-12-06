package com.aram.paandroidtask1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.aram.paandroidtask1.WinnerState.*
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_roll_dice.*
import java.lang.StringBuilder
import java.util.*

class RollDiceActivity : AppCompatActivity() {

    private var targetScore: Int = 0

    private lateinit var dice1_image1: ImageView
    private lateinit var dice2_image1: ImageView
    private lateinit var dice1_image2: ImageView
    private lateinit var dice2_image2: ImageView

    private lateinit var player1_tv: TextView
    private lateinit var player2_tv: TextView
    private lateinit var player1_rolls_tv: TextView
    private lateinit var player2_rolls_tv: TextView

    private var p1List = arrayListOf(0,0,0)
    private var p2List = arrayListOf(0,0,0)

    private var p1Score = 0
    private var p2Score = 0

    private var p1LastTreeScores: StringBuilder = StringBuilder("")
    private var p2LastTreeScores: StringBuilder = StringBuilder("")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roll_dice)

        viewInit()

        roll_button.isEnabled = false
        player1_rolls_tv.isVisible = false
        player2_rolls_tv.isVisible = false

        start_button.setOnClickListener{
            setScoreListsToTV()
            targetScore = target_scor_et.text.toString().toIntOrNull()?: 0
         if (targetScore < 12 || targetScore > 120){
             target_scor_et.setError("Score must be integer bigger than 11, less than 120")
             return@setOnClickListener
         }
            roll_button.isEnabled = true
            it.isEnabled = false
            target_scor_et.isEnabled = false
            resetLayoutState()
        }

        show_button.setOnClickListener{
            showLastTreeScors()
        }

        roll_button.setOnClickListener{
            rollDice()
        }

    }

    private fun viewInit() {
        // ImageView
        dice1_image1 = findViewById(R.id.dice1_image1)
        dice2_image1 = findViewById(R.id.dice2_image1)
        dice1_image2 = findViewById(R.id.dice1_image2)
        dice2_image2 = findViewById(R.id.dice2_image2)

        // TextView
        player1_tv = findViewById(R.id.player1_tv)
        player2_tv = findViewById(R.id.player2_tv)
        player1_rolls_tv = findViewById(R.id.player1_rolls_tv)
        player2_rolls_tv = findViewById(R.id.player2_rolls_tv)
    }

    private fun rollDice(){
       val p1d1 = Random().nextInt(6) + 1
       val p1d2 = Random().nextInt(6) + 1
       val p2d1 = Random().nextInt(6) + 1
       val p2d2 = Random().nextInt(6) + 1

        val player1Dices= Pair(getDrawRes(p1d1),getDrawRes(p1d2))
        val player2Dices= Pair(getDrawRes(p2d1),getDrawRes(p2d2))

       setDrawRes(player1Dices,player2Dices)

        p1Score += (p1d1 + p1d2)
        p2Score += (p2d1 + p2d2)

        player1_tv.text = p1Score.toString()
        player2_tv.text = p2Score.toString()

        p1List.add(p1d1 + p1d2)
        p2List.add(p2d1 + p2d2)
        setScoreListsToTV()
        checkWinner()
   }



    private fun getDrawRes(randomInt: Int): Int{
       return when(randomInt){
           1 -> R.drawable.dice_1
           2 -> R.drawable.dice_2
           3 -> R.drawable.dice_3
           4 -> R.drawable.dice_4
           5 -> R.drawable.dice_5
           else -> R.drawable.dice_6
       }
   }

    private fun setDrawRes(p1: Pair<Int,Int>,p2: Pair<Int,Int>){
        dice1_image1.setImageResource(p1.first)
        dice2_image1.setImageResource(p1.second)
        dice1_image2.setImageResource(p2.first)
        dice2_image2.setImageResource(p2.second)
    }

    private fun showLastTreeScors() {
        if(!player1_rolls_tv.isVisible){
            player1_rolls_tv.isVisible = true
            player2_rolls_tv.isVisible = true
            show_button.text = getText(R.string.hide)
        } else {
            player1_rolls_tv.isVisible = false
            player2_rolls_tv.isVisible = false
            show_button.text = getText(R.string.show)
        }
    }

    private fun setScoreListsToTV() {
        p1LastTreeScores.clear()
        p2LastTreeScores.clear()
        var number = 1
        for (index in p1List.size-3 .. p1List.size-1) {
            p1LastTreeScores.append("${p1List[index]}|")
            p2LastTreeScores.append("${p2List[index]}|")
            number++
        }
        player1_rolls_tv.text = p1LastTreeScores
        player2_rolls_tv.text = p2LastTreeScores
    }

    private fun checkWinner() {
        when{
            p1Score >= targetScore && p2Score >= targetScore -> setWinnerInfo(WINNER)
            p1Score >= targetScore && p2Score < targetScore -> setWinnerInfo(PONEWINNER)
            p1Score < targetScore && p2Score >= targetScore -> setWinnerInfo(PTWOWINNER)
        }
    }

    private fun setWinnerInfo(winnerState: WinnerState){
        val textP1Winner = String.format(resources.getString(R.string.winner),p1Score.toString())
        val textP2Winner = String.format(resources.getString(R.string.winner),p2Score.toString())
        val textP1Loser = String.format(resources.getString(R.string.loser),p1Score.toString())
        val textP2Loser = String.format(resources.getString(R.string.loser),p2Score.toString())

        if (winnerState == WINNER){
            player1_tv.text = textP1Winner
            player2_tv.text = textP2Winner
        }

        if (winnerState == PONEWINNER){
            player1_tv.text = textP1Winner
            player2_tv.text = textP2Loser
        }

        if (winnerState == PTWOWINNER){
            player1_tv.text = textP1Loser
            player2_tv.text = textP2Winner
        }

        start_button.isEnabled = true
        roll_button.isEnabled = false
        target_scor_et.isEnabled = true
    }

   private fun resetLayoutState(){

       player1_tv.text = getText(R.string.player_1)
       player2_tv.text = getText(R.string.player_2)
       player1_rolls_tv.text = getText(R.string.last_3_rolls)
       player2_rolls_tv.text = getText(R.string.last_3_rolls)

       dice1_image1.setImageResource(R.drawable.empty_dice)
       dice2_image1.setImageResource(R.drawable.empty_dice)
       dice1_image2.setImageResource(R.drawable.empty_dice)
       dice2_image2.setImageResource(R.drawable.empty_dice)

       p1Score = 0
       p2Score = 0
       p1List = arrayListOf(0,0,0)
       p2List = arrayListOf(0,0,0)
   }
}
