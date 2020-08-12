package hangman2.gameStates;

import hangman2.Game;

import java.util.Scanner;

public class GameStatePrompt extends GameState {
	@Override
	public GameState evaluate() {
		var scanner = new Scanner(System.in);
		System.out.println("Guess a letter.");
		String choice = scanner.nextLine();
		boolean result = false;
		if(choice.matches("^([a-z]|[A-Z])$")) {
			char guess = choice.toLowerCase().charAt(0);
			result = Game.processGuess(guess);
			if(!result)
				System.out.println("You have already guessed that letter. Choose again.");
		}
		else
			System.out.println("Not a valid letter.");
		if(result)
			return new GameStateDisplay();
		return new GameStatePrompt();
	}
}
