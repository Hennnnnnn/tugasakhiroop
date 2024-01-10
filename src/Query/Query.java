package Query;

import java.util.*;
import java.sql.*;

public class Query {
	Connection conn = null;
	
	public ResultSet select(String table, String condition) {
//		System.out.println("select * "
//        		+ " from "+ table
//        		+ " where " + condition);
		ResultSet rs = null;
		try {
	        Statement stmt = conn.createStatement();
	        rs = stmt.executeQuery("select * "
	        		+ " from "+ table
	        		+ " where " + condition);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return rs;
	}
	
	public void update(String table, String column_value, String new_value, String condition) {
		try {
			String s = "update " + table + " set " + column_value + " = " + new_value+ " where " + condition;
			PreparedStatement pst = conn.prepareStatement(s);
            
            int check = pst.executeUpdate();
            
            if (check != 0) {
            	System.out.println();
                System.out.println("Data successfully updated!");
            } else {
            	System.out.println();
                System.out.println("Update failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
    public void delete(String table, String column_id, Integer id) {
        try {
            PreparedStatement pst = conn.prepareStatement("delete from " + table + " where " + column_id + " = " + id);
            
            int check = pst.executeUpdate();
            if (check != 0) {
                System.out.println("Data successfully Deleted!");
            } else {
                System.out.println("No data deleted");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    public void insert(String table, String column, String value) {
        try {
            PreparedStatement pst = conn.prepareStatement("insert into "+ table + " " + column + " values " + value);
//            System.out.println("insert into "+ table + " " + column + " values " + value);
            int check = pst.executeUpdate();
            if (check != 0) {
                System.out.println("Data successfully inserted!");
            } else {
                System.out.println("Insert failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ResultSet getLastData(String table, String field) {
    	ResultSet rs = null;
		try {
	        Statement stmt = conn.createStatement();
	        rs = stmt.executeQuery("select * from " + table + " order by " + field + " DESC LIMIT 1");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return rs;
    }
    
	public Integer size(ResultSet rs) {
		Integer ct = 0;
		try {
			while(rs.next()) {
				ct++;
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
		return ct;
	}
	
	public ResultSet show_transaction(ResultSet rs, Integer targetId) {
		// from table orders join table menus, display menu name, menu price, menu quantity, menu_price*quantity
		try {
	        Statement stmt = conn.createStatement();
	        rs = stmt.executeQuery("select nama_makanan, price, quantity, price*quantity AS `total_price` "
	        		+ " from "+ "orders as o join menus as m ON m.menu_id = o.menu_id"
	        		+ " where transaction_id = " + targetId);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		return rs;
	}
	
	public Query() {
		try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/akhiroop", "root", "");
           
            if (conn != null) {
                System.out.println("Connection established!");
            } else {
                System.out.println("Failed to connect");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
}
