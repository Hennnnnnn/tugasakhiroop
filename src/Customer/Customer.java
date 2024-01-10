package Customer;

import java.sql.*;
import Query.Query;

public class Customer {
	public Integer id;
	public String name;
	
	public String getName(Query query, Integer customerId) {
		ResultSet rs = query.select("mscustomers", "customerId = " + customerId);
		try {
			while(rs.next()) {
				return rs.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
}
