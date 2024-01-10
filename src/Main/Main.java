package Main;

import java.util.*;

import Query.Query;

public class Main {
	Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		Query q = new Query();
		new Main(q);
		
	}
	
	public Main(Query q) {
		while(true) {
			System.out.println();
            System.out.println("-----do something-----");
            System.out.println("1. Add Menu");
            System.out.println("2. Update Menu");
            System.out.println("3. Delete Menu");
            System.out.println("4. Add Transaction (new customer)");
            System.out.println("5. Update Transaction");
            System.out.println("6. Exit");
        	System.out.println("enter number from [1-6]");
            Integer inp = scanner.nextInt(); scanner.nextLine();
            
            switch (inp) {
	            case 1:
	                menu.add_menu(query, employee);
	                break;
	            case 2:
	                menu.update_menu(query, employee);
	                break;
	            case 3:
	                menu.delete_menu(query, employee);
	                break;
	            case 4:
	                new_transaction.make_reservation(query, employee);
	                break;
	            case 5:
	                update_transaction.update(query, employee);
	                break;
	            case 6:
	                System.out.println("Arigatouu~~");
	                System.exit(0);
	                break;
	            default:
                // Handle invalid input
	            	System.out.println("Invalid input!");
	            	break;
		}
	}
}
