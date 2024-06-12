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
            System.out.println("Or enter 'q' or 'quit' to exit");
            
            String choice = in.nextLine().trim();
            
            if (choice.equals("quit") || choice.equals("q")) {
                System.out.println("Good bye!");
                break;
            }
            
            try {
                int option = Integer.parseInt(choice);
                if (option == 0) {
                    createFund();
                } else if (option <= org.getFunds().size() && option > 0) {
                    displayFund(option);
                } else {
                    System.out.println("Fund number is out of bounds.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }           
            
    }				

					

	public void createFund() {

		String name = "";
		String description = "";
		long target;
		
		while(name.isEmpty()){
			System.out.print("Enter the fund name: ");
			name = in.nextLine().trim();
			if(name.isEmpty()){
				System.out.println("Fund name cannot be left blank or empty.");
			}
		}
		while(description.isEmpty()){
			System.out.print("Enter the fund description: ");
			description = in.nextLine().trim();
			if(name.isEmpty()){
				System.out.println("Fund description cannot be left blank or empty.");
			}
		}
		
		while(true){
			System.out.print("Enter the fund target: ");
			try{
				target = Long.parseLong(in.nextLine().trim());
				if(target < 0){
					System.out.println("Fund target cannot be negative.");
				}else{
					break;
				}
			} catch (NumberFormatException e){
				System.out.println("Invalid input. Please enter a number");
			}
		}
		

		Fund fund = dataManager.createFund(org.getId(), name, description, target);
		org.getFunds().add(fund);

		
	}

	
	
	public void displayFund(int fundNumber) {
        
        Fund fund = org.getFunds().get(fundNumber - 1);
        
        System.out.println("\n\n");
        System.out.println("Here is information about this fund:");
        System.out.println("Name: " + fund.getName());
        System.out.println("Description: " + fund.getDescription());
        System.out.println("Target: $" + fund.getTarget());
        
        List<Donation> donations = fund.getDonations();
        System.out.println("Number of donations: " + donations.size());
        double total = 0;
        for (Donation donation : donations) {
            System.out.println("* " + donation.getContributorName() + ": $" + donation.getAmount() + " on " + donation.getDate());
            total += donation.getAmount();
        }
        
        double percent = (total / fund.getTarget()) * 100;
        System.out.printf("Total donation amount: $%.2f (%.2f%% of target)\n", total, percent);
        
        System.out.println("Press the Enter key to go back to the listing of funds");
        in.nextLine();
        
    }
	
	
	public static void main(String[] args) {
		
		DataManager ds = new DataManager(new WebClient("localhost", 3001));
		
		String login = args[0];
		String password = args[1];
		
		Organization org;
		try{
			org = ds.attemptLogin(login, password);

		} catch(IllegalStateException e){
			System.out.println("Error communicating with the server.");
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
