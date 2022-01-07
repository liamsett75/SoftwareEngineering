package UIControllers;

import Databases.NodeDB;
import Databases.ReligiousRequestDB;
import Databases.SanitationRequestDB;
import Employee.Employee;
import RequestFacade.Request;
import com.jfoenix.controls.JFXButton;
import Graph.Node;
import RequestFacade.*;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

//import imaging.Employee.Employee;
//import imaging.RequestFacade.MedicalImagingInfo;
//import imaging.RequestFacade.MedicalImagingRequest;
//import imaging.RequestFacade.Request;
//import imaging.RequestFacade.RequestInfo;
//import RequestFacade.RequestInfo;

public class ProfileController implements Initializable {


    @FXML
    JFXButton btnBack;

    @FXML
    Label empNameLabel;

    @FXML
    Label empIDLabel;

    @FXML
    Label empTypeLabel;

    @FXML
    Label emptyContentLabel;

    @FXML
    Label empPhoneLabel;

    @FXML
    JFXButton btnLoadProfile;

    @FXML
    JFXButton completedSanBtn;

    @FXML
    JFXButton completedRelBtn;

    @FXML
    JFXTabPane profileTabPane;

    @FXML
    Tab sanitationTab;

    @FXML
    Tab religiousTab;


    @FXML
    JFXTreeTableView<SanitationInfo> sanitationTable = new JFXTreeTableView<SanitationInfo>();
    @FXML
    ImageView profileImage;


    JFXTreeTableColumn<SanitationInfo, String> reqIDColSan = new JFXTreeTableColumn<SanitationInfo, String>("Request ID");
    JFXTreeTableColumn<SanitationInfo, String> sanTypeCol = new JFXTreeTableColumn<SanitationInfo, String>("Sanitation Type");
    JFXTreeTableColumn<SanitationInfo, String> timeRequestedColSan = new JFXTreeTableColumn<SanitationInfo, String>("Time Requested");
    JFXTreeTableColumn<SanitationInfo, String> employeeColSan = new JFXTreeTableColumn<SanitationInfo, String>("Employee");
    JFXTreeTableColumn<SanitationInfo, String> biohazardCol = new JFXTreeTableColumn<SanitationInfo, String>("Biohazard?");
    JFXTreeTableColumn<SanitationInfo, String> completedColSan = new JFXTreeTableColumn<SanitationInfo, String>("Is Completed?");

    @FXML
    JFXTreeTableView<ReligiousInfo> religiousTable = new JFXTreeTableView<ReligiousInfo>();

    JFXTreeTableColumn<ReligiousInfo, String> reqIDColRel = new JFXTreeTableColumn<ReligiousInfo, String>("Request ID");
    JFXTreeTableColumn<ReligiousInfo, String> fNameCol = new JFXTreeTableColumn<ReligiousInfo, String>("First Name");
    JFXTreeTableColumn<ReligiousInfo, String> lNameCol = new JFXTreeTableColumn<ReligiousInfo, String>("Last Name");
    JFXTreeTableColumn<ReligiousInfo, String> religionCol = new JFXTreeTableColumn<ReligiousInfo, String>("Religion");
    JFXTreeTableColumn<ReligiousInfo, String> religiousActivityCol = new JFXTreeTableColumn<ReligiousInfo, String>("Religious Activity");
    JFXTreeTableColumn<ReligiousInfo, String> timeRequestedColRel = new JFXTreeTableColumn<ReligiousInfo, String>("Time Requested");
    JFXTreeTableColumn<ReligiousInfo, String> employeeColRel = new JFXTreeTableColumn<ReligiousInfo, String>("Employee");
    JFXTreeTableColumn<ReligiousInfo, String> completedColRel = new JFXTreeTableColumn<ReligiousInfo, String>("Is Completed?");


    private LinkedList<SanitationRequest> sanRequestLL = new LinkedList<SanitationRequest>();
    private ObservableList<SanitationInfo> sanInfoLL = FXCollections.observableArrayList();

    private LinkedList<ReligiousRequest> relRequestLL = new LinkedList<ReligiousRequest>();
    private ObservableList<ReligiousInfo> relInfoLL = FXCollections.observableArrayList();

    @FXML MainScreenController mainScreenController;

    public ProfileController(){

    }

    // Hashmap of links to photos
    HashMap<String, String> profilePics = new HashMap<>();

    public void setMainScreenController(MainScreenController mc){
        this.mainScreenController = mc;
    }

    @FXML static Parent mainScreenParent;

    public static void setMainScreenParent(Parent mainScreenParent) {
        ProfileController.mainScreenParent = mainScreenParent;
    }

