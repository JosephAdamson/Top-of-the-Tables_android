package com.joeadamson.topofthetables.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.joeadamson.topofthetables.R;
import com.joeadamson.topofthetables.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Landing page for application.
 *
 * @author Joseph Adamson
 */
public class MainMenuActivity extends AppCompatActivity {

    private ListView modeView;

    /**
     * Custom list adapter for main menu
     */
    private class ModeListAdapter extends ArrayAdapter<ItemModel> {

        private final int layout;

        public ModeListAdapter(Context context, int resource, List<ItemModel> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder;

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());

                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();

                viewHolder.modeButton = (Button) convertView.findViewById(R.id.mode_button);;

                // set up the button in the list item
                viewHolder.modeButton.setOnClickListener(new ModeClicker());
                viewHolder.modeButton.setText(getItem(position).modeName);
                viewHolder.modeButton.setTag(getItem(position).mode);

                int colourId = getResources().getIdentifier(
                        getItem(position).colourResource, "color", getPackageName());
                viewHolder.modeButton.setBackgroundTintList(
                        ContextCompat.getColorStateList(getContext(), colourId));

                viewHolder.personalBest = (GridLayout) convertView.findViewById(R.id.personalBest);

                // set gridlayout according to current personal best for the activity.
                Log.i("on item set", Integer.toString(getItem(position).bestScore));
                for (int i = 0; i < getItem(position).bestScore; i++) {
                    ImageView child = (ImageView) viewHolder.personalBest.getChildAt(i);
                    child.setImageResource(R.drawable.star);
                }

                convertView.setTag(viewHolder);
            } else {
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.modeButton.setText(getItem(position).modeName);
            }
            return convertView;
        }

        /**
         * Data class containing assets for each item in our list view
         */
        public class ViewHolder {
            Button modeButton;
            GridLayout personalBest;
        }
    }

    /**
     * Data class for loading our list view.
     */
    public class ItemModel {

        String modeName;
        String colourResource;
        int mode;
        int bestScore;

        public ItemModel(String modeName, String colourResource, int mode, int bestScore) {
            this.modeName = modeName;
            this.colourResource = colourResource;
            this.mode = mode;
            this.bestScore = bestScore;
        }
    }

    private class ModeClicker implements View.OnClickListener {

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

        DatabaseHandler dbh = new DatabaseHandler(MainMenuActivity.this);
        Cursor result = dbh.getPersonalBests();
        result.moveToFirst();

        ArrayList<ItemModel> items = new ArrayList<>();
        items.add(new ItemModel("Multiplication", "picker_green", 0, result.getInt(0)));
        items.add(new ItemModel("Division", "picker_yellow", 1,  result.getInt(1)));
        items.add(new ItemModel("Addition", "picker_red", 2,  result.getInt(2)));
        items.add(new ItemModel("Subtraction", "picker_blue", 3,  result.getInt(3)));

        modeView = (ListView) findViewById(R.id.modeView);
        modeView.setAdapter(new ModeListAdapter(this, R.layout.main_menu_item, items));

        dbh.close();
        result.close();
    }

    /**
     * Refresh activity in the back stack when back is pressed for
     * the succeeding activity.
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}