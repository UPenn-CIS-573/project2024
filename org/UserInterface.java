import java.util.List;
import java.util.Scanner;

public class UserInterface {
	
	
	private DataManager dataManager;
	private Organization org;
	private Scanner in = new Scanner(System.in);
	
	public UserInterface(DataManager dataManager, Organization org) {
		this.dataManager = dataManager;
		this.org = org;
	}
	
	public void start() {
				
		while (true) {
			System.out.println("\n\n");
			if (org.getFunds().size() > 0) {
				System.out.println("There are " + org.getFunds().size() + " funds in this organization:");
			
				int count = 1;
				for (Fund f : org.getFunds()) {
					
					System.out.println(count + ": " + f.getName());
					
					count++;
				}
				System.out.println("Enter the fund number to see more information.");
			}
			System.out.println("Enter 0 to create a new fund");
			
			String input = in.nextLine();
			if (input.equals("quit") || input.equals("q")) {
				System.out.println("Goodbye!");
				break;
			}

			// int option = in.nextInt();
			// in.nextLine();
			
			// invalid number
			int option = -1;
			boolean novalid = true;
			while (novalid) {
				
				
				try {
					option = Integer.parseInt(input);
				}
				catch (NumberFormatException e) {
					System.out.println("Invalid input. Please enter a number.");
					input = in.nextLine();
					continue;
				}
				if (option > org.getFunds().size() || option < 0) {
					System.out.println("Invalid input. Please enter a number between 0 and " + org.getFunds().size());
					input = in.nextLine();
					continue;
				}
				novalid = false;
			}

			if (option == 0) {
				createFund(); 
			}
			else {
				displayFund(option);
			}
		}			
			
	}
	
	public void createFund() {

		// Modify this method so that it rejects any blank fund name or description, 
		// or a negative value for the fund target, and gracefully handles any non-numeric input for the fund target. 
		// In all cases, it should re-prompt the user to enter the value until they enter something valid. 
		// It should not be possible for the user to crash the program by entering invalid inputs at the prompts.

		
		System.out.print("Enter the fund name: ");
		// String name = in.nextLine().trim();
		String name = "";
		while (name.trim().equals("")) {
			name = in.nextLine().trim();
			if (name.trim().equals("")) {
				System.out.println("Fund name cannot be blank.");
			}
		}
		
		System.out.print("Enter the fund description: ");
		String description = "";
		while (description.trim().equals("")) {
			description = in.nextLine().trim();
			if (description.trim().equals("")) {
				System.out.println("Fund description cannot be blank.");
			}
		}
		
		System.out.print("Enter the fund target: ");
		// long target = in.nextInt();
		// in.nextLine();
		long target = -1;
		while (target < 0) {
			try {
				target = Long.parseLong(in.nextLine());
				if (target < 0) {
					System.out.println("Fund target cannot be negative.");
				}
			}
			catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number.");
				target = -1;
			}
		}

		Fund fund = dataManager.createFund(org.getId(), name, description, target);
		org.getFunds().add(fund);
	}
	
	
	public void displayFund(int fundNumber) {
		// Bug No.1 out of index
		// The bug is that the fundNumber is not checked to be within the bounds of the funds list.
		if(fundNumber <= org.getFunds().size() && fundNumber > 0) {
			Fund fund = org.getFunds().get(fundNumber - 1);

			System.out.println("\n\n");
			System.out.println("Here is information about this fund:");
			System.out.println("Name: " + fund.getName());
			System.out.println("Description: " + fund.getDescription());
			System.out.println("Target: $" + fund.getTarget());

			List<Donation> donations = fund.getDonations();
			System.out.println("Number of donations: " + donations.size());
			for (Donation donation : donations) {
				System.out.println("* " + donation.getContributorName() + ": $" + donation.getAmount() + " on " + donation.getDate());
			}
		} else {
			System.out.println("Invalid fund number");
		}

		System.out.println("Press the Enter key to go back to the listing of funds");
		in.nextLine();
	}
	
	
	public static void main(String[] args) {
		
		DataManager ds = new DataManager(new WebClient("localhost", 3001));
		
		String login = args[0];
		String password = args[1];

		Organization org = null;
		try{
			org = ds.attemptLogin(login, password);
		} catch (IllegalStateException e) {
			System.out.println("Error in communicating with server.");
			return;
		}
		
		if (org == null) {
			System.out.println("Login failed.");
		}
		else {

			UserInterface ui = new UserInterface(ds, org);
		
			ui.start();
		
		}
	}

}
