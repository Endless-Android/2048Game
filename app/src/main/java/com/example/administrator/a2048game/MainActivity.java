package com.example.administrator.a2048game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.administrator.a2048game.view.GameView;

public class MainActivity extends AppCompatActivity implements GameView.GameListener {

	private TextView mScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();


	}

	private void initView() {
		mScore = (TextView) findViewById(R.id.id_score);

	}

	@Override
	public void onScoreChange(int score) {
		mScore.setText("SCORE: " + score);
	}
}
