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
import java.util.stream.Stream;
import kafka.PubSubLogic;
import javafx.collections.FXCollections;
public class FxApplication extends Application{

    private final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;

    private final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

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

    private TextField titleTextField;//A widget intended to hold the value of the AnalysisText title
    private PubSubLogic pubSubLogic;
    private ComboBox<String>typeComboBox; //A widget intended to hold the value of the AnalysisText title
    private void initProps(){
        analyseButton = new Button("Analyse");
        settingsGridPane = new GridPane();
        graphGridPane = new GridPane();
        analysisTextArea = new TextArea();
        analysisBorderPane = new BorderPane();
        analysisTextArea.setTooltip(new Tooltip("Enter the text to analyse here"));
        analysisGridPane = new GridPane();
        analysisBorderPane = new BorderPane();
        characterCountLabel.setText("0/3000");
        pubSubLogic = PubSubLogic.getInstance();

        titleTextField = new TextField();
        //Populated with some dummy items for now
        typeComboBox = new ComboBox<>(FXCollections.observableArrayList("Social Media","News","Article"));
    }

    private BarChart<String,Integer> produceBarChart(){
        //An axis to hold the words present on each axis
        CategoryAxis xCategoryAxis = new CategoryAxis();
        NumberAxis yNumberAxis = new NumberAxis();

        xCategoryAxis.setCategories(FXCollections.observableArrayList());

        yNumberAxis.setLowerBound(0);
        yNumberAxis.setUpperBound(1);
        BarChart<String,Integer> barChart = new BarChart<String,Integer>();
        return null;

        // TODO: Consider the integration of another technology
    }

    //i.e add content to each of the widgets
    private void populateWidgets(){
        analysisBorderPane.setCenter(analysisTextArea);
        analysisBorderPane.setBottom(analyseButton);
        analysisGridPane.add(analyseButton,0,0);
        analysisGridPane.add(characterCountLabel,0,1);
    }

    private void initListeners(){
        analysisTextArea.setOnKeyTyped(event ->
                characterCountLabel.setText(analysisTextArea.getText().length() + "/3000"));

        analyseButton.setOnMousePressed(event ->{
            Platform.runLater(()->{
                AnalysisText analysisText =
                        new AnalysisText(titleTextField.getText().trim())
                        .setId(UUID.randomUUID())
                        .setContent(analysisTextArea.getText().trim())
                        .setSentLocalDateTime(LocalDateTime.now())
                        .setType(typeComboBox.getValue());

               pubSubLogic.sendMessage(PubSubLogic.TOPIC_UNPROCESSED,analysisTextArea.getText().trim());
            });
        });
    }
    public FxApplication(){
      initProps();
      populateWidgets();
      initListeners();
    }
    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage){
        Scene scene = new Scene(new BorderPane());
        primaryStage.setScene(scene);
    }
}
