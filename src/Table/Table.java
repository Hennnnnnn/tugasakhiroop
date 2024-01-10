package Table;

import java.sql.*;
import Query.Query;

public class Table {
	public String tableType;
	public Integer numOfPeople;

	public boolean checkTable(Query query) {
	    ResultSet rs = query.select("tableinfos", "typeofTabel = '" + this.tableType + "'");
	    boolean error = false;

	    try {
	        if (!rs.next()) {
	            System.out.println("Unknown table type");
	            error = true;
	        } else {
	            int tableCapacity = rs.getInt("tableCapacity");
	            if (this.numOfPeople > tableCapacity) {
	                System.out.println("The number of people exceeds table capacity (max. " + tableCapacity + ")");
	                error = true;
	            }
	        }
	    } catch (SQLException e) {
	        error = true;
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) {
	                rs.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return error;
	}

	public void showTable(Query query, Integer transactionId) {
	    ResultSet rs = query.select("tableinfos", "transactionId = " + transactionId);

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
