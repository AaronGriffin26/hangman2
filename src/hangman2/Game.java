package hangman2;

import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Game {
	private static String playerName = "Player";
	/**
	 * Difficulty level of the game, used to pick a word pool in the glossary.
	 */
	private static int difficulty = 0;
	/**
	 * The correct word the player must reveal.
	 */
	private static String word = " ";
	/**
	 * Array of flags associated with each letter of the word.
	 */
	private static boolean[] revealed = new boolean[1];
	/**
	 * Letters that the player has entered.
	 */
	private static ArrayList<Character> guessed = new ArrayList<>();
	/**
	 * Letters that the player has entered and are not in the word.
	 */
	private static StringBuilder missedLetters = new StringBuilder();

	public static void setPlayerName(String newName) {
		playerName = newName;
	}

	public static void incrementDifficulty() {
		System.out.println("Let's make it harder...");
		difficulty++;
	}

	public static void pickWord() {
		word = Glossary.pick(difficulty);
		revealed = new boolean[word.length()];
		guessed.clear();
		missedLetters = new StringBuilder();
	}

	public static boolean isPlaying() {
		return (missedLetters.length() < 7);
	}

	public static boolean wordRevealed() {
		Stream<Boolean> stream = IntStream.range(0, revealed.length).mapToObj(i -> revealed[i]);
		return stream.reduce(true, Boolean::logicalAnd);
	}

	public static void displayState() {
		System.out.println(Hangman.getState(missedLetters.length()));
		if(difficulty > 0)
			System.out.println("Level " + (difficulty + 1));
		System.out.println("Missed letters: " + missedLetters);
		System.out.println(getWordDisplay());
	}

	private static String getWordDisplay() {
		Stream<String> stream = IntStream.range(0, word.length()).mapToObj(i -> revealed[i] ? String.valueOf(word.charAt(i)) : "_");
		return stream.reduce("", Game::combineLetters);
	}

	private static String combineLetters(String currentLetters, String newLetter) {
		return currentLetters + newLetter + " ";
	}

	public static boolean processGuess(char guess) {
		if(guess == ' ' || guessed.contains(guess))
			return false;
		guessed.add(guess);
		boolean miss = !word.contains(String.valueOf(guess));
		IntStream.range(0, word.length()).mapToObj(i -> word.charAt(i) == guess ? revealed[i] = true : null).toArray();
		if(miss)
			missedLetters.append(guess);
		return true;
	}

	public static void displayGameOver() {
		System.out.println(Hangman.getState(missedLetters.length()));
		System.out.println("You have made too many mistakes and died. The word was \"" + word + "\".");
	}

	public static void displayVictory() {
		System.out.println("Yes! The secret word is \"" + word + "\"! You have won!");
	}

	public static void rewardPoints() {
		int points = Glossary.getPoints(word);
		Scores.addPoints(playerName, points);
		int totalPoints = Scores.getTotalPoints(playerName);
		System.out.println("You earned " + points + " points for getting it correct. You have " + totalPoints + " points.");
	}

	public static boolean won() {
		return (wordRevealed() && missedLetters.length() < 7);
	}
}
