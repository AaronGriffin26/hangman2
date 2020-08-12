package hangman2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Hangman {
	public static String getState(int state) {
		Path path = Paths.get(System.getProperty("user.dir"), "src", "hangman2", "display", state + ".txt");
		try {
			return Files.readString(path);
		}
		catch(IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}
