package FileReaders;

import Databases.EmployeeDB;
import Databases.ReligiousEmployeeDB;
import Databases.SanitationEmployeeDB;
import Employee.Employee;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


/*This class is responsible for receiving the data from the
employee csv file */
public class EmployeeReader implements FileReader {
    private static ArrayList<Employee> employeeList = new ArrayList<Employee>();

    //getters and setters
    public static ArrayList<Employee> getEmployeeList() {
        return employeeList;
    }
    public static void setEmployeeList(ArrayList<Employee> employeeList) {
        EmployeeReader.employeeList = employeeList;
    }

    /**
     * Takes in CSV file, reads employees from it,
     * and saves them to employeeList
     * @param fileName
     */
    public void readFile (String fileName) {
        BufferedReader br;
        String line;
        String tempEmpID;
        String tempFirstName;
        String tempLastName;
        String tempEmpType;
        String tempSpecifications;
        boolean tempIsBusy;
        String tempPhoneNo;
        String tempEmailUserName;
        boolean tempPref;

        String busyString;
        String prefString;

        //EmployeeDB.createEmployeeTable();
        //ImagingEmployeeDB.createImagingEmployeeTable();
        SanitationEmployeeDB.createSanitationEmployeeTable();


        ReligiousEmployeeDB.createReligiousEmployeeTable();

        try {
            br = new BufferedReader(new java.io.FileReader(fileName));
            line = br.readLine(); //this line allows the header in the csv to be skipped
//            System.out.println("current line is " + line);
            while ((line = br.readLine()) != null) {
                String[] lineArray = line.split(splitBy);
                tempEmpID = lineArray[0];
                tempFirstName = lineArray[1];
                tempLastName = lineArray[2];
                tempEmpType = lineArray[3];
                tempSpecifications = lineArray[4];
                busyString = lineArray[5];

                if(busyString.equals("T"))
                    tempIsBusy = true;
                else
                    tempIsBusy = false;

                tempPhoneNo = lineArray[6];
                tempEmailUserName = lineArray[7];

                prefString = lineArray[8];

                if(prefString.equals("Y"))
                    tempPref = true;
                else
                    tempPref = false;

                Employee tempEmployee = new Employee(tempEmpID, tempFirstName, tempLastName, tempEmpType, tempSpecifications, tempIsBusy, tempPhoneNo, tempEmailUserName, tempPref);
                employeeList.add(tempEmployee);


                try {
                    EmployeeDB.addEmployeeDB(tempEmployee);

                    if(tempEmployee.getEmpType().equals("SAN")) {
                        SanitationEmployeeDB.addSanitationEmployeeDB(tempEmployee);
//                        System.out.println("san emp " + tempEmployee.getFirstName() + " has been added ");
                    }


                }catch (SQLException e)
                {
                    e.printStackTrace();
                }

//                System.out.println("employee table has been filled");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
