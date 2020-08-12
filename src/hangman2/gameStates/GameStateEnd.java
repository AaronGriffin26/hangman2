package hangman2.gameStates;

import hangman2.Game;

import java.util.Scanner;

public class GameStateEnd extends GameState {
	private static boolean displayedState;

	@Override
	public GameState evaluate() {
		if(!displayedState) {
			if(Game.won()) {
				Game.displayVictory();
				Game.rewardPoints();
			}
			else
				Game.displayGameOver();
			System.out.println("Do you want to play again? (yes or no)");
			displayedState = true;
		}

		var scanner = new Scanner(System.in);
		String choice = scanner.nextLine().toLowerCase();
		if(choice.equals("y") || choice.equals("yes")) {
			displayedState = false;
			return new GameStateStart();
		}
		if(choice.equals("n") || choice.equals("no"))
			return null;
		System.out.println("Please enter 'yes' or 'no' if you want to play again.");
		return new GameStateEnd();
	}
}
