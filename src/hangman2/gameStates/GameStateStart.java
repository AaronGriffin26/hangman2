package hangman2.gameStates;

import hangman2.Game;

public class GameStateStart extends GameState {
	@Override
	public GameState evaluate() {
		if(Game.won())
			Game.incrementDifficulty();
		Game.pickWord();
		return new GameStateDisplay();
	}
}