    public LinkedList<SanitationRequest> getSanRequestLL() {
        return sanRequestLL;
    }

    public void setSanRequestLL(LinkedList<SanitationRequest> sanRequestLL) {
        this.sanRequestLL = sanRequestLL;
    }

    public JFXButton getBtnBack() {
        return btnBack;
    }

    public void setBtnBack(JFXButton btnBack) {
        this.btnBack = btnBack;
    }

    public JFXTreeTableColumn<ReligiousInfo, String> getfNameCol() {
        return fNameCol;
    }

    public void setfNameCol(JFXTreeTableColumn<ReligiousInfo, String> fNameCol) {
        this.fNameCol = fNameCol;
    }

    public JFXTreeTableColumn<ReligiousInfo, String> getlNameCol() {
        return lNameCol;
    }

    public void setlNameCol(JFXTreeTableColumn<ReligiousInfo, String> lNameCol) {
        this.lNameCol = lNameCol;
    }

    public JFXTreeTableColumn<ReligiousInfo, String> getReligionCol() {
        return religionCol;
    }

    public void setReligionCol(JFXTreeTableColumn<ReligiousInfo, String> religionCol) {
        this.religionCol = religionCol;
    }

    public JFXTreeTableColumn<ReligiousInfo, String> getReligiousActivityCol() {
        return religiousActivityCol;
    }

