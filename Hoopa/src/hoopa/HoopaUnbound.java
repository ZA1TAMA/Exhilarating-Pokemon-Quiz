package hoopa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import javax.sound.sampled.*;

public class HoopaUnbound {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_CYAN = "\u001B[36m";

	// Ask a query
	public static void ask(String query, String multipleChoice, int i) {
		System.out.println(i + 1 + ". " + query + "\n" + multipleChoice);
	}

	// Evaluator
	public static double evaluate(ArrayList<Character> finalAnswer, ArrayList<Character> correctAnswer, int i,
			double finalScore, int nextRandom) {
		if (finalAnswer.get(i) == correctAnswer.get(nextRandom)) {
			finalScore++;
			System.out.println(ANSI_CYAN + "Question " + (i + 1) + " is correct!" + ANSI_RESET);
		} else {
			System.out.println(ANSI_RED + "You fool! Question " + (i + 1) + " is incorrect!" + ANSI_RESET);
		}
		return finalScore;
	}
	
	public static void PlayMusic(String location) {
		
		try {
			
			File musicPath = new File(location);
			
			if (musicPath.exists()) {
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
				Clip clip = AudioSystem.getClip();
				clip.open(audioInput);
				clip.start();
			} else {
				System.out.println("Can't find file.");
			}
		
		} catch (Exception emusic) {
			System.out.println(emusic);
		}
	}

	public static void main(String[] args) {
		
		//Music
		String music = "oceanicmuseum.wav";
		PlayMusic(music);	
		
		// Linking data files.
		File queryData = new File("question.txt");
		File choiceData = new File("multiple_choice.txt");
		File ansKeyData = new File("correctAnswers.txt");

		// Declaring all array lists.
		ArrayList<String> query = new ArrayList<String>();
		ArrayList<String> multipleChoice = new ArrayList<String>();
		ArrayList<Character> finalAnswer = new ArrayList<Character>();
		ArrayList<Character> correctAnswer = new ArrayList<Character>();

		Scanner playerInput = new Scanner(System.in);

		// Introduction Message
		System.out.println(ANSI_YELLOW + "EXHILIRATING POKÉMON QUIZ" + ANSI_RESET);

		// Filling in the array list with data from each file.
		try {
			// Creating scanner objects.
			Scanner qfileReader = new Scanner(queryData);
			Scanner mfileReader = new Scanner(choiceData);
			Scanner cFileReader = new Scanner(ansKeyData);

			while (qfileReader.hasNextLine() | mfileReader.hasNextLine() | cFileReader.hasNextLine()) {
				query.add(qfileReader.nextLine());
				multipleChoice.add(mfileReader.nextLine());
				correctAnswer.add(cFileReader.next().charAt(0));
			}
			qfileReader.close();
			mfileReader.close();
			cFileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Game loop repeater
		boolean repeat = true;
		while (repeat) {

			repeat = false;
			double finalScore = 0;

			// Random Number Generator
			Random numberGenerator = new Random();
			int nextRandom = 0;
			nextRandom = numberGenerator.nextInt(query.size());
			HashSet<Integer> validate = new HashSet<>();

			// Poses each question one by one and evaluates your score in the background.
			for (int i = 0; i < query.size(); i++) {
				
				while (validate.contains(nextRandom)) {
					
					nextRandom = numberGenerator.nextInt(query.size());
				}
				
				validate.add(nextRandom);
				ask(query.get(nextRandom), multipleChoice.get(nextRandom), i);
				
				String playerAnswer = null;
				
				boolean repeatInput = true;
				while (repeatInput) { 

					repeatInput = false;
					playerAnswer = playerInput.nextLine();
					
				if (playerAnswer.length() != 1) {
					System.out.println("You fool! Pick only one letter.");
					repeatInput = true;
				} else {
					playerAnswer.charAt(0);	
					continue;
				}
				
				}
				char finalPlayerAnswer = playerAnswer.charAt(0);
				finalPlayerAnswer = Character.toUpperCase(finalPlayerAnswer);
				finalAnswer.add(finalPlayerAnswer);
				finalScore = evaluate(finalAnswer, correctAnswer, i, finalScore, nextRandom);
			}
			// Reveals your final score as a percentage.
			System.out.println("You're final score is " + finalScore / query.size() * 100 + "%");
			if (finalScore == query.size()) {
				System.out.println(ANSI_GREEN + "You're a true Pokémon master!" + ANSI_RESET);
			} else {
				System.out.println(ANSI_RED + "You're a literally braindead. Go back to trainer school." + ANSI_RESET);
			}
			// Repeat function
			finalAnswer.clear();
			System.out.println(ANSI_GREEN + "\n" + "Try again? Press Y to continue or any key to end." + ANSI_RESET);
			char cont = playerInput.next().charAt(0);
			cont = Character.toUpperCase(cont);
			if (cont == 'Y') {
				repeat = true;
			} else {
				System.out.println("Thank you for playing my game!");
			}
		}

		playerInput.close();
	}
}
