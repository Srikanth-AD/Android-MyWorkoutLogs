package me.srikanth.myworkoutlogs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

public class SaveWorkoutLog extends BaseActivity {

    int initNumOfExerciseRows = 4;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_save_workout_log);
        setTitle("Save Workout Log");

        // Initialize exercises table UI with rows
        for (int i = 0; i < initNumOfExerciseRows; i++) {
            addNewExerciseRow();
        }

        // On click add button, add table row
        ImageButton addExerciseRowBtn = findViewById(R.id.add_exercise_row_button);
        addExerciseRowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewExerciseRow();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Log.i("back", "press");
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    private void addNewExerciseRow() {
        final TableLayout exercisesTableLayout = findViewById(R.id.table_exercises_edit);
        TableRow tr = (TableRow) View.inflate(this, R.layout.table_row_edit_exercise, null);

        EditText exerciseNameEditText = (EditText) tr.getChildAt(1);
        final ImageButton removeExerciseRowBtn = (ImageButton) tr.getChildAt(4);

        exerciseNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
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