    public void setReligiousActivityCol(JFXTreeTableColumn<ReligiousInfo, String> religiousActivityCol) {
        this.religiousActivityCol = religiousActivityCol;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image(getClass().getResourceAsStream("/Images/user_white_icon.png"));
        profileImage.setImage(image);

        // Values are overridden since you can't have duplicate keys
        profilePics.put("448256835", "/Images/ProfilePics/pic_brolfes.png"); // Brent
        profilePics.put("530998872", "/Images/ProfilePics/pic_igrigolia.png"); // Irakli
        profilePics.put("451924104", "/Images/ProfilePics/pic_lsetterlund.png"); // Liam
        profilePics.put("407791834", "/Images/ProfilePics/pic_cwolanin.png"); // Cooper
        profilePics.put("424429937", "/Images/ProfilePics/pic_pkumi.png"); // Petra
        profilePics.put("606809822", "/Images/ProfilePics/pic_cenright.png"); // Caitlin
        profilePics.put("429753250", "/Images/ProfilePics/pic_aqchan.png"); // Amanda
        profilePics.put("275347314", "/Images/ProfilePics/pic_dmrice.png"); // Deanna
        profilePics.put("738085992", "/Images/ProfilePics/pic_kgzellerbach.png"); // Kit
        profilePics.put("196143994", "/Images/ProfilePics/pic_kcbimonte.png"); // Kevin
        //profilePics.put("355059219", "/Images/ProfilePics/pic_bgmsadoques.png"); // Ben CAN'T ADD BECAUSE DOESN'T EXIST IN DATABASE
        profilePics.put("staff", "/Images/ProfilePics/pic_wwong2.png");     // Prof. Wong


        hideShowTables();

        reqIDColSan.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SanitationInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SanitationInfo, String> param) {
                return param.getValue().getValue().getReqID();
            }
        });

        sanTypeCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SanitationInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SanitationInfo, String> param) {
                return param.getValue().getValue().getSanType();
            }
        });

        biohazardCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SanitationInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SanitationInfo, String> param) {
                boolean tempBio = param.getValue().getValue().isBiohazard();
                if (tempBio) {
                    return new SimpleStringProperty("Yes");
                }
                else {
                    return new SimpleStringProperty("No");
                }
            }
        });

        timeRequestedColSan.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SanitationInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SanitationInfo, String> param) {
                return param.getValue().getValue().getTimeRequested();
            }
        });

        employeeColSan.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SanitationInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SanitationInfo, String> param) {
                return param.getValue().getValue().getCompletedByWhoID();
            }
        });


        completedColSan.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SanitationInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SanitationInfo, String> param) {
                boolean tempCompleted = param.getValue().getValue().isHasBeenCompleted();
                if (tempCompleted) {
                    return new SimpleStringProperty("Yes");
                }
                else {
                    return new SimpleStringProperty("No");
                }
            }
        });

        //---------------------------------------------------------------------------------

        /*
            JFXTreeTableColumn<ReligiousInfo, String> reqIDColRel = new JFXTreeTableColumn<ReligiousInfo, String>("Request ID");
    JFXTreeTableColumn<ReligiousInfo, String> fNameCol = new JFXTreeTableColumn<ReligiousInfo, String>("First Name");
    JFXTreeTableColumn<ReligiousInfo, String> lNameCol = new JFXTreeTableColumn<ReligiousInfo, String>("Last Name");
    JFXTreeTableColumn<ReligiousInfo, String> religionCol = new JFXTreeTableColumn<ReligiousInfo, String>("Religion");
    JFXTreeTableColumn<ReligiousInfo, String> religiousActivityCol = new JFXTreeTableColumn<ReligiousInfo, String>("Religious Activity");
    JFXTreeTableColumn<ReligiousInfo, String> completedColRel = new JFXTreeTableColumn<ReligiousInfo, String>("Is Completed?");

         */

        reqIDColRel.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ReligiousInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ReligiousInfo, String> param) {
                return param.getValue().getValue().getReqID();
            }
        });

        fNameCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ReligiousInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ReligiousInfo, String> param) {
                return param.getValue().getValue().getPatientFName();
            }
        });

        lNameCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ReligiousInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ReligiousInfo, String> param) {
                return param.getValue().getValue().getPatientLName();
            }
        });

        religionCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ReligiousInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ReligiousInfo, String> param) {
                return param.getValue().getValue().getReligion();
            }
        });

        religiousActivityCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ReligiousInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ReligiousInfo, String> param) {
                return param.getValue().getValue().getReligiousActivity();
            }
        });


        timeRequestedColRel.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ReligiousInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ReligiousInfo, String> param) {
                return param.getValue().getValue().getTimeRequested();
            }
        });

        employeeColRel.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ReligiousInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ReligiousInfo, String> param) {
                return param.getValue().getValue().getCompletedByWhoID();
            }
        });


        completedColRel.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ReligiousInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ReligiousInfo, String> param) {
                boolean tempCompleted = param.getValue().getValue().isHasBeenCompleted();
                if (tempCompleted) {
                    return new SimpleStringProperty("Yes");
                }
                else {
                    return new SimpleStringProperty("No");
                }
            }
        });

        //------------------------------------------------------------------------------

        refreshSanitation();
        refreshReligious();
    }

    private void updateProfilePic() {
        String filePath;

        if(profilePics.get(MainScreenController.getCurEmployee().getEmpID()) != null) {
            filePath = profilePics.get(MainScreenController.getCurEmployee().getEmpID());
        }
        else {
            filePath = "/Images/user_white_icon.png";
        }

        Image image = new Image(getClass().getResourceAsStream(filePath));
        profileImage.setImage(image);
    }

    private String phoneNumFormat(String phoneNum) {
        return phoneNum.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
    }

    public void refreshSanitation() {
        sanRequestLL.clear();
        sanInfoLL.clear();

        //sanRequestLL = MedicalImagingRequestsDB.getAllMedicalImagingRequests();
        //change this to use bio hazard hashmap
        sanRequestLL = SanitationRequestDB.getAllSanitationRequestsHashMap();



        for (SanitationRequest r : sanRequestLL) {
            SanitationInfo temp = SanitationInfo.requestToInfo(r);
//            System.out.println("Temp biohaz value is " + temp.isBiohazard());
            sanInfoLL.add(temp);
        }

        final TreeItem<SanitationInfo> root = new RecursiveTreeItem<SanitationInfo>(sanInfoLL, RecursiveTreeObject::getChildren);
        sanitationTable.getColumns().setAll(reqIDColSan, sanTypeCol, biohazardCol, timeRequestedColSan, employeeColSan, completedColSan);
        sanitationTable.setRoot(root);
        sanitationTable.setShowRoot(false);

    }

    public void refreshReligious() {
        relRequestLL.clear();
        relInfoLL.clear();

        relRequestLL = ReligiousRequestDB.getAllReligiousRequests();
        //ADD DB LINE HERE

        for (ReligiousRequest r : relRequestLL) {
//            System.out.println("Requester id is " + r.getRequester().getEmpID());
            ReligiousInfo temp = ReligiousInfo.requestToInfo(r);
            relInfoLL.add(temp);
        }

        final TreeItem<ReligiousInfo> root = new RecursiveTreeItem<ReligiousInfo>(relInfoLL, RecursiveTreeObject::getChildren);
        religiousTable.getColumns().setAll(reqIDColRel, fNameCol, lNameCol, religionCol, religiousActivityCol, timeRequestedColRel,employeeColRel, completedColRel);
        religiousTable.setRoot(root);
        religiousTable.setShowRoot(false);

    }

    @FXML
    public void markCompletedSanOnClick(ActionEvent actionEvent) {
        ObservableList<TreeTablePosition<SanitationInfo, ?>> requestsSelected = sanitationTable.getSelectionModel().getSelectedCells();
        SanitationRequest tempRequest = null;

        for (int i = 0; i < requestsSelected.size(); i++) {
            TreeItem<SanitationInfo> requestTreeItem = requestsSelected.get(i).getTreeItem();
            SanitationInfo tempInfo = requestsSelected.get(i).getTreeItem().getValue();
            tempRequest = SanitationRequestDB.getSanitationRequestHashMap(tempInfo.getReqID().getValue());
            tempInfo.setHasBeenCompleted(true);

            if(tempRequest.getIsBiohazard())
            {
                try {
//                    System.out.println("++++++++++++++++ temp req req id is " + tempRequest.getReqID());

                    System.out.println(SanitationController.getBhNodeHM().keySet());

                    Node tempBHNode = SanitationController.getBhNodeHM().get(tempRequest.getReqID());

//                    System.out.println("------------------Temp bh ndode id is " + tempBHNode.getNodeID());

                    NodeDB.addNodeDB(tempBHNode);

                    //mainScreenController.adminController.refresh();

                }catch (SQLException e)
                {
                    e.printStackTrace();
                }

            }

           // Request.addRequest(tempRequest);
            //MedicalImagingRequestsDB.setRequestCompletedDB(tempRequest.getReqID(), true);
            //System.out.println();
//            System.out.println("Request ID in markCompleted san is " + tempRequest.getReqID());
            SanitationRequestDB.setSanitationRequestCompletedDB(tempRequest.getReqID(), true);

            refreshSanitation();
//            System.out.println("================== about to do this");
            HashMap<String, Node> tempHashMap = SanitationController.getBhNodeHM();
//            System.out.println(tempHashMap.keySet());
            tempHashMap.remove(tempRequest.getReqID());
//            System.out.println("request from hash map ws sucessful");
            SanitationController.setBhNodeHM(tempHashMap);

        }
    }

    @FXML
    public void markCompletedRelOnClick(ActionEvent actionEvent) {
       ObservableList<TreeTablePosition<ReligiousInfo, ?>> requestsSelected = religiousTable.getSelectionModel().getSelectedCells();
        Request tempRequest = null;

        for (int i = 0; i < requestsSelected.size(); i++) {
            TreeItem<ReligiousInfo> requestTreeItem = requestsSelected.get(i).getTreeItem();
            ReligiousInfo tempInfo = requestsSelected.get(i).getTreeItem().getValue();
            tempRequest = ReligiousRequestDB.getReligiousRequest(tempInfo.getReqID().getValue());
            tempInfo.setHasBeenCompleted(true);
            // Request.addRequest(tempRequest);
            //MedicalImagingRequestsDB.setRequestCompletedDB(tempRequest.getReqID(), true);
            //System.out.println();
            ReligiousRequestDB.setReligiousRequestCompletedDB(tempRequest.getReqID(), true);
            refreshReligious();
        }
    }

    //HOW DO WE MAKE ONE BUTTON DO BOTH?
    //CAN WE HAVE TWO BUTTONS IN THE SAME PLACE THAT SWICTH VISIBILITY
    //FOR NOW MAKE TWO DIFFERENT BUTTONS AND FUNCTIONS

    //NOW NEED TO MAKE DB FOR RELIGIOS REQUETS

    @FXML
    public void backOnClick() {
        Scene scene = btnBack.getScene();
        scene.setRoot(mainScreenParent);
    }

    @FXML
    public void loadListOnClick() {
        Employee e = MainScreenController.getCurEmployee();

        empNameLabel.setText(e.getFirstName() + " " + e.getLastName());
        empIDLabel.setText(e.getEmpID());
        empTypeLabel.setText(e.getEmpType());
        empPhoneLabel.setText(phoneNumFormat(e.getPhoneNo()));
        updateProfilePic();

        refreshSanitation();
    }

    @FXML
    public void hideShowTables(){
        Employee e = MainScreenController.getCurEmployee();
       if (e.getEmpType().equals("Admin")) {
           //change nothing
           emptyContentLabel.setVisible(false);
       }
       else if ((e.getEmpType().equals("SAN"))){
           profileTabPane.getTabs().remove(religiousTab);
           emptyContentLabel.setVisible(false);
       }
       else if ((e.getEmpType().equals("REL"))){
           profileTabPane.getTabs().remove(sanitationTab);
           emptyContentLabel.setVisible(false);
       }
       else {
           profileTabPane.getTabs().remove(sanitationTab);
           profileTabPane.getTabs().remove(religiousTab);
           emptyContentLabel.setVisible(true);
       }

    }



}



