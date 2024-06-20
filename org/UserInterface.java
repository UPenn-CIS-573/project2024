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
			int option;

			while (true) {
				if (in.hasNextInt()) {
					option = in.nextInt();
					if (option < 0 || option > org.getFunds().size()) {
						System.out.println("Please enter a valid fund number: ");
						continue;
					}
					in.nextLine();
					break;
				}else if (in.hasNext()) {
					String input = in.next();
					if (input.equals("q") || input.equals("quit")) {
						System.out.println("Good bye!");
						return;
					}else{
						System.out.println("Please enter an Integer:");
						in.nextLine();
					}
				}else {
					System.out.println("Please enter an Integer: ");
					in.nextLine();
				}
			}

			if (option == 0) {
				while (true) {
					try {
						createFund();
						break;
					} catch (IllegalArgumentException | IllegalStateException e) {
						System.out.println(e.getMessage());
						System.out.println("Would you like to retry? (y/n)");
						String retry = in.nextLine().trim().toLowerCase();
						if (!retry.equals("y")) {
							System.out.println("Back to the listing of funds");
							break;
						}
					}
				}
			} else {
				displayFund(option);
			}
		}
	}

	public void createFund() throws IllegalStateException{
		String name = "";
		String description = "";
		long target = -1;

		while (name.isBlank()) {
			System.out.print("Enter the fund name: ");
			name = in.nextLine().trim();
			if (name.isBlank()) {
				System.out.println("Fund name cannot be blank. Please enter a valid name.");
			}
		}

		while (description.isBlank()) {
			System.out.print("Enter the fund description: ");
			description = in.nextLine().trim();
			if (description.isBlank()) {
				System.out.println("Fund description cannot be blank. Please enter a valid description.");
			}
		}


		while (target < 0) {
			System.out.print("Enter the fund target: ");
			String targetInput = in.nextLine().trim();
			try {
				target = Long.parseLong(targetInput);
				if (target < 0) {
					System.out.println("Fund target cannot be negative. Please enter a non-negative value.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a valid numeric value for the fund target.");
			}
		}

		try {
			Fund fund = dataManager.createFund(org.getId(), name, description, target);
			if (fund != null) {
				org.getFunds().add(fund);
				System.out.println("Fund created successfully.");
			} else {
				System.out.println("Failed to create fund.");
			}
		} catch (IllegalArgumentException | IllegalStateException e) {
			throw e;
		}
	}


	public void displayFund(int fundNumber) {

		Fund fund = org.getFunds().get(fundNumber - 1);

		System.out.println("\n\n");
		System.out.println("Here is information about this fund:");
		System.out.println("Name: " + fund.getName());
		System.out.println("Description: " + fund.getDescription());
		System.out.println("Target: $" + fund.getTarget());

		List<Donation> donations = fund.getDonations();
		long totalDonations = 0;
		System.out.println("Number of donations: " + donations.size());
		for (Donation donation : donations) {
			System.out.println("* " + donation.getContributorName() + ": $" + donation.getAmount() + " on " + donation.getDate());
			totalDonations += donation.getAmount();
		}

		int percent = (int)(totalDonations * 100 / fund.getTarget());
		System.out.println("Total donation amount: $" + totalDonations + " (" + percent + "% of target)");

		System.out.println("Press the Enter key to go back to the listing of funds");
		in.nextLine();
	}


	public static void main(String[] args) {
		DataManager ds = new DataManager(new WebClient("localhost", 3001));

		String login = args[0];
		String password = args[1];

		while (true) {
			try {
				Organization org = ds.attemptLogin(login, password);

				if (org == null) {
					System.out.println("Login failed.");
					return;
				} else {
					UserInterface ui = new UserInterface(ds, org);
					ui.start();
					return;
				}
			} catch (IllegalArgumentException | IllegalStateException e) {
				System.out.println(e.getMessage());
				System.out.println("Would you like to retry? (y/n)");
				Scanner in = new Scanner(System.in);
				String retry = in.nextLine().trim().toLowerCase();
				if (!retry.equals("y")) {
					System.out.println("Program terminated.");
					break;
				}
			}
		}
	}
}
