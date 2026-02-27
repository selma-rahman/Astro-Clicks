package UI;
import model.Exoplanet;
import java.util.*;
import logic.QueryEngine;


public class MainView {
	
	List<Exoplanet> planets;
	Scanner sc = new Scanner(System.in);
	QueryEngine queryEngine;
	
	public MainView(List<Exoplanet> p) {
		this.planets = p;
	}
	

	public void start() {
		this.queryEngine = new QueryEngine(planets);
		System.out.println("		WELCOME TO: EXOSCOPE");
		sleep(2);
		System.out.println("•✦　✯　　　•·　　*　　　*　　.·　•··　　　✶★　　*　　☆　　•\n*　　°　✵　　　　　°•　　　　　　　　　　　✦　•　°　　　•　°·　☆");
        sleep(2);
		System.out.println("	꩜ Exoscope loaded " + planets.size() + " planets. ꩜\n");
		mainMenu();
		
		sleep(2);
		System.out.println("	꩜ EXITING: EXOSCOPE ꩜");
		
	}
	
	public void mainMenu() {
		sleep(2);
		System.out.println("Enter a to view search options.");
		sleep(1);
		System.out.println("Enter b to learn about different exoplanet discovery methods.");
		sleep(1);
		System.out.println("Enter c to exit EXOSCOPE.");
		String view = sc.nextLine();
		if (view.equals("a")) {
			System.out.println("To search a planet by name, enter 1");
			System.out.println("To search a planet by host star, enter 2");
			System.out.println("To search a planet by discovery year, enter 3");
			System.out.println("To search a planet by discovery method, enter 4");
			System.out.println("To search a planet by radius, enter 5");
			System.out.println("To search a planet by mass, enter 6");
			System.out.println("To search a planet by orbital period, enter 7");
			System.out.println("To return to main menu, enter 8");
			getUserInput();
		} else if (view.equals("b")) {
			System.out.println("Exoplanet discovery methods\n--------------------");
			System.out.println("Astrometry\nEclipse Timing Variations\nImaging\nMicrolensing\nRadial Velocity\nTransit\nTransit Timing Variations\n");
			mainMenu();
		} else if (view.equals("c")) {
			System.out.println("");
		} else {
			System.out.println("Please enter a valid option.");
			mainMenu();
		}
	}
	
	public void getUserInput() {
		int choice = Integer.parseInt(sc.nextLine());
		switch (choice){
			case 1:
				System.out.println("* You can enter a partial planet name if you wish *");
				System.out.println("Enter a planet name:");
				String name = sc.nextLine();
				List<Exoplanet> results = queryEngine.filterByName(name);
				print(results);
				mainMenu();
				break;
			case 2:
				System.out.println("* You can enter a partial planet name if you wish *");
				System.out.println("Enter a host star name:");
				String hostStar = sc.nextLine();
				results = queryEngine.filterByHostStar(hostStar);
				print(results);
				mainMenu();
				break;
			case 3:
				System.out.println("Enter a discovery year:");
				String year = sc.nextLine();
				results = queryEngine.filterByYear(Integer.parseInt(year));
				print(results);
				mainMenu();
				break;
			case 4:
				System.out.println("Enter a discvoery method:");
				String method = sc.nextLine();
				results = queryEngine.filterByDiscoveryMethod(method);
				print(results);
				mainMenu();
				break;
			case 5:
				System.out.println("Radius measured in units of radius of the Earth");
				System.out.println("Enter a lower radius boundary:");
				Double lower = Double.parseDouble(sc.nextLine());
				System.out.println("Enter an upper radius boundary:");
				Double upper = Double.parseDouble(sc.nextLine());
				results = queryEngine.filterByRadius(lower, upper);
				print(results);
				mainMenu();
				break;
			case 6:
				System.out.println("Mass measured in units of masses of the Earth");
				System.out.println("Enter a lower mass boundary:");
				lower = Double.parseDouble(sc.nextLine());
				System.out.println("Enter an upper mass boundary:");
				upper = Double.parseDouble(sc.nextLine());
				results = queryEngine.filterByMass(lower, upper);
				print(results);
				mainMenu();
				break;
			case 7:
				System.out.println("Enter a lower orbital period boundary (in days):");
				lower = Double.parseDouble(sc.nextLine());
				System.out.println("Enter an upper orbital  period boundary:");
				upper = Double.parseDouble(sc.nextLine());
				results = queryEngine.filterByOrbitalPeriod(lower, upper);
				print(results);
				mainMenu();
				break;
			case 8:
				mainMenu();
		}
	
	}
	
	public void print(List<Exoplanet> results) {
		String kg = "y";
		int x = 0;
		int y = 5;
		if ((kg.equals("y") && results.size() >= y) == false) {
			System.out.println("No results found. Returning to main menu.");
		}
		while ((kg.equals("y") && results.size() >= y)) {
			for (int i = x; i < y; i++) {
				System.out.println(results.get(i).toString());
			}
			System.out.println("Would you like to see more results? (y/n)");
			kg = sc.nextLine();
			x += 5;
			y += 5;
		}
		System.out.println("꩜ RETURNING TO MAIN MENU ꩜");
	}
	
	public void sleep(int n) {
		try {
			Thread.sleep(n*1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}


