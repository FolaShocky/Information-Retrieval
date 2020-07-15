package app;
import analysistext.AnalysisText;
import javafx.application.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.chart.*;
import java.awt.Toolkit;
import java.time.LocalDateTime;
import java.util.*;

import kafka.PubSubLogic;
import javafx.collections.FXCollections;
import org.springframework.boot.SpringApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class FxApplication extends Application{

    private final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;

    private final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    private Scene analysisScene;

    private Button analyseButton;
    //Set to be at the top of the GUI
    private GridPane settingsGridPane;
    //Set to be at the centre of the GUI
    private GridPane graphGridPane;
    //Set to be at the bottom of the GUI
    private BorderPane analysisBorderPane;

    //This widget is a child of the analysisBorderPane
    private GridPane analysisGridPane;

    private TextArea analysisTextArea;

    private Label characterCountLabel;

    private TextField analysisTitleTextField;//A widget intended to hold the value of the AnalysisText title
    private PubSubLogic pubSubLogic;
    private RestTemplate restTemplate;

    private ComboBox<String>typeComboBox; //A widget intended to hold the value of the AnalysisText title
    private void initProps(){
        analyseButton = new Button("Analyse");
        settingsGridPane = new GridPane();
        graphGridPane = new GridPane();
        analysisTextArea = new TextArea();
        analysisBorderPane = new BorderPane();
        analysisGridPane = new GridPane();
        characterCountLabel = new Label("0/3000");
        analysisScene = new Scene(analysisBorderPane);
        pubSubLogic = PubSubLogic.getInstance();

        analysisTitleTextField = new TextField();
        typeComboBox = new ComboBox<>(FXCollections.observableArrayList("Social Media","News","Article"));
        restTemplate = new RestTemplate();
    }

    private BarChart<String,Integer> produceBarChart(){
        //An axis to hold the words present on each axis
        CategoryAxis xCategoryAxis = new CategoryAxis();
        NumberAxis yNumberAxis = new NumberAxis();

        xCategoryAxis.setCategories(FXCollections.observableArrayList());

        yNumberAxis.setLowerBound(0);
        yNumberAxis.setUpperBound(1);
        //BarChart<String,Integer> barChart = new BarChart<String,Integer>();
        return null;

        // TODO: Consider the integration of another technology
    }

    //i.e add content to each of the widgets
    private void populateWidgets(){
        analysisGridPane.add(analysisTitleTextField,0,0);
        analysisGridPane.add(analysisTextArea,0,1);
        analysisGridPane.add(analyseButton,0,2);
        analysisBorderPane.setCenter(analysisGridPane);

        analysisTextArea.setTooltip(new Tooltip("Enter the text to analyse here"));

        analysisScene.setRoot(analysisBorderPane);
    }

    private void initListeners(){
        analysisTextArea.setOnKeyTyped(event ->
                characterCountLabel.setText(analysisTextArea.getText().length() + "/3000"));

        analyseButton.setOnMousePressed(event ->{
            AnalysisText analysisText =
                    new AnalysisText(analysisTitleTextField.getText().trim()).setId(UUID.randomUUID())
                            .setContent(analysisTextArea.getText().trim())
                            .setSentLocalDateTime(LocalDateTime.now())
                            .setType(typeComboBox.getValue());
            Platform.runLater(()->{
               pubSubLogic.sendMessage(PubSubLogic.TOPIC_UNPROCESSED,analysisText.getContent());
               sendData(analysisText.getId().toString(),analysisText.getTitle(),analysisText.getContent(),
                       analysisText.getType(),analysisText.getSentLocalDateTime());
            });
        });
    }

    private boolean sendData(String id, String title,String content, String type,LocalDateTime sentLocalDateTime){
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<AnalysisText> analysisTextHttpEntity = new HttpEntity<>(httpHeaders);
            analysisTextHttpEntity.getBody();
            return restTemplate.postForEntity(
                    "http://localhost:8080/sendLatestAnalysisText?"
                            + Constants.ANALYSIS_TEXT_FIELD_ID + "=" + id + "&"
                            + Constants.ANALYSIS_TEXT_FIELD_TITLE + "=" + title + "&"
                            + Constants.ANALYSIS_TEXT_FIELD_CONTENT + "=" + content + "&"
                            + Constants.ANALYSIS_TEXT_FIELD_TYPE + "=" + type + "&"
                            + Constants.ANALYSIS_TEXT_FIELD_SENT_LOCAL_DATE_TIME + "=" + sentLocalDateTime, new AnalysisText(), AnalysisText.class)
                    .getStatusCode().is2xxSuccessful();
        }
        catch (Exception ex){
            System.out.println("An exception occurred: " + ex.getMessage());
            return false;
        }

    }
    public FxApplication(){
      initProps();
      populateWidgets();
      initListeners();
    }
    public static void main(String[] args){
        launch(args);
        Platform.runLater(()-> SpringApplication.run(FxApplication.class,args));


    }
    @Override
    public void start(Stage primaryStage){
        FxApplication fxApplication = new FxApplication();
        primaryStage.setScene(fxApplication.analysisScene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
