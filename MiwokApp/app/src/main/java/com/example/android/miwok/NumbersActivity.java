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

public class NumbersActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.word_list);

    //Create a list of words
    ArrayList<Word> words = new ArrayList<>();

    //words.add("one");
    words.add(new Word("one", "lutti"));
    words.add(new Word("two", "otiiko"));
    words.add(new Word("three", "tolookosu"));
    words.add(new Word("four", "oyyisa"));
    words.add(new Word("five", "massokka"));
    words.add(new Word("six", "temmokka"));
    words.add(new Word("seven", "kenekaku"));
    words.add(new Word("eight", "kawinta"));
    words.add(new Word("nine", "wo'e"));
    words.add(new Word("ten", "ma'aacha"));

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

