package com.joeadamson.topofthetables.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;

import com.joeadamson.topofthetables.R;

import java.util.ArrayList;
import java.util.List;

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
        setContentView(R.layout.main_menu_activity);

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