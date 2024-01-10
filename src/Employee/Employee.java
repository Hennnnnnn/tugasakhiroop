package Employee;

import java.util.*;
import Query.Query;
import java.sql.*;

public class Employee {
	public Integer id;
	public String nama;
	public String location;
	public String restoType;
	Scanner scanner = new Scanner(System.in);

	public void show_employee(Query query) {
	    try {
	        ResultSet rs = query.select("msemployee", "true");

	        if (rs != null) {
	            while (rs.next()) {
	                System.out.printf("employeeId : %s\n" +
	                                  "Name: %s \n" +
	                                  "Location: %s\n\n",
	                                  rs.getString("employeeId"),
	                                  rs.getString("name"),
	                                  rs.getString("place"));
	            }
	            rs.close();
	        } else {
	            System.out.println("No employee data found.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Error occurred while fetching employee data: " + e.getMessage());
	    }
	}
	
	public Boolean check_data(Integer id, Query query) {
	    try {
	        ResultSet rs = query.select("msemployee", "employeeId = " + id);
	        int count = 0;

	        while (rs.next()) {
	            count++;
	            this.id = id;
	            this.nama = rs.getString("name");
	            this.location = rs.getString("place");

	            if (this.location.equals("Bandung") || this.location.equals("Jakarta") || this.location.equals("Bali")) {
	                this.restoType = "Special";
	            } else {
	                this.restoType = "Local";
	            }
	        }

	        rs.close();
	        return count == 1;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public Employee(Query query) {
    	System.out.println("Choparang exists");
        System.out.println();
        System.out.println("-----Employee-----");
        show_employee(query);
        while(true) {
        	System.out.println("Insert your employee id:");
            Integer id = scanner.nextInt(); 
            scanner.nextLine();
           
            if(check_data(id, query).equals(true)) {
            	System.out.println("Employee id enter succesfully");
            	break;
            }
            System.out.println("Unknown employee id. Please re-input");
        }
        
        return;
    }



}
