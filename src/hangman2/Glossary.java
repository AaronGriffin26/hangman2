package hangman2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

public class Glossary {
	/**
	 * The number of graded words to skip per difficulty level.
	 */
	private static final int DIFFICULTY_STEP = 17;
	/**
	 * The number of graded words to randomly pick from in a difficulty level.
	 */
	private static final int DIFFICULTY_RANGE = 62;
	/**
	 * The relative frequency of each letter in the alphabet as found in the Concise Oxford Dictionary (11th edition, 2004).
	 * Each letter in the word adds 1/LETTER_USAGE to the grade.
	 */
	private static final float[] LETTER_USAGE = {
			43.31f, 10.56f, 23.13f, 17.25f, // ABCD
			56.88f, 9.24f, 12.59f, 15.31f, // EFGH
			38.45f, 1f, 5.61f, 27.98f, // IJKL
			15.36f, 33.92f, 36.51f, 16.14f, // MNOP
			1f, 38.64f, 29.23f, 35.43f, // QRST
			18.51f, 5.13f, 6.57f, 1.48f, // UVWX
			9.06f, 1.39f // YZ
	};
	/**
	 * The amount of grade to add for each letter not included in a word.
	 */
	private static final float MISSING_LETTER_GRADE = 0.8f;

	private static boolean wordsWereGraded;
	private static LinkedList<String> words = new LinkedList<>();
	private static LinkedList<Float> grades = new LinkedList<>();
	private static boolean wordWasAdded = false;
	private static ArrayList<Character> characters = new ArrayList<>();
	private static float grade = 0f;

	public static String pick(int difficulty) {
		if(!wordsWereGraded) {
			generateDifficultyDictionary();
			wordsWereGraded = true;
		}
		var rng = new Random();
		int startIndex = Math.min(difficulty * DIFFICULTY_STEP, words.size() - DIFFICULTY_RANGE);
		return words.get(startIndex + rng.nextInt(DIFFICULTY_RANGE));
	}

	private static void generateDifficultyDictionary() {
		words.clear();
		grades.clear();
		getWords().stream().map(Glossary::addWord).toArray();
	}

	private static String addWord(String word) {
		float grade = gradeWord(word);
		wordWasAdded = false;
		IntStream.range(0, words.size()).mapToObj(i -> insertWord(word, i, grade)).toArray();
		if(!words.contains(word)) {
			words.add(word);
			grades.add(grade);
		}
		return "added";
	}

	private static String insertWord(String word, int index, float grade) {
		if(!wordWasAdded && grade < grades.get(index)) {
			words.add(index, word);
			grades.add(index, grade);
			wordWasAdded = true;
		}
		return "attempted";
	}

	private static List<String> getWords() {
		Path path = Paths.get(System.getProperty("user.dir"), "src");
		try {
			return Files.readAllLines(Paths.get(path.toString(), "hangman2", "Glossary.txt"));
		}
		catch(Exception ignored) {
			return Arrays.asList("errorabcdfghijklmnpqstuvwxyz\n".repeat(99).split("\n"));
		}
	}

	private static float gradeWord(String word) {
		characters.clear();
		grade = 0f;
		IntStream.range(0, word.length()).mapToObj(i -> addCharacter(word.charAt(i))).toArray();
		grade += (26f - characters.size()) * MISSING_LETTER_GRADE;
		return grade;
	}

	private static String addCharacter(char c) {
		if(!characters.contains(c)) {
			characters.add(c);
			grade += 1f / LETTER_USAGE[c - 'a'];
		}
		return "added";
	}

	public static int getPoints(String word) {
		int index = words.indexOf(word);
		return (int)(grades.get(index) * 100f);
	}
}
