package hangman2.gameStates;

import hangman2.Game;

public class GameStateDisplay extends GameState {
	@Override
	public GameState evaluate() {
		Game.displayState();
		if(Game.wordRevealed() || !Game.isPlaying())
			return new GameStateEnd();
		return new GameStatePrompt();
	}
}
