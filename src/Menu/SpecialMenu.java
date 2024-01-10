package Menu;

import java.util.*;
import java.sql.*;
import Employee.Employee;
import Query.Query;

public class SpecialMenu implements menuTypes {

    public HashMap<Integer, Integer> displayMenuLocation(Query query, Employee employee, HashMap<Integer, Integer> saveId, Integer count) {
        try {
            ResultSet rs = query.select("msmenu", "location = '" + employee.location + "'");
            if (rs.next()) {
                System.out.printf("List %s menu: \n", employee.restoType);
                while (rs.next()) {
                    System.out.printf("No: %s \n" +
                                    "Nama: %s \n" +
                                    "Harga : %s\n",
                            count,
                            rs.getString("foodname"),
                            rs.getString("price"));

                    ResultSet menuRs = query.select("specialmenu", "menuId = " + rs.getInt("menuId"));
                    while (menuRs.next()) {
                        System.out.printf("Narasi : %s\n", menuRs.getString("narasi"));
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
        ResultSet rs = query.select("specialmenu", "menuId = " + menuId);
        try {
			if (!rs.next()) return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
        query.delete("specialmenu", "menuId", menuId);
    }

    public void addToThisMenu(Query query, List<String> newMenu) {
        ResultSet rs = query.select("msmenu", "foodname = '" + newMenu.get(0) + "'");
        try {
            Integer menuId = null;
            while (rs.next()) {
                menuId = rs.getInt("menuId");
            }
            if (menuId != null) {
                String columns = "(`menuId`, `narasi`)";
                String values = "(" + menuId + ", '" + newMenu.get(2) + "')";
                query.insert("specialmenu", columns, values);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

