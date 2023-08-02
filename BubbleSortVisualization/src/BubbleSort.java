import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BubbleSort extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    private Canvas canvas;
    private BorderPane pane;
    private StackPane sp;
    private HBox pane2;
    private HBox inputFieldBox;
    private GraphicsContext g;
    private int Array_Size = 100;
    private TextField inputField;
    private boolean inputFlag;
    private Button addRectangle;
    private Button startButton;
    private Button shuffleButton;
    private Button warningButton;
    private Button submitButton;
    private Button pauseButton;
    private volatile boolean isSortingPaused = false;
    int[] values = new int[Array_Size];
    Font font = Font.font("Courier New", FontWeight.BOLD, 12);
    String style = "-fx-background-color: #CC0033;-fx-text-fill : white ; -fx-text-font-family : sans-serif;";
    private Element[] elements;

    public void start(Stage stage) throws Exception {
        inputFlag = false;
        pane = new BorderPane();
        pane2 = new HBox();
        canvas = new Canvas(950, 600);
        g = canvas.getGraphicsContext2D();
    
        //Bubble Sort Image
        Pane canvasPane = new HBox();
        Image img = new Image("bubblesort.png");
        canvasPane.getChildren().add(new ImageView(img));
        canvasPane.setPadding(new Insets(25,250,25,300));

        //String styles = "-fx-background-color: transparent;"+"-fx-border-color: transparent;" ;
        
        warningButton = new Button();
        Font font2 = Font.font("Verdana",FontWeight.BOLD,30);
        warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : Red ; -fx-text-font-family : sans-serif;-fx-text-size:30px");
        //warningButton.setStyle(styles);
        warningButton.setAlignment(Pos.BOTTOM_CENTER);
        warningButton.setFont(font2);
        warningButton.prefHeight(30);

        VBox midNode = new VBox();
        midNode.getChildren().addAll(canvasPane,warningButton);
        midNode.setSpacing(50);
        midNode.setAlignment(Pos.CENTER);
        midNode.setStyle("-fx-background-color: lightblue;");
        g.setFill(Color.WHITESMOKE);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //  TOP AREA OF BORDER-PANE
        Text name = new Text();
        name.setText("Bubble Sort Algorithm");
        name.setFill(Color.WHITE);
        name.setFont(Font.font("DejaVu Serif", FontWeight.BOLD, 70));


        HBox topNode = new HBox(name);
        topNode.setPadding(new Insets(20,40,20, 0));
        topNode.setAlignment(Pos.CENTER);
        topNode.setStyle("-fx-background-color:#49C5B6;");

        //Bottom AREA OF BORDER-PANE
        inputField = new TextField();
        //inputField.setFont(Font.font(20));
        inputField.setStyle("-fx-font-weight: bold;-fx-font-size: 20px;-fx-background-color:white;-fx-border-color:lightblue;-fx-border-width: 2px;");
        inputField.setScaleX(1);
        inputField.setScaleY(1);
        addRectangle = new Button("Add Elements");
        addRectangle.setFont(font);
        addRectangle.setStyle(style);
        addRectangle.setScaleX(4);
        addRectangle.setScaleY(2.5);

        startButton = new Button("Start");
        startButton.setOnAction(e -> {
            if(inputFlag == true){
                warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : Green ; -fx-text-font-family : sans-serif;-fx-text-size:30px");
                warningButton.setText("Sorting Started!");
                doBubbleSort();
            }else{
                warningButton.setAlignment(Pos.BOTTOM_CENTER);
                warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : Red ; -fx-text-font-family : sans-serif;-fx-text-size:30px");
                warningButton.setText("Please Enter Array Elements");
            }});
        startButton.setFont(font);
        startButton.setStyle(style);
        startButton.setScaleX(4);
        startButton.setScaleY(2.5);

        HBox bottomNode = new HBox(200);
        bottomNode.getChildren().addAll(inputField, addRectangle);
        bottomNode.setAlignment(Pos.CENTER);
        bottomNode.setPadding(new Insets(50, 100, 50, 0));
        bottomNode.setStyle("-fx-background-color: #49C5B6;");

        addRectangle.setOnAction(even -> {
            changeSize();
           
        });

        pane.setTop(topNode);
        pane.setCenter(midNode);
        pane.setBottom(bottomNode);
        Scene scene = new Scene(pane, 1500, 1000, false, null);
        stage.setScene(scene);
        stage.show();
    }

    public void changeButtons() {
        pane2.setAlignment(Pos.CENTER);
        pane2.setStyle("-fx-background-color: lightblue;");
        pane2.setPadding(new Insets(0,0,250,0));
        VBox screen = new VBox(pane2, warningButton);
        screen.setAlignment(Pos.BOTTOM_CENTER);
        warningButton.setAlignment(Pos.BASELINE_CENTER);
        warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : Red ; -fx-text-font-family : sans-serif;-fx-text-size:30px");
        screen.setStyle("-fx-background-color: lightblue;");
        //screen.setAlignment(Pos.CENTER);
        drawSorted();

        shuffleButton = new Button("Shuffle");
        shuffleButton.setFont(font);
        shuffleButton.setStyle(style);
        // shuffleButton.setOnAction( e -> shuffle() );
        shuffleButton.setScaleX(3.5);
        shuffleButton.setScaleY(2.5);

        pauseButton = new Button();
        pauseButton.setText("Pause");
        pauseButton.setFont(font);
        pauseButton.setStyle(style);
        pauseButton.setOnAction(e ->pauseSorting());
        pauseButton.setScaleX(3.5);
        pauseButton.setScaleY(2.5);

        HBox buttons = new HBox(200);
        buttons.getChildren().addAll(startButton, shuffleButton, pauseButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(30, 30, 50, 50));

        inputFieldBox = new HBox();
        drawInputBox();
        HBox textBox = new HBox(inputFieldBox);
        textBox.setSpacing(10);
        textBox.setAlignment(Pos.CENTER);

        submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            warningButton.setText("");
            getInput();});
        submitButton.setScaleX(3.5);
        // submitButton.setPrefWidth(10);
        submitButton.setScaleY(2.5);
        submitButton.setPadding(new Insets(5, 5, 5, 5));

        HBox inputBox = new HBox(textBox, submitButton);
        inputBox.setSpacing(100);
        inputBox.setPadding(new Insets(20, 0, 0, 0));
        inputBox.setAlignment(Pos.CENTER);

        VBox allButtons = new VBox();
        allButtons.setStyle("-fx-background-color: #49C5B6;");
        allButtons.getChildren().addAll(inputBox, buttons);
        allButtons.setPrefHeight(200);
        allButtons.setPrefWidth(1300);
        allButtons.setSpacing(30);

        pane.setBottom(allButtons);
        pane.setCenter(screen);
    }

    public void changeSize() {
        String inputText = inputField.getText().trim();
        try {
            int newSize = Integer.parseInt(inputText);
            if (newSize <= 0 || newSize>100) {
                warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : Red ; -fx-text-font-family : sans-serif;-fx-text-size:30px");
                warningButton.setText("Invalid Input: Array size must be a positive integer");
            } else {
                Array_Size = newSize;
                warningButton.setText("");
                changeButtons();
            }
        } catch (NumberFormatException ne) {
            warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : Red ; -fx-text-font-family : sans-serif;-fx-text-size:30px");
            warningButton.setText("Invalid Input: Please enter a valid integer for the array size.");
        }
    }

    public void drawInputBox() {
        double width = 100;
        double spacing = 5;

        for (int i = 0; i < Array_Size; i++) {
            TextField textField = new TextField();
            textField.setPrefWidth(width);
            textField.setPrefHeight(50);
            textField.setFont(Font.font(30));
            inputFieldBox.getChildren().add(textField);

            if (i < Array_Size - 1) {
                inputFieldBox.setSpacing(spacing);
            }
        }
    }

    public void getInput() {
        inputFlag = true;
        values = new int[Array_Size];
        for (int i = 0; i < Array_Size; i++) {
            try {
                TextField textField = (TextField) inputFieldBox.getChildren().get(i);
                String inputString = textField.getText();
                int value = Integer.parseInt(inputString.trim());
                values[i] = value;
            } catch (NumberFormatException e) {
                values[i] = 0;
            }
        }
        pane2.getChildren().clear();
        drawSorted();
    }

    public void drawSorted() {
        double width = (double) (canvas.getWidth() / Array_Size);
        int height = (int) (100);
        int y = 500;

        elements = new Element[Array_Size];

        for (int i = 0; i < Array_Size; i++) {
            Rectangle rectangle = new Rectangle(i * width, y, width, height);
            rectangle.setFill(Color.BLUE);
            rectangle.setStroke(Color.BLACK);

            Text numberText = new Text(String.valueOf(values[i]));
            numberText.setFill(Color.WHITE);
            numberText.setFont(Font.font("Arial", FontWeight.BOLD, 50));

            double textX = i * width + width / 2 - numberText.getBoundsInLocal().getWidth() / 2;
            double textY = y - 10;

            numberText.setX(textX);
            numberText.setY(textY);

            sp = new StackPane();
            sp.getChildren().addAll(rectangle, numberText);
            pane2.getChildren().add(sp);
            elements[i] = new Element(rectangle, values[i]);
        }
            
    }

    public void doBubbleSort() {
        int n = values.length;

        Thread sortThread = new Thread(() -> {
            
                for (int i = 0; i < n - 1; i++) {
                    for (int j = 0; j < n - i - 1; j++) {
                        //warningButton.setText("");
                        if (values[j] > values[j + 1]) {
                            int temp = values[j];
                            values[j] = values[j + 1];
                            values[j + 1] = temp;

                            if (isSortingPaused) {
                                while (isSortingPaused) {
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            double tempX = elements[j].rectangle.getX();
                            elements[j].rectangle.setX(elements[j + 1].rectangle.getX());
                            elements[j + 1].rectangle.setX(tempX);

                            PauseTransition pause = new PauseTransition(Duration.millis(2000));
                            Platform.runLater(() -> {
                                pane2.getChildren().clear();
                                drawSorted();
                            });
                            pause.play();

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : Green ; -fx-text-font-family : sans-serif;-fx-text-size:30px");
            Platform.runLater(() -> warningButton.setText("Bubble Sort completed successfully!"));
        });

        sortThread.start();
    }

    public void pauseSorting(){

        if(!isSortingPaused){
            warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : Red ; -fx-text-font-family : sans-serif;-fx-text-size:30px");
            warningButton.setText("Sorting Paused!");
            pauseButton.setText("Resume");
            isSortingPaused = true;
        }else{
            warningButton.setStyle("-fx-background-color: transparent;-fx-text-fill : Red ; -fx-text-font-family : sans-serif;-fx-text-size:30px");
            warningButton.setText("Sorting Resumed!");
            pauseButton.setText("Pause");
            isSortingPaused = false;
        }
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
