import java.util.List;
import java.util.Map;
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
			int fundCount = org.getFunds().size();
			if (fundCount > 0) {
				System.out.println("There are " + org.getFunds().size() + " funds in this organization:");

				int count = 1;
				for (Fund f : org.getFunds()) {

					System.out.println(count + ": " + f.getName());

					count++;
				}
				System.out.println("-> Enter the fund number to see more information.");
			} else {
				System.out.println("No fund available.");
			}
			System.out.println("-> Enter 0 to create a new fund");

			// change organization's password
			System.out.println("-> Enter 'c' to change your password");

			// modify organization
			System.out.println("-> Enter 'e' to edit your account information");

			System.out.println("-> Enter 'q' or 'quit' to exit the main menu");

			int option = -1;

			while (true) {
				if (in.hasNextInt()) {
					option = in.nextInt();
					if (option < 0 || option > org.getFunds().size()) {
						System.out.println("Please enter a valid fund number or letters indicated above: ");
						continue;
					}
					in.nextLine();
					break;
				}else if (in.hasNext()) {
					String input = in.next();
					if (input.equals("q") || input.equals("quit")) {
						System.out.println("Good bye!");
						return;
					}else if (input.equals("c")) {
						in.nextLine();
						updateOrgPassword();
						break;
					}else if (input.equals("e")) {
						in.nextLine();
						updateOrg();
						break;
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
			} else if (option > 0) {
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

		System.out.println("Choose an option (Enter a number):");
		System.out.println("1. View individual donations");
		System.out.println("2. View aggregated donations by contributor");
		System.out.println("3. Delete this fund");

		int choice = in.nextInt();
		in.nextLine();

		switch (choice) {
			case 1:
				List<Donation> donations = fund.getDonations();
				long totalDonations = 0;
				System.out.println("Number of donations: " + donations.size());
				for (Donation donation : donations) {
					System.out.println("* " + donation.getContributorName() + ": $" + donation.getAmount() + " on " + donation.getDate());
					totalDonations += donation.getAmount();
				}
				int percent = (int) (totalDonations * 100 / fund.getTarget());
				System.out.println("Total donation amount: $" + totalDonations + " (" + percent + "% of target)");
				break;

			case 2:
				List<Map.Entry<String, Fund.AggregateInfo>> aggregateDonations = fund.getAggregatedDonations();
				for (Map.Entry<String, Fund.AggregateInfo> entry : aggregateDonations) {
					String contributor = entry.getKey();
					Fund.AggregateInfo info = entry.getValue();
					System.out.println("* " + contributor + ", " + info.donationCount + " donations, $" + info.totalAmount + " total");
				}
				break;

			case 3:
				System.out.println("Are you sure you want to delete this fund? (yes/no)");
				String confirmation = in.nextLine().trim();
				if (confirmation.equalsIgnoreCase("yes")) {
					try {
						dataManager.deleteFund(fund.getId());
						org.removeFund(fund.getId());
						System.out.println("Fund deleted successfully.");
					} catch (IllegalStateException e) {
						System.out.println("Failed to delete the fund: " + e.getMessage());
					}
				}
				break;

			default:
				System.out.println("Invalid choice. Please enter a valid option.");
				break;
		}

		System.out.println("Press the Enter key to go back to the listing of funds");
		in.nextLine();
	}

	public void updateOrgPassword() {
		System.out.print("Enter your current password: ");
		String currentPassword = in.nextLine().trim();

		if (!currentPassword.equals(org.getPassword())) {
			System.out.println("Your entered current password is incorrect (press Enter to return to the main menu):");
			in.nextLine();
			return;
		}
		System.out.print("Enter your new password: ");
		String newPassword1 = in.nextLine().trim();
		System.out.print("Enter your new password again: ");
		String newPassword2 = in.nextLine().trim();

		if (!newPassword1.equals(newPassword2)) {
			System.out.println("The new passwords do not match (press Enter to return to the main menu):");
			in.nextLine();
			return;
		}

		try {
			String result = dataManager.updateOrgsPassword(org.getId(), newPassword1);
			if (result.equals("success")) {
				System.out.println("Password changed successfully (press Enter to return to the main menu):");
				org.setPassword(newPassword1);
				in.nextLine();
			} else {
				System.out.println("Failed to change password (press Enter to return to the main menu):");
				in.nextLine();
			}
		} catch (IllegalArgumentException | IllegalStateException e) {
			System.out.println("Error in communicating with server: " + e.getMessage());
		}
	}
	public void updateOrg() {
		System.out.print("Enter your password: ");
		String currentPassword = in.nextLine().trim();

		if (!currentPassword.equals(org.getPassword())) {
			System.out.println("Your entered password is incorrect (press Enter to return to the main menu):");
			in.nextLine();
			return;
		}
		System.out.println("Enter the new organization name (press Enter to keep the current value): ");
		System.out.println("Current name is: " + org.getName());
		String newName = in.nextLine().trim();
		if (newName.isEmpty()) {
			newName = org.getName();
		}

		System.out.println("Enter the new organization description (press Enter to keep the current value): ");
		System.out.println("Current description is: " + org.getDescription());
		String newDescription = in.nextLine().trim();
		if (newDescription.isEmpty()) {
			newDescription = org.getDescription();
		}

		try {
			String result = dataManager.updateOrg(org.getId(), newName, newDescription);
			if (result.equals("success")) {
				System.out.println("Organization information updated successfully (press Enter to return to the main menu):");
				in.nextLine();
				org.setName(newName);
				org.setDescription(newDescription);
			} else {
				System.out.println("Failed to update organization information (press Enter to return to the main menu):");
				in.nextLine();
			}
		} catch (IllegalArgumentException | IllegalStateException e) {
			System.out.println("Error in communicating with server: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		DataManager ds = new DataManager(new WebClient("localhost", 3001));
		Scanner in = new Scanner(System.in);

		//User input their own login and password
		System.out.print("Enter your login:");
		String login = in.nextLine().trim();
		System.out.print("Enter your password: ");
		String password = in.nextLine().trim();

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
				String retry = in.nextLine().trim().toLowerCase();
				if (!retry.equals("y")) {
					System.out.println("Program terminated.");
					break;
				}
			}
		}
	}
}
