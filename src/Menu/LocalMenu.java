package Menu;

import Employee.Employee;
import Query.Query;

import java.sql.*;
import java.util.*;

public class LocalMenu implements menuTypes {

    public HashMap<Integer, Integer> displayMenuLocation(Query query, Employee employee, HashMap<Integer, Integer> saveId, Integer count) {
        try {
            ResultSet rs = query.select("msmenu", "location = '" +  employee.location + "'");
            if (rs.next()) {
                System.out.printf("List %s menu: \n", employee.restoType);
                while (rs.next()) {
                    System.out.printf("No: %s \n" +
                                    "Nama: %s \n" +
                                    "Harga : %s\n",
                            count,
                            rs.getString("foodname"),
                            rs.getString("price"));

                    ResultSet menuRs = query.select("localmenu", "menuId = " + rs.getInt("menuId"));
                    while (menuRs.next()) {
                        System.out.printf("Description: %s\nOrigin: %s\n", menuRs.getString("Description"), menuRs.getString("origin"));
                    }
                    System.out.println("");

                    saveId.put(count, rs.getInt("menuId"));
                    count++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return saveId;
    }

    public void deleteFromThisMenu(Query query, Integer menuId) {
        ResultSet rs = query.select("localmenu", "menuId = " + menuId);
        try {
			if (!rs.next()) return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
        query.delete("localmenu", "menuId", menuId);
    }

    public void addToThisMenu(Query query, List<String> newMenu) {
        ResultSet rs = query.select("msmenu", "foodname = '" + newMenu.get(0) + "'");
        try {
            Integer menuId = null;
            while (rs.next()) {
                menuId = rs.getInt("menuId");
            }
            if (menuId != null) {
                String columns = "(`menuId`, `Description`, `Origin`)";
                String values = "(" + menuId + ", '" + newMenu.get(2) + "', '" + newMenu.get(3) + "')";
                query.insert("localmenu", columns, values);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

