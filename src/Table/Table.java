package Table;

import java.sql.*;
import Query.Query;

public class Table {
	public String tableType;
	public Integer numOfPeople;

	public boolean checkTable(Query query) {
	    ResultSet rs = query.select("tableinfo", "typeofTabel = '" + this.tableType + "'");
	    boolean error = true;

	    try {
	        if (rs.next()) {
	            int tableCapacity = rs.getInt("tableCapacity");
	            if (this.numOfPeople <= tableCapacity) {
	                error = false;
	            } else {
	                System.out.println("The number of people exceeds table capacity (max. " + tableCapacity + ")");
	            }
	        } else {
	            System.out.println("Unknown table type");
	        }
	    } catch (SQLException e) {
	        error = true;
	        e.printStackTrace();
	    }

	    return error;
	}

	public void showTable(Query query, Integer transactionId) {
	    ResultSet rs = query.select("tableinfo", "transactionId = " + transactionId);

	    try {
	        while (rs.next()) {
	            System.out.println("Table type: " + this.tableType);
	            System.out.println("Number of people: " + this.numOfPeople);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

}
