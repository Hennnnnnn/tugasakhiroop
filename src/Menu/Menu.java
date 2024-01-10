package Menu;

import java.util.*;
import java.sql.*;
import Employee.Employee;
import Query.Query;


public class Menu {
	public int id;
    public String name;
    int price;
    public String location;
    private Scanner scan = new Scanner(System.in);
    public HashMap<Integer, Integer> saveId = new HashMap<>();
    private Integer count = 1;
    SpecialMenu menuSpecial = new SpecialMenu();
    LocalMenu menuLocal = new LocalMenu();

    public void resetState() {
        saveId.clear();
        count = 1;
    }

    public void displayAllMenus(Query query, Employee employee) {
        try {
            ResultSet rs = query.select("msmenu", "location = 'All locations'");
            if (rs.next()) {
                System.out.printf("General menu:\n");
                while (rs.next()) {
                    System.out.printf("No: %s \n" +
                                    "Nama: %s \n" +
                                    "Harga: %s\n\n",
                            count,
                            rs.getString("foodname"),
                            rs.getString("price"));
                    saveId.put(count, Integer.parseInt(rs.getString("menuId")));
                    count++;
                }
            }

            if (employee.restoType.equals("Special")) {
                saveId = menuSpecial.displayMenuLocation(query, employee, saveId, count);
            } else {
                saveId = menuLocal.displayMenuLocation(query, employee, saveId, count);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean validate(Query query, Integer menuId, Employee employee) {
        try {
            // Check if this menu is being ordered
            int rowCount = query.size(query.select("msorders", "menuId = " + menuId));

            if (rowCount > 0) {
                System.out.println("Update request rejected, the requested menu is being ordered\n");
                return false;
            }

            // Check if the employee has the right to modify this menu
            ResultSet rs = query.select("msmenu", "menuId = " + menuId);
            while (rs.next()) {
                String menuLocation = rs.getString("location");
                if (!menuLocation.equals("All locations") && !menuLocation.equals(employee.location)) {
                    System.out.println("You don't have the authority to modify this menu\n");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public void updateMenu(Query query, Employee employee) {
        resetState();
        // Print all available msmenu
        System.out.printf("\n\nList of msmenu at %s branch restaurant:\n\n", employee.location);
        displayAllMenus(query, employee);

        List<String> columns = Arrays.asList("foodname", "price");
        Integer menuId;
        Integer updateOp;
        String newValue;

        // Pick menuId to update
        while (true) {
            System.out.printf("Enter menu ID to update [1 - %d]: ", saveId.size());
            menuId = scan.nextInt();
            scan.nextLine();
            if (menuId >= 1 && menuId <= saveId.size()) {
                break;
            }
            System.out.println("Menu ID doesn't exist! Please re-input");
        }

        // Convert menu ID from display to actual menu ID
        menuId = saveId.get(menuId);
        // Check menu ID history transaction and employee location
        if (!validate(query, menuId, employee)) {
            return;
        }

        // Pick which column to update
        while (true) {
            System.out.println("-----Update-----");
            System.out.println("1. Menu Name");
            System.out.println("2. Menu Price");
            updateOp = scan.nextInt();
            scan.nextLine();
            if (updateOp == 1 || updateOp == 2) {
                break;
            }
            System.out.println("Unknown choice, please re-input");
        }

        // Input the new value for update
        System.out.println("\nNew value:\n");
        newValue = scan.nextLine();

        // Perform the update
        String columnName = columns.get(updateOp - 1);
        query.update("msmenu", columnName, (updateOp == 1) ? "'" + newValue + "'" : newValue, "menuId = " + menuId);
    }
    
    public void deleteFromMenu(Query query, Integer menuId) {
		query.delete("msmenu", "menuId", menuId);
	}
    
    public void deleteMenu(Query query, Employee employee) {
        resetState();
        // Print all available msmenu
        System.out.printf("\n\nList of msmenu at %s branch restaurant:\n\n", employee.location);
        displayAllMenus(query, employee);

        Integer menuId;
        // Pick menu ID
        while (true) {
            System.out.printf("Enter menu ID to delete [1 - %d]\n", saveId.size());
            menuId = scan.nextInt();
            scan.nextLine();
            if (menuId >= 1 && menuId <= saveId.size()) {
                break;
            }
            System.out.println("Menu ID doesn't exist! Please re-input");
        }

        // Convert menu ID from display to actual menu ID from table
        menuId = saveId.get(menuId);

        // Check menu ID history transaction and employee location
        if (!validate(query, menuId, employee)) {
            return;
        }

        // Delete from menu_special table (if the menu is special)
        menuSpecial.deleteFromThisMenu(query, menuId);

        // Delete from menu_local table (if the menu is local)
        menuLocal.deleteFromThisMenu(query, menuId);

        // Delete from msmenu table
        deleteFromMenu(query, menuId);
    }

    public void addToMenus(Query query, List<String> newMenu, Employee employee) {
        String columns = "(`foodname`, `price`, `location`)";
        String values = String.format("('%s', %s, '%s')", newMenu.get(0), newMenu.get(1), employee.location
);
        query.insert("msmenu", columns, values);
    }

    public void addMenu(Query query, Employee employee) {
        resetState();
        List<String> question = Arrays.asList("foodname", "price", "narasi", "origin");
        List<String> newMenu = new ArrayList<>(Arrays.asList("", "", "", ""));
        String menuType;

        // Ask user for menu type
        System.out.println("-----Add Menu-----");
        while (true) {
            System.out.print("New menu type (Regular, Special, or Local): ");
            menuType = scan.nextLine();
            if (menuType.equals("Regular") || menuType.equals(employee.restoType)) {
                break;
            }
            System.out.println("You don't have the authority to create that kind of menu. Please re-input\n");
        }

        // Get input data
        for (int i = 0; i < 4; i++) {
            if ((i == 2 && menuType.equals("Regular")) || (i == 3 && menuType.equals("Special"))) {
                break;
            }
            System.out.printf("[%s] : \n", question.get(i));
            String inp = scan.nextLine();
            newMenu.set(i, inp);
        }

        // Insert into msmenu table
        addToMenus(query, newMenu, employee);

        // If menu is special, insert into menu_special table
        if (menuType.equals("Special")) {
            menuSpecial.addToThisMenu(query, newMenu);
        }

        // If menu is local, insert into menu_local table
        if (menuType.equals("Local")) {
            menuLocal.addToThisMenu(query, newMenu);
        }
    }



}
