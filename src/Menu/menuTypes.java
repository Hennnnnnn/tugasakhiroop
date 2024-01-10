package Menu;

import java.util.*;

import Employee.Employee;
import Query.Query;

interface menuTypes {
	public HashMap<Integer, Integer> displayMenuLocation(Query query, Employee employee, HashMap<Integer, Integer> save_id, Integer Count);
	public void deleteFromThisMenu(Query query, Integer menu_id);
	public void addToThisMenu(Query query, List<String> new_menu);
}
