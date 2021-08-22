package com.joeadamson.topofthetables;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import android.os.CountDownTimer;

import com.joeadamson.topofthetables.trainers.TrainerAddition;
import com.joeadamson.topofthetables.trainers.TrainerDivision;
import com.joeadamson.topofthetables.trainers.TrainerMultiplication;
import com.joeadamson.topofthetables.trainers.TrainerModel;
import com.joeadamson.topofthetables.trainers.TrainerSubtraction;

/**
 * Main game session (multiplication/division/addition/subtraction)
 * for "Top of the Tables" app.
 *
 * @author Joseph Adamson
 */
public class TrainerActivity extends AppCompatActivity {

    GridLayout optionGrid;
    TextView expressionView;
    TextView timerView;
    TextView answerPrompt;
    Button playAgain;
    TrainerModel trainer;
    ArrayList<Button> optionButtons = new ArrayList<>();
    CountDownTimer gameTimer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisecondsUntilDone) {
            timerView.setText(String.format(
                    Locale.getDefault(), "%ds", (int) millisecondsUntilDone / 1000
            ));
        }

        @Override
        public void onFinish() {
            for (Button button : optionButtons) {
                button.setClickable(false);
            }
            answerPrompt.setVisibility(View.INVISIBLE);
            playAgain.setVisibility(View.VISIBLE);
        }
    };

    GridLayout scoreGrid;

    // Data structure and member used to manage score view.
    // Point threshold map (key=score, val=starColumn) dictates how
    // many stars are awarded according to the score.
    int starColumn = -1;
    private static final HashMap<Integer, String> THRESHOLDS;
    static {
        THRESHOLDS = new HashMap<>();
        THRESHOLDS.put(0, "-1");
        THRESHOLDS.put(5, "0");
        THRESHOLDS.put(8, "1");
        THRESHOLDS.put(10, "2");
        THRESHOLDS.put(12, "3");
        THRESHOLDS.put(15, "4");
    }

    /**
     * On-click listener for the four option buttons contained
     * in optionGrid.
     */
    class OptionClicker implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            // Find out which option the user has clicked.
            Button buttonCLicked = (Button) view;
            int guess = Integer.parseInt(buttonCLicked.getText().toString());

            if (guess == trainer.getAnswer()) {
                answerPrompt.setText(R.string.correct);
                trainer.setScore(trainer.getScore() + 1);
                Log.i("current score", Integer.toString(trainer.getScore()));
            } else {
                answerPrompt.setText(R.string.wrong);
                trainer.setScore(trainer.getScore() - 1);
            }

            // reset optionGrid and expressionView
            refreshScoreGrid();
            trainer.loadTrainer();
            setOptionsGrid();
            setExpression();
        }
    }

    /**
     * On-click listener for the "play again" button; resets game state
     * and timer and refreshes UI.
     */
    class PlayAgainClicker implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            playAgain.setVisibility(View.GONE);
            answerPrompt.setVisibility(View.VISIBLE);

            for (Button button: optionButtons) {
                button.setClickable(true);
            }

            // reset game
            answerPrompt.setText("");
            trainer.loadTrainer();
            trainer.setScore(0);
            refreshScoreGrid();
            starColumn = -1;
            setOptionsGrid();
            setExpression();
            gameTimer.start();
        }
    }

    /**
     * Change the numbers displayed on the buttons in
     * the options grid.
     */
    public void setOptionsGrid() {
        for (int i = 0; i < 4; i++) {
            TextView button = (TextView) optionGrid.getChildAt(i);
            button.setText(String.format(Locale.getDefault(),
                    "%d", trainer.getOptionNumbers().get(i)));
        }
    }

    /**
     * Refresh expression view.
     */
    public void setExpression() {
        expressionView.setText(trainer.getExpression());
    }

    /**
     * Dynamically refresh scoreGrid with simple animations.
     */
    public void refreshScoreGrid() {

        int currentScore = trainer.getScore();

        String starColumnToken = THRESHOLDS.get(currentScore);

        if (starColumnToken != null) {

            int starColumnUpdate = Integer.parseInt(starColumnToken);

            if (starColumn < starColumnUpdate) {
                while (starColumn < starColumnUpdate) {
                    starColumn++;
                    animateStar(starColumn, 1f);
                }
            }

            if (starColumn > starColumnUpdate) {
                while (starColumn > starColumnUpdate) {
                    animateStar(starColumn, 0f);
                    starColumn--;
                }
            }
        }
    }

    /**
     * Animate stars in scoreGrid.
     *
     * @param gridIndex child of the gridlayout
     * @param scale factor used to shrink/enlarge the star.
     */
    public void animateStar(int gridIndex, float scale) {
        scoreGrid.getChildAt(gridIndex).animate()
                .scaleX(scale)
                .scaleY(scale);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // determine game mode and set up activity accordingly.
        int gameMode = getIntent().getIntExtra("MODE", -1);

        switch (gameMode) {
            case 0: trainer = new TrainerMultiplication();
                setContentView(R.layout.multiplication_activity);
                break;

            case 1: trainer = new TrainerDivision();
                setContentView(R.layout.division_activity);
                break;

            case 2: trainer = new TrainerAddition();
                setContentView(R.layout.addition_activity);
                break;

            case 3: trainer = new TrainerSubtraction();
                setContentView(R.layout.subtraction_activity);
                break;

            default: Log.i("Error", "Game mode error!");
        }

        expressionView = (TextView) findViewById(R.id.expressionView);
        timerView = (TextView) findViewById(R.id.timerView);
        scoreGrid = (GridLayout) findViewById(R.id.scoreGrid);
        optionGrid = (GridLayout) findViewById(R.id.optionGrid);
        answerPrompt = (TextView) findViewById(R.id.answerPrompt);

        // wire buttons in gridlayout
        for (int i = 0; i < 4; i++) {
            String buttonId = "option_button_" + i;
            int resourceId = getResources().getIdentifier(buttonId, "id", getPackageName());
            Button button = (Button) findViewById(resourceId);
            button.setOnClickListener(new OptionClicker());
            optionButtons.add(button);
        }

        // set up invisible 'play again' button upon timeout
        playAgain = (Button) findViewById(R.id.playAgainButton);
        playAgain.setOnClickListener(new PlayAgainClicker());

        setOptionsGrid();
        setExpression();
        gameTimer.start();
    }
}
