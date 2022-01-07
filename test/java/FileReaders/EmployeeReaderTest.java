package FileReaders;

import Employee.Employee;
import _Initialize._Initialize;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNotEquals;

public class EmployeeReaderTest {
  @BeforeClass
  public static void setUp(){
    _Initialize.setClearEmployeeFlag(true);
    _Initialize.setUp();
  }

  @AfterClass
  public static void tearDown(){
    _Initialize.setUp();
  }

  /**
   * tests read Employees
   */
  @Test
  public void readFile(){
      _Initialize.getEmployeeReader().readFile("EmployeesFile.csv");
      ArrayList<Employee> employees = EmployeeReader.getEmployeeList();
      assertNotEquals(employees.size(), 0);
  }
}