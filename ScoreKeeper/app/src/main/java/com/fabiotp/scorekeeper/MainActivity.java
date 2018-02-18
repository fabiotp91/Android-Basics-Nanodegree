package com.fabiotp.scorekeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  int scoreTeamHome = 0;
  int scoreTeamVisitor = 0;
  int foulHome = 0;
  int foulVisitor = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

  }

  //Reset the score
  public void resetScore(View v) {
    scoreTeamHome = 0;
    scoreTeamVisitor = 0;
    foulHome = 0;
    foulVisitor = 0;
    displayForTeamHome(scoreTeamHome);
    displayForTeamVisitor(scoreTeamVisitor);
    displayFoulTeamHome(foulHome);
    displayFoulTeamVisitor(foulVisitor);
  }


  //region Team Home
  //Displays the score for Team Home.
  public void displayForTeamHome(int score) {
    TextView scoreView = findViewById(R.id.team_home_score);
    scoreView.setText(String.valueOf(score));
  }

  //Display the fouls for Team Home
  public void displayFoulTeamHome(int foul) {
    TextView foulView = findViewById(R.id.team_home_foul);
    foulView.setText(String.valueOf(foul));
  }

  //Gives +3 points for Team Home.
  public void add3PointsHome(View v) {
    scoreTeamHome += 3;
    displayForTeamHome(scoreTeamHome);
  }

  //Gives +2 points for Team Home.
  public void add2PointsHome(View v) {
    scoreTeamHome += 2;
    displayForTeamHome(scoreTeamHome);
  }

  //Gives +1 points for Team Home.
  public void add1PointsHome(View v) {
    scoreTeamHome += 1;
    displayForTeamHome(scoreTeamHome);
  }

  //Gives +1 Foul for Team Home
  public void addFoulHome(View v) {
    foulHome += 1;
    displayFoulTeamHome(foulHome);
  }
  //endregion

  //region Team Visitor
  //Displays the score for Team Visitor.
  public void displayForTeamVisitor(int score) {
    TextView scoreView = findViewById(R.id.team_visitor_score);
    scoreView.setText(String.valueOf(score));
  }

  //Display the fouls for Team Visitor
  public void displayFoulTeamVisitor(int foul) {
    TextView foulView = findViewById(R.id.team_visitor_foul);
    foulView.setText(String.valueOf(foul));
  }

  //Gives +3 points for Team Visitor.
  public void add3PointsVisitor(View v) {
    scoreTeamVisitor += 3;
    displayForTeamVisitor(scoreTeamVisitor);
  }

  //Gives +2 points for Team Visitor.
  public void add2PointsVisitor(View v) {
    scoreTeamVisitor += 2;
    displayForTeamVisitor(scoreTeamVisitor);
  }

  //Gives +1 points for Team Visitor.
  public void add1PointsVisitor(View v) {
    scoreTeamVisitor += 1;
    displayForTeamVisitor(scoreTeamVisitor);
  }

  //Gives +1 Foul for Team Visitor
  public void addFoulVisitor(View v) {
    foulVisitor += 1;
    displayFoulTeamVisitor(foulVisitor);
  }
  //endregion
}
