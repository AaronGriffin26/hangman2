package hangman2;

import hangman2.gameStates.GameState;
import hangman2.gameStates.GameStateStart;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		System.out.println("H A N G M A N\nEnter your name:");
		Scanner in = new Scanner(System.in);
		String playerName = in.nextLine();
		Game.setPlayerName(playerName);
		System.out.println("You have " + Scores.getTotalPoints(playerName) + " points.");
		GameState state = new GameStateStart();
		while(state != null) {
			state = state.evaluate();
		}
		Scores.displayHighScores();
	}
}
