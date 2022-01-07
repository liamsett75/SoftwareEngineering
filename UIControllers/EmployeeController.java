package UIControllers;

import Databases.EmployeeDB;
import Employee.Employee;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTreeTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;

import java.sql.SQLException;
import java.util.LinkedList;

public class EmployeeController
{
    @FXML Label tableWarninglbl;
    @FXML Label warninglbl;
    @FXML JFXButton btnDeleteSelectedEmployee;
    @FXML JFXButton btnAddBlankEmployee;
    @FXML JFXTextField newEmpIDTF;
    @FXML JFXButton btnBack;
    @FXML JFXButton btnLoadList;

    @FXML TreeTableView<Employee> treeTableEmployees = new TreeTableView<Employee>();
    @FXML TreeTableColumn<Employee, String> colEmployeeID = new TreeTableColumn<Employee, String>();
    @FXML TreeTableColumn<Employee, String> colFirstName = new TreeTableColumn<Employee, String>();
    @FXML TreeTableColumn<Employee, String> colLastName  = new TreeTableColumn<Employee, String>();
    @FXML TreeTableColumn<Employee, String> colType = new TreeTableColumn<Employee, String>();
    @FXML TreeTableColumn<Employee, String> colSpecifications  = new TreeTableColumn<Employee, String>();
    @FXML TreeTableColumn<Employee, String> colPhoneNumber = new TreeTableColumn<Employee, String>();
    @FXML TreeTableColumn<Employee, String> colEmailUserName = new TreeTableColumn<Employee, String>();
    @FXML TreeTableColumn<Employee, String> colEmailPreferences = new TreeTableColumn<Employee, String>();
    private static ObservableList<Employee> treeTableEmployeeList = FXCollections.observableArrayList();
    private TreeItem<Employee> employeeTreeRoot;
    private static LinkedList<Employee> employeeLinkedList;

    private final Integer empIDLength = 10;
    private final Integer firstNameLength = 20;
    private final Integer lastNameLength = 20;
    private final Integer empTypeLength = 10;
    private final Integer phoneNoLength = 10;
    private final Integer emailUserNameLength = 20;
    private final Integer specificationsLength = 50;



    public static void setMainScreenParent(Parent mainScreenParent) {
        EmployeeController.mainScreenParent = mainScreenParent;
    }

    @FXML static Parent mainScreenParent;

