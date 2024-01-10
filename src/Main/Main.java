package Main;

import java.util.*;
import Menu.Menu;
import Employee.Employee;
import Query.Query;
import Query.newTransaction;
import Query.updateTransaction;

public class Main {
	Scanner scanner = new Scanner(System.in);
	Menu menu = new Menu();
	newTransaction newTransaction = new newTransaction();
	updateTransaction updateTransaction = new updateTransaction();
	
	public static void main(String[] args) {
		Query q = new Query();
		new Main(q);
	}
	
	public Main(Query q) {
		Employee employee = new Employee(q);
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
	                menu.addMenu(q, employee);
	                break;
	            case 2:
	                menu.updateMenu(q, employee);
	                break;
	            case 3:
	                menu.deleteMenu(q, employee);
	                break;
	            case 4:
	            	newTransaction.makeReservation(q, employee);
	                break;
	            case 5:
	            	updateTransaction.update(q, employee);
	                break;
	            case 6:
	                System.out.println("Bye!!");
	                System.exit(0);
	                break;
	            default:
	            	System.out.println("Invalid input!");
	            	break;
            	}
		
			}
		}
}
