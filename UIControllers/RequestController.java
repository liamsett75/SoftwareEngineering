package UIControllers;

import Databases.EmployeeDB;
import Employee.Employee;
import RequestFacade.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class RequestController implements Initializable {

    //Updates these to be Request and appropriate type
        @FXML TableView<Request> tableViewRequests;
        @FXML TableColumn<Request, String> reqIDCol;
        @FXML TableColumn<Request, String> serviceTypeCol;
        @FXML TableColumn<Request, Boolean> completedCol; //will this boolean thing work??
        @FXML TableColumn<Request, String> completedByWhoCol;
    private static ObservableList<Request> tableRequestList = FXCollections.observableArrayList();

        @FXML
        Button btnMarkCompleted;
        @FXML
        Button btnLoadReqIDTF;

        @FXML
        TextField reqIDTF, serviceTypeTF, completedTF, enterReqIDTF;

       @FXML
        Label errorLabelReq;

        Request curRequest;
    public String employeeName;

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }


        boolean hasRequest = false;
       // String nodeID, building, nodeType, longName, shortName, floor;

        //shuld these be of type Calendar
        String reqID;
        String serviceType;
        String hasBeenCompleted;
        String completedByWho;
//    Employee requester;
//    Node location;
//    Calendar timeRequested;
//    Calendar scheduledTimeStart;
//    Calendar scheduledTimeEnd;
//    Calendar actualTimeStart;
//    Calendar actualTimeEnd;
    //boolean hasBeenCompleted;


        Boolean goodTF, goodNumbers;


       // @Override
        public void initialize(URL location, ResourceBundle resources) {
//            System.out.println("Init Admin Request Controller");

               //public Request (String reqID, String serviceType, Employee requester, Node location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart, Calendar actualTimeEnd, boolean hasBeenCompleted) {

                reqIDCol.setCellValueFactory(new PropertyValueFactory<>("reqID"));
            serviceTypeCol.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
//            empIDCol.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
//            nodeIDCol.setCellValueFactory(new PropertyValueFactory<>("locationID"));
//            timeReqCol.setCellValueFactory(new PropertyValueFactory<>("timeRequested"));
//            schedStartCol.setCellValueFactory(new PropertyValueFactory<>("scheduledStart"));
//            schedEndCol.setCellValueFactory(new PropertyValueFactory<>("scheduledEnd"));
//            actualStartCol.setCellValueFactory(new PropertyValueFactory<>("actualStart"));
//            actualEndCol.setCellValueFactory(new PropertyValueFactory<>("actualEnd"));
            completedCol.setCellValueFactory(new PropertyValueFactory<>("hasBeenCompleted"));
            completedByWhoCol.setCellValueFactory(new PropertyValueFactory<>("completedByWho"));

            setDisableTF(true);
//            btnEdit.setDisable(true);
//            btnDelete.setDisable(true);
//            btnAdd.setDisable(true);
            btnMarkCompleted.setDisable(true);

//            onlyDigits(xCoordTF);
//            onlyDigits(yCoordTF);

//            System.out.println("gets to before read on click");
            readOnClick();


//        loadOnClick();
//        System.out.println("loaded");
//        readOnClick();
//        System.out.println("read");
//        populateTableWithNodes();
        }


        public void readOnClick(){
            //NodeReader nodeReader = new NodeReader();

            //nodeReader.readFile("nodesv3.csv");
            //System.out.println("file has been read");
//            System.out.println("gets to before pop table");
            populateTableWithRequests();
            btnMarkCompleted.setDisable(false);
//            System.out.println("read on click has been finished");
        }

        @FXML
        public void markCompleteOnClick(){
            LinkedList<Request> requestList = Request.getAllRequests();

            String curReqID = reqIDTF.getText();

            for(Request r: requestList)
            {
                if (r.getReqID().equals(curReqID))
                {
                    r.setHasBeenCompleted(true);
                    employeeName = "test: replace name";

//                    System.out.println("employee name is " + employeeName);
                    String[]empFirstLast = employeeName.split("\\s+");
                    r.setCompletedByWho(Employee.getEmpFromName(EmployeeDB.getDBEmployees(), empFirstLast[0], empFirstLast[1]));
                }
            }

            updateTable();


        }

        public void updateTable(){
            tableRequestList.clear();
            populateTableWithRequests();
        }

        //private void populateTableWithRequests(){
        private void populateTableWithRequests(){
            LinkedList<Request> requestList = Request.getAllRequests();

//            System.out.println("sise of request list is " + requestList.size());

            for(Request r: requestList)
            {

                tableRequestList.add(r);

                System.out.println(r.getReqID());
            }
//            System.out.println("List length is " + requestList.size());
//            System.out.println("Table length is " + tableRequestList.size());
            tableViewRequests.setItems(tableRequestList);
        }

        @FXML
        public void loadRequestOnClick(){
            if (enterReqIDTF.getText().length() < 1 || enterReqIDTF.getText().length() > 15) {
                errorLabelReq.setText("Enter a valid nodeID length");
                btnMarkCompleted.setDisable(true); //needed if the user enters valid name then a non valid one
            } else {
                hasRequest = false;

                LinkedList<Request> requestList = Request.getAllRequests();

                for (Request r : requestList) {
                    if (r.getReqID().equals(enterReqIDTF.getText())) {
                        hasRequest = true;
                        curRequest = r;
                    }
                }

                if (hasRequest) {
                    setDisableTF(false);
                    reqIDTF.setDisable(true);
                    btnMarkCompleted.setDisable(false);
                    reqIDTF.setText(enterReqIDTF.getText());
                    errorLabelReq.setText("Request Found");
                    reqID = curRequest.getReqID();
                    serviceType = curRequest.getServiceType();

                    if(curRequest.getHasBeenCompleted())
                        hasBeenCompleted = "T";
                    else
                        hasBeenCompleted = "F";


                    reqIDTF.setText(reqID);
                    serviceTypeTF.setText(serviceType);
                    completedTF.setText(hasBeenCompleted);

                } else { //if the given Node ID doesn't match a current node in the LL
                    errorLabelReq.setText("Request Not Found. Create new node?");
                    setDisableTF(false);
                    reqIDTF.setDisable(true);
                    btnMarkCompleted.setDisable(true);

                }
            }

        }


        //reqIDTF, serviceTypeTF, completedTF;
        public void setDisableTF(boolean value){
            reqIDTF.setDisable(value);
            serviceTypeTF.setDisable(value);
            completedTF.setDisable(value);

        }


}
