package threedpmredux;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DoStuff {
	Scanner input = new Scanner(System.in);
	String threading = null;
	final String build = "782015";

	int steps = 0;

	public void initialize() {
		List<Particles> particlelist = new ArrayList<>();

		for (int i = 0; i < 1250; i++) {
			particlelist.add(new Particles());
		}

		this.intro(particlelist);
	}

	private void intro(Collection<Particles> particlelist) {
		String entry;
		Organize organizer;

		boolean running = true;
		boolean threaded = true;
		long timer;

		System.out.println("Welcome to 3DPM Redux! This is a work in progress.  ");

		while (running) {
			if (threaded) {
				threading = "multi-threaded";
			}
			else {
				threading = "single-threaded";
			}

			System.out.println("\nDefault number of particles: 10000");
			System.out.println("\nPlease select the number of particle movements:");
			System.out.println("1: 1024");
			System.out.println("2: 10240");
			System.out.println("3: 102400");
			System.out.println("4: 1024000");
			System.out.println("5: 10240000");
			System.out.println("6: 102400000");
			System.out.println("7: 1024000000");
			System.out.println("t: Toggle between single and multi-threaded mode.");
			System.out.println("Current mode: " + threading);
			System.out.println("x: exit program");

			try {
				entry = input.nextLine();

				switch (entry) {
					case "1":
						steps = 1024;
						break;
					case "2":
						steps = 10240;
						break;
					case "3":
						steps = 102400;
						break;
					case "4":
						steps = 1024000;
						break;
					case "5":
						steps = 10240000;
						break;
					case "6":
						steps = 102400000;
						break;
					case "7":
						steps = 1024000000;
						break;
					case "t":
						threaded = !threaded;
						break;
					case "x":
						running = false;
						break;
					default:
						System.out.println("Invalid input.  Try again, please.");
				}
			}
			catch (NoSuchElementException e) {
				System.out.println("Uh, try again? Please?");
			}

			if (steps != 0) {
				organizer = new Organize();

				timer = System.currentTimeMillis();

				organizer.handlethreads(particlelist, steps, threaded);

				timer = System.currentTimeMillis() - timer;

				this.printallparticles(particlelist, timer);
				steps = 0;
			}
		}
		input.close();
	}

	private void printallparticles(Collection<Particles> particlelist, long timer) {
		File outputfile;
		File resultfile;

		long filetimer;
		float meh = ((steps * 10000f) / (timer / 1000f)) / 1000000f;

		try {
			outputfile = new File("./output.txt");
			resultfile = new File("./results.txt");
			if (outputfile.exists()) {
				outputfile.delete();
			}
			outputfile.createNewFile();
			resultfile.createNewFile();
		}
		catch (NullPointerException e) {
			System.out.println("\nPathname argument invalid.");
		}
		catch (SecurityException e) {
			System.out.println("\nFile write permissions denied.");
		}
		catch (IOException e) {
			System.out.println("General IO error.  File write operation failed.");
		}

		filetimer = System.currentTimeMillis();

		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output.txt", true)))) {
			for (Particles particle : particlelist) {
				for (byte i = 0; i < 8; i++) {
					out.println(particle.getxpos(i));
					out.println(particle.getypos(i));
					out.println(particle.getzpos(i) + "\n");
				}
			}
		}
		catch (IOException e) {
			System.out.println("General IO error.  File write operation failed.");
		}

		filetimer = System.currentTimeMillis() - filetimer;

		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("results.txt", true)))) {
			out.println("Build: " + build);
			out.println(steps + " steps completed.");
			out.println("Mode: " + threading);
			out.println("\nIt took " + timer + " milliseconds to complete the workload.");
			out.println("(" + meh + "M steps per second)");
			out.println("It took " + filetimer + " milliseconds to write the output file.");
			out.println("");
		}
		catch (IOException e) {
			System.out.println("General IO error.  File write operation failed.");
		}

		System.out.println("\nIt took " + timer + " milliseconds to complete the workload.");
		System.out.println("(" + meh + "M steps per second)");
		System.out.println("It took " + filetimer + " milliseconds to write the output file.");
	}
}
