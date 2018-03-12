/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.word_list);

    //Create a list of words
    ArrayList<Word> words = new ArrayList<>();

    //words.add("one");
    words.add(new Word("Where are you going?", "minto wuksus"));
    words.add(new Word("What is your name?", "tinnә oyaase'nә"));
    words.add(new Word("My name is...", "oyaaset..."));
    words.add(new Word("How are you feeling?", "michәksәs?"));
    words.add(new Word("I’m feeling good.", "kuchi achit"));
    words.add(new Word("Are you coming?", "әәnәs'aa?"));
    words.add(new Word("Yes, I’m coming.", "hәә’ әәnәm"));
    words.add(new Word("I’m coming.", "әәnәm"));
    words.add(new Word("Let’s go.", "yoowutis"));
    words.add(new Word("Come here.", "әnni'nem"));

    WordAdapter adapter = new WordAdapter(this, words);

    ListView listView = (ListView) findViewById(R.id.list);
    listView.setAdapter(adapter);

    //Find the root view of the whole layout
    //LinearLayout rootView = (LinearLayout) findViewById(R.id.rootView);

    /* While Loop
    //Create a variable to keep track of the current index position
    int index = 0;
    while (index < words.size()) {

      //Create a new {@link TextView} that displayed the word at
      //and add the View as a child to the rootview
      TextView wordView = new TextView(this);
      wordView.setText(words.get(index));
      rootView.addView(wordView);

      //Update counter variable
      index++; //index = index + 1
    }
    */
    /*
    //For loop
    for (int index = 0; index < words.size(); index++) {
      //Create a new {@link TextView} that displayed the word at
      //and add the View as a child to the rootview
      TextView wordView = new TextView(this);
      wordView.setText(words.get(index));
      rootView.addView(wordView);
    }
    */
  }
}

