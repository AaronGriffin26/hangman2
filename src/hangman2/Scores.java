package hangman2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Scores {
	private static boolean foundName = false;
	private static int foundPoints = 0;

	public static void addPoints(String playerName, int points) {
		foundName = false;
		Path path = Paths.get(System.getProperty("user.dir"), "scores.txt");
		try {
			List<String> allScores = Files.readAllLines(path);
			Object[] objects = allScores.stream().map(s -> addToName(s, playerName, points)).toArray();
			allScores = new ArrayList<>(Arrays.asList(Arrays.copyOf(objects, objects.length, String[].class)));
			if(!foundName)
				allScores.add(playerName + "|" + points);
			Files.write(path, allScores);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	private static String addToName(String text, String playerName, int points) {
		if(text.substring(0, text.indexOf('|')).equals(playerName)) {
			int currentPoints = Integer.parseInt(text.substring(text.indexOf('|') + 1)) + points;
			foundName = true;
			text = playerName + "|" + currentPoints;
		}
		return text;
	}

	public static int getTotalPoints(String playerName) {
		foundPoints = 0;
		Path path = Paths.get(System.getProperty("user.dir"), "scores.txt");
		try {
			List<String> allScores = Files.readAllLines(path);
			allScores.stream().map(s -> findPoints(s, playerName)).toArray();
			return foundPoints;
		}
		catch(IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	private static String findPoints(String text, String playerName) {
		if(text.substring(0, text.indexOf('|')).equals(playerName))
			foundPoints = Integer.parseInt(text.substring(text.indexOf('|') + 1));
		return text;
	}

	public static void displayHighScores() {
		System.out.println("Current scores:");
		Path path = Paths.get(System.getProperty("user.dir"), "scores.txt");
		try {
			List<String> allScores = Files.readAllLines(path);
			IntStream.range(0, Math.min(10, allScores.size())).forEach(i -> System.out.println(allScores.get(i)));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
