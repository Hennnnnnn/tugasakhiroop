package Query;
import java.util.*;

import Employee.Employee;
import Customer.Customer;
import Menu.Menu;

import java.sql.*;

public class updateTransaction {
	Scanner scan = new Scanner(System.in);
	
	public void viewAllTransactions(Query query) {
		System.out.println("== Transactions ==");
        ResultSet rs = query.select("transactions", "1");
        Customer customer = new Customer();

        try {
            while (rs.next()) {
                System.out.println("Transaction ID: " + rs.getInt("transactionId"));
                System.out.println("Customer name: " + customer.getName(query, rs.getInt("customerId")));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public void take_order(Query query, Employee employee) {
	    boolean update_status = false;
	    Integer transactionId = null;
	    String status = "";

	    System.out.println("== Taking order ==");

	    while (true) {
	        System.out.printf("Transaction ID: ");
	        transactionId = scan.nextInt();
	        scan.nextLine();

	        ResultSet rs = query.select("transactions", "transactionId = " + transactionId);
	        try {
	            if (rs.next()) {
	                status = rs.getString("status");
	                if (status.equals("in_reserve")) {
	                    update_status = true;
	                    break;
	                } else if (status.equals("in_order")) {
	                    break;
	                }
	            } else {
	                System.out.println("Invalid transaction ID, please re-input\n");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    Menu menu = new Menu();
	    Integer quantity;
	    menu.displayAllMenus(query, employee);

	    while (true) {
	        System.out.printf("No. menu: ");
	        int menuId = scan.nextInt();
	        scan.nextLine();

	        if (menu.saveId.containsKey(menuId)) {
	            menuId = menu.saveId.get(menuId);
	        } else {
	            System.out.println("No such menu exists, please re-input");
	            continue;
	        }

	        System.out.printf("Quantity: ");
	        quantity = scan.nextInt();
	        scan.nextLine(); 

	        ResultSet orderRs = query.select("msorders", "transactionId = " + transactionId + " and menuId = " + menuId);
	        Integer newQuantity = 0;

	        try {
	            if (orderRs.next()) {
	                newQuantity = orderRs.getInt("quantity") + quantity;
	                query.update("msorders", "quantity", Integer.toString(newQuantity),
	                        "transactionId = " + transactionId + " and menuId = " + menuId);
	            } else {
	                query.insert("msorders", "(`transactionId`, `menuId`, `quantity`)",
	                        "(" + transactionId + ", " + menuId + ", " + quantity + ")");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        System.out.println("Add another menu? [y/n]");
	        String repeat = scan.nextLine();
	        if (repeat.equals("n")) {
	            if (update_status) {
	                query.update("transactions", "status", "'in_order'", "transactionId = " + transactionId);
	            }
	            System.out.println("");
	            break;
	        }
	        System.out.println("");
	    }
	}

	
	public void finalize_order(Query query, Employee employee) {
	    System.out.print("Input Transaction ID: ");
	    Integer finalId = scan.nextInt();
	    scan.nextLine();

	    ResultSet rs = query.select("transactions", "transactionId = " + finalId);
	    Integer totalPrice = 0;

	    try {
	        if (rs.next() && rs.getString("status").equals("in_order")) {
	            ResultSet tr = query.show_transaction(rs, finalId);

	            System.out.println();
	            System.out.println("Transaction ID: " + finalId);
	            System.out.println("Menu                Price    Quantity");

	            while (tr.next()) {
	                String menuName = tr.getString("foodname");
	                Integer menuPrice = tr.getInt("price");
	                Integer menuQuantity = tr.getInt("quantity");

	                System.out.printf("%-20s", menuName);
	                System.out.printf("%-10d", menuPrice);
	                System.out.println("  " + menuQuantity);

	                totalPrice += (menuPrice * menuQuantity);
	            }

	            System.out.printf("%20s", "");
	            System.out.println("Total Price: " + totalPrice);

	            query.update("transactions", "status", "'finalized'", "transactionId = " + finalId);
	        } else {
	            System.out.println("Invalid or non 'in_order' transaction ID. Finalization failed.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	
	public void update(Query query, Employee employee) {
	    System.out.println("== Update transaction ==");

	    viewAllTransactions(query);

	    int choice;
	    do {
	        System.out.println("1. Take order");
	        System.out.println("2. Finalize order");
	        System.out.print("Enter choice: ");
	        choice = scan.nextInt();
	        if (choice == 1 || choice == 2) {
	            scan.nextLine(); 
	            break;
	        }
	        System.out.println("Unknown option, please re-enter\n");
	    } while (true);
	    System.out.println();

	    switch (choice) {
	        case 1:
	            take_order(query, employee);
	            break;
	        case 2:
	            finalize_order(query, employee);
	            break;
	    }
	}

}