    @FXML
    public void loadListOnClick()
    {
        // new table
        colEmployeeID.setCellValueFactory(new TreeItemPropertyValueFactory<Employee, String>("empID"));
        // cannot change employee ID
        colFirstName.setCellFactory(TextFieldTreeTableCell.<Employee>forTreeTableColumn());
        colFirstName.setCellValueFactory(new TreeItemPropertyValueFactory<Employee, String>("firstName"));
        colFirstName.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
                TreeItem<Employee> employeeItem = treeTableEmployees.getTreeItem(event.getTreeTablePosition().getRow());
                employeeItem.getValue().setFirstName(event.getNewValue());
                // gets the employee from the linked list and gives it to the db
                Employee oldEmployee = Employee.getEmpFromID(employeeLinkedList, employeeItem.getValue().getEmpID());
                Employee newEmployee = employeeItem.getValue();
                if(validEmployee(newEmployee)){
                    tableWarninglbl.setText("");
                    EmployeeDB.editEmployeeDB(oldEmployee, newEmployee);
                    oldEmployee.setFirstName(newEmployee.getFirstName());
                }
                else{
                    tableWarninglbl.setText("Invalid First Name for " + newEmployee.getEmpID() + ", must be less than or equal to " + firstNameLength + " characters.");
                }
            }
        });
        colLastName.setCellFactory(TextFieldTreeTableCell.<Employee>forTreeTableColumn());
        colLastName.setCellValueFactory(new TreeItemPropertyValueFactory<Employee, String>("lastName"));
        colLastName.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
                TreeItem<Employee> employeeItem = treeTableEmployees.getTreeItem(event.getTreeTablePosition().getRow());
                employeeItem.getValue().setLastName(event.getNewValue());
                // gets the employee from the linked list and gives it to the db
                Employee oldEmployee = Employee.getEmpFromID(employeeLinkedList, employeeItem.getValue().getEmpID());
                Employee newEmployee = employeeItem.getValue();
                if(validEmployee(newEmployee)){
                    tableWarninglbl.setText("");
                    EmployeeDB.editEmployeeDB(oldEmployee, newEmployee);
                    oldEmployee.setLastName(newEmployee.getLastName());
                }
                else{
                    tableWarninglbl.setText("Invalid Last Name for " + newEmployee.getEmpID() + ", must be less than or equal to " + lastNameLength + " characters.");
                }
            }
        });
        colType.setCellFactory(TextFieldTreeTableCell.<Employee>forTreeTableColumn());
        colType.setCellValueFactory(new TreeItemPropertyValueFactory<Employee, String>("empType"));
        colType.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
                TreeItem<Employee> employeeItem = treeTableEmployees.getTreeItem(event.getTreeTablePosition().getRow());
                employeeItem.getValue().setEmpType(event.getNewValue());
                // gets the employee from the linked list and gives it to the db
                Employee oldEmployee = Employee.getEmpFromID(employeeLinkedList, employeeItem.getValue().getEmpID());
                Employee newEmployee = employeeItem.getValue();
                if(validEmployee(newEmployee)){
                    tableWarninglbl.setText("");
                    EmployeeDB.editEmployeeDB(oldEmployee, newEmployee);
                    oldEmployee.setEmpType(newEmployee.getEmpType());
                }
                else {
                    tableWarninglbl.setText("Invalid type for " + newEmployee.getEmpID() + ", must be less than or equal to " + empTypeLength + " characters.");
                }
            }
        });
        colSpecifications.setCellFactory(TextFieldTreeTableCell.<Employee>forTreeTableColumn());
        colSpecifications.setCellValueFactory(new TreeItemPropertyValueFactory<Employee, String>("specifications"));
        colSpecifications.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
                TreeItem<Employee> employeeItem = treeTableEmployees.getTreeItem(event.getTreeTablePosition().getRow());
                employeeItem.getValue().setSpecifications(event.getNewValue());
                // gets the employee from the linked list and gives it to the db
                Employee oldEmployee = Employee.getEmpFromID(employeeLinkedList, employeeItem.getValue().getEmpID());
                Employee newEmployee = employeeItem.getValue();
                if(validEmployee(newEmployee)){
                    tableWarninglbl.setText("");
                    EmployeeDB.editEmployeeDB(oldEmployee, newEmployee);
                    oldEmployee.setSpecifications(newEmployee.getSpecifications());
                }
                else{
                   tableWarninglbl.setText("Invalid Specification for " + newEmployee.getEmpID() + ", must be less than or equal to " + specificationsLength + " characters.");
                }
            }
        });
        colPhoneNumber.setCellFactory(TextFieldTreeTableCell.<Employee>forTreeTableColumn());
        colPhoneNumber.setCellValueFactory(new TreeItemPropertyValueFactory<Employee, String>("phoneNo"));
        colPhoneNumber.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
                TreeItem<Employee> employeeItem = treeTableEmployees.getTreeItem(event.getTreeTablePosition().getRow());
                employeeItem.getValue().setPhoneNo(event.getNewValue());
                // gets the employee from the linked list and gives it to the db
                Employee oldEmployee = Employee.getEmpFromID(employeeLinkedList, employeeItem.getValue().getEmpID());
                Employee newEmployee = employeeItem.getValue();
                if(validEmployee(newEmployee)){
                    tableWarninglbl.setText("");
                    EmployeeDB.editEmployeeDB(oldEmployee, newEmployee);
                    oldEmployee.setPhoneNo(newEmployee.getPhoneNo());
                }
                else{
                    tableWarninglbl.setText("Invalid Specification for " + newEmployee.getEmpID() + ", must be less than or equal to " + phoneNoLength + " characters.");
                }
            }
        });
        colEmailUserName.setCellFactory(TextFieldTreeTableCell.<Employee>forTreeTableColumn());
        colEmailUserName.setCellValueFactory(new TreeItemPropertyValueFactory<Employee, String>("emailUserName"));
        colEmailUserName.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
                TreeItem<Employee> employeeItem = treeTableEmployees.getTreeItem(event.getTreeTablePosition().getRow());
                employeeItem.getValue().setEmailUserName(event.getNewValue());
                // gets the employee from the linked list and gives it to the db
                Employee oldEmployee = Employee.getEmpFromID(employeeLinkedList, employeeItem.getValue().getEmpID());
                Employee newEmployee = employeeItem.getValue();
                if(validEmployee(newEmployee)){
                    tableWarninglbl.setText("");
                    EmployeeDB.editEmployeeDB(oldEmployee, newEmployee);
                    oldEmployee.setEmailUserName(newEmployee.getEmailUserName());
                }
                else{
                    tableWarninglbl.setText("Invalid Specification for " + newEmployee.getEmpID() + ", must be less than or equal to " + emailUserNameLength + " characters.");
                }
            }
        });
        ObservableList<String> options = FXCollections.observableArrayList();
        options.addAll("true", "false");
        colEmailPreferences.setCellFactory(ComboBoxTreeTableCell.<Employee, String>forTreeTableColumn(options));
        colEmailPreferences.setCellValueFactory(new TreeItemPropertyValueFactory<Employee, String>("emailPref")); // y or n
        colEmailPreferences.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
                TreeItem<Employee> employeeItem = treeTableEmployees.getTreeItem(event.getTreeTablePosition().getRow());
                if(event.getNewValue().equals("true")){
                    employeeItem.getValue().setEmailPref(true);
                }
                else{
                    employeeItem.getValue().setEmailPref(false);
                }
                // gets the employee from the linked list and gives it to the db
                Employee oldEmployee = Employee.getEmpFromID(employeeLinkedList, employeeItem.getValue().getEmpID());
                Employee newEmployee = employeeItem.getValue();
                if(validEmployee(newEmployee)){
                    tableWarninglbl.setText("");
                    EmployeeDB.editEmployeeDB(oldEmployee, newEmployee);
                    oldEmployee.setEmailPref(newEmployee.getEmailPref());
                }
                else{
                    tableWarninglbl.setText("How... how did you do that?");
                }
            }
        });
        populateEmployeeTable();
    }

    public void updateTable()
    {
        //tableEmployeeList.clear();
        treeTableEmployeeList.clear();
        populateEmployeeTable();
    }

    private void populateEmployeeTable(){
        // new list
        employeeLinkedList = EmployeeDB.getDBEmployees();
//        System.out.println("Size of Employee Linked List is " + employeeLinkedList.size());

        // base employee root
        employeeTreeRoot = new TreeItem<Employee>(new Employee("1","First","Last","Type","Specification",false,"9999999999","nobody",false));

        for(Employee e: employeeLinkedList) {
            e.setEmpID(e.getEmpID());
            TreeItem<Employee> employeeTreeItem = new TreeItem<Employee>(e);
            employeeTreeRoot.getChildren().add(employeeTreeItem);
        }

//        System.out.println("Size of the Tree Table Employee List is " + treeTableEmployeeList.size());
        treeTableEmployees.setShowRoot(false);
        treeTableEmployees.setEditable(true);
        colEmployeeID.setEditable(true);
        colFirstName.setEditable(true);
        colLastName.setEditable(true);
        colType.setEditable(true);
        colSpecifications.setEditable(true);
        colPhoneNumber.setEditable(true);
        colEmailUserName.setEditable(true);
        colEmailPreferences.setEditable(true);
        treeTableEmployees.setRoot(employeeTreeRoot);
    }

    public boolean validEmployee(Employee employee) {
        boolean goodTF = true;
        if (employee.getEmpID().length() > empIDLength) {
            goodTF = false;
        }
        if (employee.getFirstName().length() > firstNameLength){
            goodTF = false;
        }
        if (employee.getLastName().length() > lastNameLength) {
            goodTF = false;
        }
        if (employee.getEmpType().length() > empTypeLength) {
            goodTF = false;
        }
        if (employee.getPhoneNo().length() > phoneNoLength) {
            goodTF = false;
        }
        if (employee.getEmailUserName().length() > emailUserNameLength) {
            goodTF = false;
        }
        if (employee.getSpecifications().length() > specificationsLength) {
            goodTF = false;
        }
        return goodTF;
    }

    @FXML
    public void backOnClick(){
        Scene scene = btnBack.getScene();
        scene.setRoot(mainScreenParent);
    }

    public void updateEmployeeID(TreeTableColumn.CellEditEvent cellEditEvent) {
    }

    public void updateFirstName(TreeTableColumn.CellEditEvent cellEditEvent) {
    }

    public void updateLastName(TreeTableColumn.CellEditEvent cellEditEvent) {
    }

    public void updateType(TreeTableColumn.CellEditEvent cellEditEvent) {
    }

    public void updateSpecifications(TreeTableColumn.CellEditEvent cellEditEvent) {
    }

    public void updatePhoneNumber(TreeTableColumn.CellEditEvent cellEditEvent) {
    }

    public void updateEmailUsername(TreeTableColumn.CellEditEvent cellEditEvent) {
    }

    public void updateEmailPreferences(TreeTableColumn.CellEditEvent cellEditEvent) {
    }

    @FXML
    public void setNewEmpID(ActionEvent actionEvent) {
        // not needed
    }

    @FXML
    public void addBlankEmployeeOnClick(ActionEvent actionEvent) {
        if(employeeLinkedList.isEmpty()){
            loadListOnClick(); // loads if not loaded
        }
        // adds the employee if there is an ID
        if(!newEmpIDTF.getText().equals("")){
            // and that ID is not in the table
            if(Employee.getEmpFromID(employeeLinkedList, newEmpIDTF.getText())==null){
                warninglbl.setText("");
                if(newEmpIDTF.getText().length()<=10){
                    // then the employee can be added to the DB
                    Employee employee = new Employee(newEmpIDTF.getText(), "", "", "", "",false,"","",false);
                    try {
                        EmployeeDB.addEmployeeDB(employee);
                        updateTable(); // table is then updated
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    warninglbl.setText("Employee ID is too long, must be less than " + empIDLength + " characters." );
                }
            }
            else{
                warninglbl.setText("Employee ID already exists");
            }
        }
    }

    @FXML
    public void deleteSelectedEmployeeOnClick(ActionEvent actionEvent) {
        if(employeeLinkedList==null){
            loadListOnClick(); // loads if not loaded
        }
        ObservableList<TreeTablePosition<Employee,?>> employeesSelected = treeTableEmployees.getSelectionModel().getSelectedCells();
        for(int i = 0; i< employeesSelected.size() ; i++){
            TreeItem<Employee> employeeTreeItem = employeesSelected.get(i).getTreeItem();
            Employee deleteEmployee = Employee.getEmpFromID(employeeLinkedList, employeeTreeItem.getValue().getEmpID());
            EmployeeDB.deleteEmployeeDB(deleteEmployee);
            updateTable();
        }
    }
}
