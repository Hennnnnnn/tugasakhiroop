package Employee;

import java.util.*;
import Query.Query;
import java.sql.*;

public class Employee {
	public Integer id;
	public String nama;
	public String location;
	public String restoType;
	public void show_employee(Query query) {
	    try {
	        ResultSet rs = query.select("employees", "true");

	        if (rs != null) {
	            while (rs.next()) {
	                System.out.printf("Employee_id : %s\n" +
	                                  "Name: %s \n" +
	                                  "Location: %s\n\n",
	                                  rs.getString("employee_id"),
	                                  rs.getString("name"),
	                                  rs.getString("location"));
	            }
	            rs.close();
	        } else {
	            System.out.println("No employee data found.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Error occurred while fetching employee data: " + e.getMessage());
	    }
	}
	
	public boolean check_data(Integer id, Query query) {
	    try {
	        ResultSet rs = query.select("employees", "employee_id = " + id);
	        int count = 0;

	        while (rs.next()) {
	            count++;
	            this.id = id;
	            this.nama = rs.getString("name");
	            this.location = rs.getString("location");

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



}
