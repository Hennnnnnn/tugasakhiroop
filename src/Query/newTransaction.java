package Query;

import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;
import Customer.Customer;
import Employee.Employee;
import Table.Table;

public class newTransaction {
    private Scanner scan = new Scanner(System.in);

    public void makeReservation(Query query, Employee employee) {
        Integer numOfTable;
        Integer transactionId = null;
        Customer customer = new Customer();
        Table table = new Table();

        System.out.printf("Customer name: ");
        customer.name = scan.nextLine();

        query.insert("mscustomers", "(`name`, `place`)", "('" + customer.name + "', '" + employee.location + "')");
        try (ResultSet rs = query.getLastData("mscustomers", "customerId")) {
            while (rs.next()) {
                customer.id = rs.getInt("customerId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.printf("Number of tables reserved: ");
        numOfTable = scan.nextInt();
        scan.nextLine();

        for (int i = 0; i < numOfTable; i++) {
            System.out.printf("== Table %d ==\n", i + 1);

            while (true) {
                System.out.printf("Table type: ");
                table.tableType = scan.nextLine();

                System.out.printf("Number of people: ");
                table.numOfPeople = scan.nextInt();
                scan.nextLine();

                if (!table.checkTable(query)) {
                    query.insert("transactions", "(`customerId`, `employeeId`, `status`)",
                            "(" + customer.id + ", " + employee.id + ", 'in_reserve')");

                    try (ResultSet res = query.getLastData("transactions", "transactionId")) {
                        while (res.next()) {
                            transactionId = res.getInt("transactionId");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    query.insert("tables", "(`transactionId`, `typeofTabel`, `tableCapacity`)",
                            "(" + transactionId + ", '" + table.tableType + "', " + table.numOfPeople + ")");

                    break;
                }
            }
        }
        System.out.println("Reservation for customer " + customer.name + " has been made");
    }
}
