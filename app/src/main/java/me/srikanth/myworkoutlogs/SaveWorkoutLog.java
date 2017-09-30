package me.srikanth.myworkoutlogs;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import static android.R.attr.id;
import static me.srikanth.myworkoutlogs.MainActivity.exerciseDataFromDB;
import static me.srikanth.myworkoutlogs.MainActivity.exerciseNamesFromDB;

public class SaveWorkoutLog extends BaseActivity {

    int initNumOfExerciseRows = 3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_save_workout_log);
        setTitle("Save Workout Log");

        // On click add button, add table row
        ImageButton addExerciseRowBtn = findViewById(R.id.add_exercise_row_button);
        addExerciseRowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewExerciseRow();
            }
        });

        // Initialize exercises table UI with rows
        for (int i = 0; i < initNumOfExerciseRows; i++) {
            addNewExerciseRow();
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    private void addNewExerciseRow() {
        final TableLayout exercisesTableLayout = findViewById(R.id.table_exercises_edit);
        final TableRow tr = (TableRow) View.inflate(this, R.layout.table_row_edit_exercise, null);
        final ImageButton removeExerciseRowBtn = (ImageButton) tr.getChildAt(4);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, exerciseNamesFromDB);

        final AutoCompleteTextView exerciseNameEditText = (AutoCompleteTextView) tr.getChildAt(1);

        exerciseNameEditText.setAdapter(adapter);
        exerciseNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (!hasFocus) {
                    ImageButton muscleGroupImageBtn = (ImageButton) tr.getChildAt(0);

                    int drawableId = getResources().getIdentifier("mg_" + exerciseDataFromDB.get(exerciseNameEditText.getText().toString()), "drawable", getPackageName());

                    if ( drawableId != 0 ) {
                        muscleGroupImageBtn.setImageResource(drawableId);
                    }
                }

                if (!hasFocus && isAddNewExerciseRows()) {
                    addNewExerciseRow();
                }
            }
        });

        removeExerciseRowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableRow tr = (TableRow) view.getParent();
                exercisesTableLayout.removeView(tr);
            }
        });

        exercisesTableLayout.addView(tr);
    }

    // If 70% of the rows in exercises table are filled in, append a new empty row
    private boolean isAddNewExerciseRows() {
        int numOfNonEmptyExerciseRows = 0;
        TableLayout exercisesTableLayout = findViewById(R.id.table_exercises_edit);
        int rowCount = exercisesTableLayout.getChildCount();

        for (int i = 0; i < rowCount; i++) {
            TableRow tr = (TableRow) exercisesTableLayout.getChildAt(i);
            EditText exerciseNameEditText = (EditText) tr.getChildAt(1);

            if (exerciseNameEditText.getText() != null && exerciseNameEditText.getText().toString().length() > 0) {
                numOfNonEmptyExerciseRows++;
            }
        }

        return ((float) numOfNonEmptyExerciseRows / (float) rowCount) >= 0.7;
    }

}
