package application;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	private double speed = 50.0;	// Define a initial velocity
	double xOffset = 0;
	double yOffset = 0;
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Racing Car");
		
		Pane root = new Pane();
		
		// Draw the road border
		Rectangle roadBorder = new Rectangle(20, 100, 750, 70);
		roadBorder.setFill(null);
		roadBorder.setStroke(Color.BLACK);
		
		// Draw the car using a trapezoid, a rectangle and two circles
		Polygon carRoof = new Polygon(30, 130, 40, 105, 80, 105, 90, 130);
		carRoof.setFill(Color.RED);
		
		Rectangle carBody = new Rectangle(20, 130, 80, 30);
		carBody.setFill(Color.GREEN);
		
		Circle wheel1 = new Circle(45, 165, 5);
		Circle wheel2 = new Circle(75, 165, 5);
		wheel1.setFill(Color.BLACK);
		wheel2.setFill(Color.BLACK);
		// Create a text prompt
		Text text = new Text("Please enter the car's speed: ");
		text.setStyle("-fx-font-size: 20; -fx-fill: blue; -fx-font-family: Arial");
		text.setLayoutX(10);
		text.setLayoutY(18);
		// Create input field to control the speed
		TextField speedInput = new TextField();
		speedInput.setPromptText("Enter the car's speed(0 - 100): ");
		speedInput.setLayoutX(10);
		speedInput.setLayoutY(30);
		// Create a button to trigger the speed control
		Button control = new Button("Submit");
		control.setLayoutX(20);
		control.setLayoutY(60);
		// Create a label to display the current speed of the car
		Label currentSpeed = new Label();
		currentSpeed.setText("The car's current speed is: " + speed);
		currentSpeed.setLayoutX(100);
		currentSpeed.setLayoutY(70);
		currentSpeed.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		// Create a label to display the error message if the speed you input is wrong
		Label wrongMsg = new Label();
		wrongMsg.setLayoutX(200);
		wrongMsg.setLayoutY(30);
		wrongMsg.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		// Integrate the various parts of the car into a whole
		Pane car = new Pane();
		car.getChildren().addAll(carRoof, carBody, wheel1, wheel2);
		
		root.getChildren().addAll(roadBorder, car, speedInput, control, text, currentSpeed, wrongMsg);
		
		// Complete the animation effect of the car moving left and right
		TranslateTransition translate = new TranslateTransition(Duration.seconds(50 / speed), car);
		car.setTranslateX(-20);
		translate.setToX(700);
		translate.setCycleCount(TranslateTransition.INDEFINITE);
		translate.play(); 		
		
		// Event handler "submit" button
		control.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					speed = Double.parseDouble(speedInput.getText());
					currentSpeed.setText("The car's current speed is: " + speed);
					wrongMsg.setText("");
					if(translate.getStatus() == javafx.animation.Animation.Status.RUNNING) {
						translate.stop();
					} 
					translate.setDuration(Duration.seconds(50 / speed));
					car.setTranslateX(-20);
					translate.setToX(700);
					translate.play();
				} catch(NumberFormatException e) {
					wrongMsg.setText("Please enter valid numbers!");
				}
			}
		});
		// When the mouse clicks on the car, the car stops moving. Click the car again and the car continues to move.
		car.setOnMouseClicked(event -> {
			if(translate.getStatus() == javafx.animation.Animation.Status.RUNNING) {
				translate.pause();
			} else {
				translate.play();
			}
		});
		// The mouse can drag the car to move left and right
		car.setOnMousePressed(event -> {
			xOffset = event.getSceneX() - carBody.getLayoutX();
			yOffset = event.getSceneY() - carBody.getLayoutY();
		});
		
		car.setOnMouseDragged(event -> {
			car.setLayoutX(event.getSceneX() - xOffset);
		});
		
		// Add a "pause" button
		Button pause = new Button("Pause");
		pause.setLayoutX(390);
		pause.setLayoutY(200);
		root.getChildren().add(pause);
		pause.setOnMouseClicked(event -> {
			if(pause.getText() == "Pause") {
				pause.setText("Continue");
				translate.pause();
			}
			else if(pause.getText() == "Continue") {
				pause.setText("Pause");
				translate.play();
			}
		});
		
		Scene scene = new Scene(root, 780, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}