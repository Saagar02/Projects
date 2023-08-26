import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SelectionSort extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    private int Array_Size = 100;
    private  int[] values = new int[Array_Size];
    private Element[] elements;
    //Containers
    private Canvas canvas;
    private GraphicsContext g;
    private BorderPane pane;
    private Scene scene;
    private HBox topNode;
    private VBox midNode;
    private VBox screenNode;
    private HBox bottomNode;
    private TextField inputTextField;
    private Text numberText;
    private Rectangle rectangle;
    private Rectangle minNo;
    private Rectangle tempNo;
    private StackPane sp;
    private StackPane sp1;
    private StackPane sp2;
    private HBox iterationHbox;
    private HBox indexBox;
    private HBox rectangleBox;
    private Pane intmin;
    private Pane inttemp;
    private HBox store;
    private TextField inputFields;
    private HBox arrayInputBox;
    private HBox inputBox;
    private VBox buttonsBox;
    private HBox bottomBox;
    private HBox button ;
    //Buttons
    private Button warningButton;
    private Button addElementButton;
    private Button submitButton;
    private Button startButton;
    private Button pauseButton;
    private Button resetButton;
    //Flags
    private volatile boolean inputFlag = false;
    private volatile boolean isPaused = false;
    private volatile boolean isSubmitted = false;
    private volatile boolean isSorting = false;
    private volatile boolean isReset = false;
    // Sorting Values
    private Label iterationLabel;
    private int minIndex = 0;
    private int temp = 0;
    private int OuterIterator = 0;
    Thread sortingThread;
    private int speed = 2;
    private int sleepTime = 1000 * speed;

    //String buttonStyle = "-fx-background-color: #CC0033; -fx-text-fill: white; -fx-font-family: \"Courier New\"; -fx-font-weight: bold; -fx-font-size: 30px;";
    Font font = Font.font("Courier New", FontWeight.BOLD, 30);
    String style = "-fx-background-radius: 10;-fx-background-color: #ff6393;-fx-text-fill : #FFFFFF ; -fx-text-font-family : sans-serif;";
    
    public void start(Stage stage){

        // Top Node
        Label title = new Label("Selection Sort Algorithm");
        Label title2 = new Label("Core2Web");
        Label title3 = new Label("By : Sagar Chaudhari");
        title.setPadding(new Insets(0,0,0,280));
        title3.setPadding(new Insets(50,0,0,150));
        //title3.setAlignment(Pos.BOTTOM_CENTER);

        title.setStyle("-fx-font-family : \"DejaVu Serif\";-fx-font-weight : bold;-fx-text-fill :white ;-fx-font-size : 50px");
        title2.setStyle("-fx-font-family : \"DejaVu Serif\";-fx-font-weight : bold;-fx-text-fill :yellow ;-fx-font-size : 50px");
        title3.setStyle("-fx-font-family : \"DejaVu Serif\";-fx-font-weight : bold;-fx-text-fill :white ;-fx-font-size : 35px");

        HBox label1 = new HBox(title2);
        label1.setAlignment(Pos.CENTER_LEFT);
        label1.setPadding(new Insets(0,50,0,20));
        HBox label2 = new HBox(title);
        label2.setAlignment(Pos.CENTER);

        topNode = new HBox(label1,label2 , title3);
        topNode.setPrefHeight(100);
        topNode.setAlignment(Pos.CENTER_LEFT);
        topNode.setStyle("-fx-background-color: #6A5ACD;");

        // Mid Node
        canvas = new Canvas(950, 600);
        g = canvas.getGraphicsContext2D();
            //BubbleSortImage
        Image img = new Image ("selectionSort.png");
        ImageView imageView = new ImageView(img);
        HBox imagePane = new HBox(imageView);
        imagePane.setAlignment(Pos.CENTER);
        imagePane.setPadding(new Insets(0,0,60,0));
            //WarningButton
        warningButton= new Button();
        warningButton.setStyle("-fx-background-color: transparent ;-fx-Font-size:15px;-fx-font-weight:bold;");
            //Combining
        midNode = new VBox(imagePane,warningButton);
        midNode.setPrefHeight(500);
        midNode.setAlignment(Pos.BOTTOM_CENTER);
        midNode.setStyle("-fx-background-color: #f6ebff");

        //Bottom Node

        inputTextField = new TextField();
        inputTextField.setStyle("-fx-font-weight: bold;-fx-font-size: 30px;-fx-background-color: #F5F5F5;-fx-border-color:#6A5ACD;-fx-border-width: 2px;");
        inputTextField.setPrefWidth(200);

        addElementButton = new Button("Add Elements");
        //addElementButton.setPrefWidth(300);
        //addElementButton.setPrefHeight(60);
        addElementButton.setPrefSize(300,60);
        addElementButton.setStyle(style);
        addElementButton.setFont(font);

        addElementButton.setOnAction(e->{
            elementInput(inputTextField);
        });

        bottomNode = new HBox(inputTextField,addElementButton);
        bottomNode.setAlignment(Pos.CENTER);
        bottomNode.setSpacing(50);
        bottomNode.setStyle("-fx-background-color: #6A5ACD;");
        bottomNode.setPrefHeight(200);

        pane = new BorderPane();
        pane.setTop(topNode);
        pane.setCenter(midNode);
        pane.setBottom(bottomNode);
        pane.setTop(topNode);        
        scene = new Scene(pane, 1600, 1250);
        stage.setScene(scene);
        stage.show();

    }
    public void changeScreen(){
        String labelStyle = "-fx-font-size: 30px; -fx-text-fill: #00286c ; -fx-font-family: sans-serif; -fx-font-weight : bold";

        // Color Indications

        TilePane ColorTile = new TilePane();
        ColorTile.setHgap(20);
        ColorTile.setVgap(20);
        ColorTile.setPrefColumns(2);
        ColorTile.setPrefRows(2);

        
        Label il1 = new Label("Outer Loop Iterator : ");
        il1.setStyle(labelStyle);
        Rectangle i1Rectangle = new Rectangle(0,0,40,40);
        i1Rectangle.setFill(Color.web("#ffc45b"));
        HBox il1HBox = new HBox();
        il1HBox.setAlignment(Pos.CENTER);
        il1HBox.getChildren().addAll(il1,i1Rectangle);

        Label il2 = new Label("Inner Loop Iterator : ");
        il2.setStyle(labelStyle);
        Rectangle i2Rectangle = new Rectangle(0,0,40,40);
        i2Rectangle.setFill(Color.web("#d153b8"));
        HBox il2HBox = new HBox();
        il2HBox.setAlignment(Pos.CENTER);
        il2HBox.getChildren().addAll(il2,i2Rectangle);

        Label il3 = new Label("           Sorted Array : ");
        il3.setStyle(labelStyle);
        Rectangle i3Rectangle = new Rectangle(0,0,40,40);
        i3Rectangle.setFill(Color.web("#6B728E"));
        HBox il3HBox = new HBox();
        il3HBox.setAlignment(Pos.CENTER);
        il3HBox.getChildren().addAll(il3,i3Rectangle);

        Label il4 = new Label("      Unsorted Array : ");
        il4.setStyle(labelStyle);
        Rectangle i4Rectangle = new Rectangle(0,0,40,40);
        i4Rectangle.setFill(Color.web("#0084d8"));
        HBox il4HBox = new HBox();
        il4HBox.setAlignment(Pos.CENTER);
        il4HBox.getChildren().addAll(il4,i4Rectangle);

        ColorTile.getChildren().addAll(il1HBox,il2HBox,il3HBox,il4HBox);
    
        // Iteration Lable

        iterationLabel = new Label("Iteration Count : 0");
        iterationLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: #00286c ; -fx-font-family: sans-serif; -fx-font-weight : bold");
        iterationHbox = new HBox();
        iterationHbox.getChildren().addAll(iterationLabel,ColorTile);
        iterationHbox.setSpacing(50);
        iterationHbox.setAlignment(Pos.BASELINE_LEFT);
        iterationHbox.setPadding(new Insets(0,0,50,170));
            //IndexBox
        indexBox = new HBox();
        indexBox.setAlignment(Pos.CENTER);
        
            //RectangleBox
        rectangleBox = new HBox();
        rectangleBox.setPadding(new Insets(0,0,100,0));
        rectangleBox.setAlignment(Pos.CENTER);

            //Int temp , Int min
        intmin = new Pane();
        Label t1 = new Label("int Min");
        t1.setStyle("-fx-font-size: 30px; -fx-text-fill: #00286c; -fx-font-family: sans-serif; -fx-font-weight : bold");
        t1.setAlignment(Pos.CENTER);
            //Min No Rectangle
        minNo = new Rectangle(180, 100);
        minNo.setFill(Color.web("#5b79cc"));
        minNo.setStroke(Color.BLACK);

        intmin.getChildren().addAll(minNo);

            // Temp Variable
        inttemp = new Pane();
        Label t2 = new Label(" int Temp");
        t2.setStyle("-fx-font-size: 30px; -fx-text-fill: #00286c; -fx-font-family: sans-serif; -fx-font-weight : bold");
        t2.setAlignment(Pos.CENTER);

            //Temp Variable Rectangle
        tempNo = new Rectangle(180, 100);
        tempNo.setFill(Color.web("#5b79cc"));
        tempNo.setStroke(Color.BLACK);

        inttemp.getChildren().addAll(tempNo);


        HBox labelBox = new HBox(t1,t2);
        labelBox.setAlignment(Pos.CENTER);
        labelBox.setSpacing(250);
        drawRectangle();
        drawIndex();
        store = new HBox(intmin,inttemp);
        store.setPadding(new Insets(0,0,50,0));
        store.setAlignment(Pos.CENTER);
        store.setSpacing(200);
     
 /*        VBox main = new VBox();
        rectangleBox = new HBox();
        rectangleBox.setAlignment(Pos.CENTER);
        main.getChildren().add(rectangleBox);

        HBox labelBox = new HBox();
        Label t1 = new Label("int Min");
        t1.setStyle("-fx-font-size: 40px; -fx-text-fill: red; -fx-font-family: sans-serif; -fx-font-weight : bold");
        t1.setAlignment(Pos.CENTER);
        Label t2 = new Label("int temp");
        t2.setStyle("-fx-font-size: 40px; -fx-text-fill: red; -fx-font-family: sans-serif; -fx-font-weight : bold");
        t2.setAlignment(Pos.CENTER);
        labelBox.getChildren().addAll(t1,t2);
        labelBox.setAlignment(Pos.CENTER);
        main.getChildren().add(labelBox);

        //intmin = new VBox();
        //inttemp = new VBox();
        intmin = new HBox();
        main.getChildren().add(intmin);
        drawRectangle();*/
        warningButton.setPadding(new Insets(20,20,20,20));
        screenNode = new VBox(iterationHbox,indexBox,rectangleBox,labelBox,store,warningButton);
        screenNode.setStyle("-fx-background-color: #f6ebff");
        //screenNode = new VBox(rectangleBox,labelBox,store);
        screenNode.setSpacing(0);
        screenNode.setAlignment(Pos.BOTTOM_CENTER);


     
        //BottomNode
        startButton = new Button("Start");
        startButton.setOnAction(e -> {
            if (inputFlag == true) {
                warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : Green ; -fx-text-font-family : sans-serif;-fx-font-size:30px;-fx-font-weight:bold;");
                warningButton.setText("Sorting Started!");
                doSelectionSort(); // Start the sorting process
            } else {
                warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : Red ; -fx-text-font-family : sans-serif;-fx-font-size:30px;-fx-font-weight:bold;");
                warningButton.setText("Please Enter Array Elements");
            }
        });
        startButton.setStyle(style);
        startButton.setFont(font);
        startButton.setPrefSize(250,60);

        pauseButton = new Button("Pause");
        pauseButton.setOnAction(e -> {
            if (!isPaused) {
                warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : Red ; -fx-text-font-family : sans-serif;-fx-font-size:30px;-fx-font-weight:bold;");
                warningButton.setText("Sorting Puased");
                pauseButton.setText("Resume");
                isPaused = true;
            } else {
                warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : Red ; -fx-text-font-family : sans-serif;-fx-font-size:30px;-fx-font-weight:bold;");
                warningButton.setText("Sorting Resumed");
                pauseButton.setText("Resume");
                pauseButton.setText("Pause");
                isPaused = false;
            }
        });
        pauseButton.setStyle(style);
        pauseButton.setFont(font);
        pauseButton.setPrefSize(250,60);

        pane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                pauseButton.fire();
            }
        });

        resetButton = new Button("Reset");
        resetButton.setOnAction(e-> {
            if(!isReset){
                warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : Red ; -fx-text-font-family : sans-serif;-fx-font-size:30px;-fx-font-weight:bold;");
                warningButton.setText("Cannot Reset");
            }else{
                resetScreen();
                indexBox.getChildren().clear();
                drawIndex();
                isReset = false;
            }
        });
        resetButton.setStyle(style);
        resetButton.setFont(font);
        resetButton.setPrefSize(250, 60);

            //Start,Puase,Rest
        button = new HBox();
        button.setAlignment(Pos.BOTTOM_CENTER);
        button.getChildren().addAll(startButton,pauseButton,resetButton);
        button.setSpacing(40);
            //InputTextFields,Submit
        arrayInputBox = new HBox();
        submitButton = new Button("Submit");
        submitButton.setOnAction(e->{
            if(!isSubmitted){
                isSubmitted = true;
                getArrayFill();
                rectangleBox.getChildren().clear();
                intmin.getChildren().clear();
                inttemp.getChildren().clear();
                drawRectangle();
            }
        });
        submitButton.setStyle(style);
        submitButton.setFont(font);
        submitButton.setPrefSize(250,60);
        
        inputBox = new HBox(arrayInputBox,submitButton);
        inputBox.setSpacing(50);
        inputBox.setAlignment(Pos.CENTER);

        Label sliderLabel = new  Label("2Seconds");
        sliderLabel.setStyle("-fx-font-size: 30px;-fx-text-fill: #00286c; -fx-font-family: sans-serif; -fx-font-weight : bold");
        sliderLabel.setAlignment(Pos.CENTER);

        Slider slider = new Slider();
        slider.setOrientation(Orientation.VERTICAL);
        slider.setStyle(style);
        slider.setMaxHeight(180);
        slider.setMinWidth(40);
        slider.setMin(1);
        slider.setMax(5);
        slider.setValue(2);
        slider.setMajorTickUnit(1); 
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);

        slider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                speed = (int) slider.getValue();
                double tempSpeed = slider.getValue();
                sliderLabel.setText(Integer.toString(speed)+"Seconds");
                String speedText = "Normal";
                if(tempSpeed > 3){
                    speedText = "Slow";
                     warningButton.setStyle("-fx-background-color: transparent;-fx-font-weight: bold;-fx-text-fill : Red ; -fx-text-font-family : sans-serif;-fx-font-size:30px;-fx-font-weight:bold;");
                }else if(tempSpeed>2.0 && tempSpeed<3.0){
                    speedText = "Medium";
                    warningButton.setStyle("-fx-background-color: transparent;-fx-font-weight: bold;-fx-text-fill : Orange ; -fx-text-font-family : sans-serif;-fx-font-size:30px;-fx-font-weight:bold;");

                }else{
                    speedText = "Fast";
                    warningButton.setStyle("-fx-background-color: transparent;-fx-font-weight: bold;-fx-text-fill : Green ; -fx-text-font-family : sans-serif;-fx-font-size:30px;-fx-font-weight:bold;");

                }
                warningButton.setText("Sorting Speed: " + speedText);
            }
            
        });

        HBox sliderHbox = new HBox(slider,sliderLabel);
        sliderHbox.setSpacing(25);
        sliderHbox.setAlignment(Pos.CENTER);

        
        buttonsBox = new VBox();
        buttonsBox.getChildren().addAll(inputBox,button);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setSpacing(20);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setStyle("-fx-background-color: #6A5ACD;");
        buttonsBox.setPrefHeight(200);

        bottomBox = new HBox(buttonsBox,sliderHbox);
        bottomBox.setSpacing(50);
        bottomBox.setPrefHeight(200);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setStyle("-fx-background-color: #6A5ACD;");

        pane.setCenter(screenNode);
        pane.setBottom(bottomBox);
    }
    public void drawInputBox(){
        //double width = 100;
        //double spacing = 5;
        for(int i=0;i<Array_Size;i++){
            inputFields = new TextField();
            inputFields.setStyle("-fx-background-Color : White ;-fx-border-Color :#ff6393");
            inputFields.setPrefHeight(50);
            inputFields.setPrefWidth(100);
            inputFields.setFont(Font.font(30));
            arrayInputBox.getChildren().add(inputFields);
            if(i<Array_Size-1){
                arrayInputBox.setSpacing(5);
            }
        }
    }

    public void drawIndex() {
        double width = (canvas.getWidth() / Array_Size);
        double height = (double) (70);
        double y = canvas.getHeight() / 2;
    
        for (int i = 0; i < Array_Size; i++) {
            double x = i * width;
            Rectangle rectangle1 = new Rectangle(x, y, width, height);
            rectangle1.setFill(Color.TRANSPARENT);
            //rectangle1.setStroke(Color.BLACK);
    
            Text numberText2 = new Text(String.valueOf(i));
            numberText2.setFill(Color.web("#00286c"));
            numberText2.setFont(Font.font("Arial", FontWeight.BOLD, 30));
            
            // Calculate the center position for the text inside the rectangle
            double textX = x + (width - numberText2.getBoundsInLocal().getWidth()) / 2;
            double textY = y + height / 2 + numberText2.getBoundsInLocal().getHeight() / 4;
            numberText2.setX(textX);
            numberText2.setY(textY);
    
            StackPane sp5 = new StackPane();
            sp5.getChildren().addAll(rectangle1, numberText2);
            indexBox.getChildren().add(sp5);
        }
    }
    
    public void elementInput(TextField inputTextField){
        try{
            int no = Integer.parseInt(inputTextField.getText().trim());
            if(no<0 ){
                warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : Red ; -fx-text-font-family : sans-serif;-fx-font-size:30px;-fx-font-weight:bold;");
                warningButton.setText("Invalid Input: Array size must be a positive integer");
            }else{
                warningButton.setText("");
                Array_Size = no;
                changeScreen();
                drawInputBox();
            }
        }catch(NumberFormatException nfe){
            warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : Red ; -fx-text-font-family : sans-serif;-fx-font-size:30px;-fx-font-weight:bold;");
            warningButton.setText("Invalid Input: Array size must be a positive integer");
        } 
    }
    public void drawRectangle() {
        double width = (canvas.getWidth() / Array_Size);
        double height = (double) (100);
        double y = canvas.getHeight() / 2;
        
        elements = new Element[Array_Size];
        for (int i = 0; i < Array_Size; i++) {
            double x = i * width;
            rectangle = new Rectangle(x, y, width, height);
            if(i<OuterIterator){
                rectangle.setFill(Color.web("#6B728E"));
            }else{
                rectangle.setFill(Color.web("#0084d8"));
            }
            rectangle.setStroke(Color.BLACK);
    
            numberText = new Text(String.valueOf(values[i]));
            numberText.setFill(Color.WHITE);
            numberText.setFont(Font.font("Arial", FontWeight.BOLD, 50));
            double textX = i * width + width / 2 - numberText.getBoundsInLocal().getWidth() / 2;
            double textY = y - 10;
            numberText.setX(textX);
            numberText.setY(textY);
    
            sp = new StackPane();
            sp.getChildren().addAll(rectangle, numberText);
            rectangleBox.getChildren().add(sp);
            elements[i] = new Element(rectangle, values[i]);
        }
        // Int Min and Int Temp Update 
        numberText = new Text(String.valueOf(values[minIndex]));
        numberText.setFill(Color.WHITE);
        numberText.setFont(Font.font("Arial", FontWeight.BOLD, 50));

        sp1 = new StackPane(minNo,numberText);
        intmin.getChildren().add(sp1);
    
        numberText = new Text(String.valueOf(temp));
        numberText.setFill(Color.WHITE);
        numberText.setFont(Font.font("Arial", FontWeight.BOLD, 50));

        sp2 = new StackPane(tempNo,numberText);
        inttemp.getChildren().add(sp2);

        // Update Iterations .
        numberText = new Text(String.valueOf(OuterIterator));
        numberText.setFill(Color.web("#00286c"));
        numberText.setFont(Font.font("Arial", FontWeight.BOLD, 30));
    }

    private void getArrayFill(){
        inputFlag = true;
        for (int i = 0; i < Array_Size; i++) {
            TextField textField = (TextField) arrayInputBox.getChildren().get(i);
            
                try {
                    int no = Integer.parseInt(textField.getText().trim());
                    values[i] = no;
                } catch (NumberFormatException e) {
                    values[i] = 0;
            }
        }
        
    }
    private void resetScreen() {
        for (int i = 0; i < Array_Size; i++) {
            values[i] = 0;
        }
        sortingThread.interrupt();
        inputFlag = false;
        isSubmitted = false;
        rectangleBox.getChildren().clear();
        intmin.getChildren().clear();
        inttemp.getChildren().clear();
        OuterIterator = 0;
        minIndex = 0;
        temp = 0;
        
        drawIndex();
        drawRectangle();
        inputTextField.setText("");
        arrayInputBox.getChildren().clear();
        drawInputBox();
        warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : white ; -fx-text-font-family : sans-serif;-fx-font-size:15px; -fx-font-weight: bold;");
        warningButton.setText("");
    }
    private void doSelectionSort() {
        sortingThread= new Thread(() -> {
            isSorting = true;
            for (int i = 0; i < Array_Size - 1; i++) {

                minIndex = i;
                temp = values[i];
                OuterIterator = i+1;

                Platform.runLater(() -> iterationLabel.setText("Iteration Count : " + OuterIterator));
                
                sleepTime = 1000 * speed;
                
                while(isPaused){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
                for (int j = i + 1; j < Array_Size; j++) {
                    if (values[j] < values[minIndex]) {
                        minIndex = j;
                    }
                    sleepTime = 1000 * speed;
                    final int final2 = i;
                    final int jIndex = j;
                    final int grayC = minIndex;
                    Platform.runLater(() -> {
                        rectangleBox.getChildren().clear();
                        drawRectangle();
                        elements[final2].rectangle.setFill(Color.web("#ffc45b"));
                        //elements[grayC].rectangle.setFill(Color.GRAY);
                        elements[jIndex].rectangle.setFill(Color.web("#d153b8"));
                    });

                    try {
                            Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                            e.printStackTrace();
                    }
                    while(isPaused){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try{
                        if (Thread.currentThread().isInterrupted()) {
                            Platform.runLater(() -> {
                                drawRectangle();
                                warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : Red ; -fx-font-weight: bold;-fx-text-font-family : sans-serif;-fx-font-size:30px;-fx-font-weight:;");
                                warningButton.setText("Sorting Interrupted");
                            });
                            return;
                        }
                    }catch(Exception e){
                                
                    }finally{
                        isSorting = false;
                    }
                }
                OuterIterator++;
                values[i] = values[minIndex];
                values[minIndex] = temp;
    
                double tempX = elements[minIndex].rectangle.getX();
                elements[minIndex].rectangle.setX(elements[i].rectangle.getX());
                elements[i].rectangle.setX(tempX);
    
                PauseTransition pause = new PauseTransition(Duration.millis(5));
                final int finalI = i;
                final int finalMinIndex = minIndex;
                Platform.runLater(() -> {
                    rectangleBox.getChildren().clear();
                    drawRectangle();
                    elements[finalI+1].rectangle.setFill(Color.web("#ffc45b"));
                    pause.setOnFinished(event -> {
                        elements[finalMinIndex].rectangle.setFill(Color.web("#0084d8"));
                    });
                    pause.play();
                });
    
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(() -> {
            //    drawRectangle();
                rectangleBox.getChildren().clear();
                drawRectangle();
                warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : Green ; -fx-text-font-family : sans-serif;-fx-font-size:30px;-fx-font-weight:bold;");
                warningButton.setText("Sorting Completed!");
            });
            isSorting = false;
            isReset = true;
        });
        sortingThread.start();
    }    
    private static class Element {
        Rectangle rectangle;
        int value;

        public Element(Rectangle rectangle, int value) {
            this.rectangle = rectangle;
            this.value = value;
        }
    }
}