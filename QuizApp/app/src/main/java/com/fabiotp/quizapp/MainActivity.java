package com.fabiotp.quizapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  //Reset all the views
  public void reset(View view) {

    //Question 1 Radio Group
    RadioGroup question1 = findViewById(R.id.rdogrp_question1);
    question1.clearCheck();

    //Question 2 Radio Group
    RadioGroup question2 = findViewById(R.id.rdogrp_question2);
    question2.clearCheck();

    //Question 3 Answers
    CheckBox q3Answer1 = findViewById(R.id.cbx_q3_a1);
    q3Answer1.setChecked(false);
    CheckBox q3Answer2 = findViewById(R.id.cbx_q3_a2);
    q3Answer2.setChecked(false);
    CheckBox q3Answer3 = findViewById(R.id.cbx_q3_a3);
    q3Answer3.setChecked(false);
    CheckBox q3Answer4 = findViewById(R.id.cbx_q3_a4);
    q3Answer4.setChecked(false);

    //Question 4 Answers
    CheckBox q4Answer1 = findViewById(R.id.cbx_q4_a1);
    q4Answer1.setChecked(false);
    CheckBox q4Answer2 = findViewById(R.id.cbx_q4_a2);
    q4Answer2.setChecked(false);
    CheckBox q4Answer3 = findViewById(R.id.cbx_q4_a3);
    q4Answer3.setChecked(false);
    CheckBox q4Answer4 = findViewById(R.id.cbx_q4_a4);
    q4Answer4.setChecked(false);

    //Question 5 Answer
    EditText q5Answer = findViewById(R.id.edtxt_q5);
    q5Answer.setText(null);

    Toast.makeText(MainActivity.this, getString(R.string.txt_reset),
        Toast.LENGTH_SHORT).show();

  }

  //Find all the views
  public void checkScore(View view) {

    //Question 1 Right Answer
    RadioButton q1Answer4 = findViewById(R.id.rdo_q1_a4);
    boolean isQ1A4Checked = q1Answer4.isChecked();

    //Question 2 Right Answer
    RadioButton q2Answer3 = findViewById(R.id.rdo_q2_a3);
    boolean isQ2A3Checked = q2Answer3.isChecked();

    //Question 3 Right Answers
    CheckBox q3Answer1 = findViewById(R.id.cbx_q3_a1);
    boolean isQ3A1Checked = q3Answer1.isChecked();

    CheckBox q3Answer4 = findViewById(R.id.cbx_q3_a4);
    boolean isQ3A4Checked = q3Answer4.isChecked();

    //Question 3 Wrong Answers
    CheckBox q3Answer2 = findViewById(R.id.cbx_q3_a2);
    boolean isQ3A2Checked = q3Answer2.isChecked();

    CheckBox q3Answer3 = findViewById(R.id.cbx_q3_a3);
    boolean isQ3A3Checked = q3Answer3.isChecked();

    //Question 4 Right Answers
    CheckBox q4Answer1 = findViewById(R.id.cbx_q4_a1);
    boolean isQ4A1Checked = q4Answer1.isChecked();

    CheckBox q4Answer4 = findViewById(R.id.cbx_q4_a4);
    boolean isQ4A4Checked = q4Answer4.isChecked();

    //Question 4 Wrong Answers
    CheckBox q4Answer2 = findViewById(R.id.cbx_q4_a2);
    boolean isQ4A2Checked = q4Answer2.isChecked();

    CheckBox q4Answer3 = findViewById(R.id.cbx_q4_a3);
    boolean isQ4A3Checked = q4Answer3.isChecked();

    //Question 5 Answer
    EditText q5Answer = findViewById(R.id.edtxt_q5);
    String getQ5Input = q5Answer.getText().toString();

    /**
     * Show toast with total score and message related to score
     */
    //Bad Score
    if (calculateScore(isQ1A4Checked, isQ2A3Checked,
        isQ3Right(isQ3A1Checked, isQ3A4Checked, isQ3A2Checked, isQ3A3Checked),
        isQ4Right(isQ4A1Checked, isQ4A4Checked, isQ4A2Checked, isQ4A3Checked),
        isQ5valid(getQ5Input)) < 2) {

      Toast.makeText(MainActivity.this, getString(R.string.your_score) +
              calculateScore(isQ1A4Checked, isQ2A3Checked,
                  isQ3Right(isQ3A1Checked, isQ3A4Checked, isQ3A2Checked, isQ3A3Checked),
                  isQ4Right(isQ4A1Checked, isQ4A4Checked, isQ4A2Checked, isQ4A3Checked),
                  isQ5valid(getQ5Input))
              + getString(R.string.bad_score),
          Toast.LENGTH_LONG).show();
    }

    //Good Score
    if (calculateScore(isQ1A4Checked, isQ2A3Checked,
        isQ3Right(isQ3A1Checked, isQ3A4Checked, isQ3A2Checked, isQ3A3Checked),
        isQ4Right(isQ4A1Checked, isQ4A4Checked, isQ4A2Checked, isQ4A3Checked),
        isQ5valid(getQ5Input)) >= 3 &&
        calculateScore(isQ1A4Checked, isQ2A3Checked,
            isQ3Right(isQ3A1Checked, isQ3A4Checked, isQ3A2Checked, isQ3A3Checked),
            isQ4Right(isQ4A1Checked, isQ4A4Checked, isQ4A2Checked, isQ4A3Checked),
            isQ5valid(getQ5Input)) <= 4) {

      Toast.makeText(MainActivity.this, getString(R.string.your_score) +
              calculateScore(isQ1A4Checked, isQ2A3Checked,
                  isQ3Right(isQ3A1Checked, isQ3A4Checked, isQ3A2Checked, isQ3A3Checked),
                  isQ4Right(isQ4A1Checked, isQ4A4Checked, isQ4A2Checked, isQ4A3Checked),
                  isQ5valid(getQ5Input))
              + getString(R.string.good_score),
          Toast.LENGTH_LONG).show();
    }

    //Perfect Score
    if (calculateScore(isQ1A4Checked, isQ2A3Checked,
        isQ3Right(isQ3A1Checked, isQ3A4Checked, isQ3A2Checked, isQ3A3Checked),
        isQ4Right(isQ4A1Checked, isQ4A4Checked, isQ4A2Checked, isQ4A3Checked),
        isQ5valid(getQ5Input)) == 5) {

      Toast.makeText(MainActivity.this, getString(R.string.your_score) +
              calculateScore(isQ1A4Checked, isQ2A3Checked,
                  isQ3Right(isQ3A1Checked, isQ3A4Checked, isQ3A2Checked, isQ3A3Checked),
                  isQ4Right(isQ4A1Checked, isQ4A4Checked, isQ4A2Checked, isQ4A3Checked),
                  isQ5valid(getQ5Input))
              + getString(R.string.perfect_score),
          Toast.LENGTH_LONG).show();
    }

  }

  /**
   * Question 5 - Editext input validation
   */
  private boolean isQ5valid(String q5Answer) {
    return q5Answer.equalsIgnoreCase("Bairro Alto");
  }

  /**
   * Question 3 and 4
   * Check if 2 checkboxes are right
   * If one is wrong, it's still a wrong answer
   */
  private boolean isQ3Right(boolean isQ3A1Checked, boolean isQ3A4Checked,
      boolean isQ3A2Checked, boolean isQ3A3Checked) {
    return isQ3A1Checked && isQ3A4Checked && !isQ3A2Checked && !isQ3A3Checked;
  }

  private boolean isQ4Right(boolean isQ4A1Checked, boolean isQ4A4Checked,
      boolean isQ4A2Checked, boolean isQ4A3Checked) {
    return isQ4A1Checked && isQ4A4Checked && !isQ4A2Checked && !isQ4A3Checked;
  }

  /**
   * Calculate Total score
   */
  private int calculateScore(boolean isQuestion1Right, boolean isQuestion2Right,
      boolean isQuestion3Right, boolean isQuestion4Right,
      boolean isQuestion5Right) {
    int score = 0;

    if (isQuestion1Right) {
      score = score + 1;
    }
    if (isQuestion2Right) {
      score = score + 1;
    }
    if (isQuestion3Right) {
      score = score + 1;
    }
    if (isQuestion4Right) {
      score = score + 1;
    }
    if (isQuestion5Right) {
      score = score + 1;
    }
    return score;
  }

}
