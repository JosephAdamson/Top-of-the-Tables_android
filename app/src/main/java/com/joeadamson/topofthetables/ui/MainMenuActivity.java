package com.joeadamson.topofthetables.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.joeadamson.topofthetables.R;

import java.util.ArrayList;

/**
 * Landing page for application.
 *
 * @author Joseph Adamson
 */
public class MainMenuActivity extends AppCompatActivity {

    ArrayList<Button> modeButtons = new ArrayList<>();

    class ModeClicker implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Button modeButtonClicked = (Button) view;

            Intent intent = new Intent(MainMenuActivity.this, TrainerActivity.class);
            intent.putExtra("MODE",
                    Integer.parseInt(modeButtonClicked.getTag().toString()));
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity);

        // Wire mode buttons
        for (int i = 0; i < 4; i++) {
            String buttonId = "mode_button_" + i;
            int resourceId = getResources().getIdentifier(buttonId, "id", getPackageName());
            Button button = (Button) findViewById(resourceId);
            button.setOnClickListener(new ModeClicker());
            modeButtons.add(button);
        }

    }
}