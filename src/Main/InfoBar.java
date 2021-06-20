package Main;

import java.io.IOException;
import java.io.InputStream;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author Preston Tang
 */
public class InfoBar extends Group {

    private Text tFuel;
    private Text tSpeed;
    private Text tStatus;
    private Text tDistance;
    private Text tScore;

    public InfoBar(double x) {
        tFuel = new Text();
        tSpeed = new Text();
        tStatus = new Text();
        tDistance = new Text();
        tScore = new Text();

        InputStream fontStream = getClass().getResourceAsStream("/Fonts/8bitoperator.ttf");
        Font f;
        if (fontStream != null) {
            f = Font.loadFont(fontStream, 11);
            tFuel.setFont(f);
            tSpeed.setFont(f);
            tStatus.setFont(f);
            tDistance.setFont(f);
            tScore.setFont(f);
            try {
                fontStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            tFuel.setStyle("-fx-font: 17 impact;");
            tSpeed.setStyle("-fx-font: 17 impact;");
            tStatus.setStyle("-fx-font: 17 impact;");
            tDistance.setStyle("-fx-font: 17 impact;");
            tScore.setStyle("-fx-font: 17 impact;");
        }

        tFuel.setX(x - 43);
        tFuel.setY(20);
        tFuel.setFill(Color.WHITE);
        tFuel.setText("FUEL: ");

        tSpeed.setX(x - 43);
        tSpeed.setY(40);
        tSpeed.setFill(Color.WHITE);
        tSpeed.setText("SPEED: ");

        tDistance.setX(x - 43);
        tDistance.setY(60);
        tDistance.setFill(Color.WHITE);
        tDistance.setText("DISTANCE: ");

        tStatus.setX(x - 43);
        tStatus.setY(80);
        tStatus.setFill(Color.WHITE);
        tStatus.setText("STATUS: ");

        tScore.setX(x - 43);
        tScore.setY(100);
        tScore.setFill(Color.WHITE);
        tScore.setText("SCORE: (0-100)");

        super.getChildren().addAll(tFuel, tSpeed, tDistance, tStatus, tScore);
    }

    public Text getFuelUI() {
        return this.tFuel;
    }

    public Text getSpeedUI() {
        return this.tSpeed;
    }

    public Text getDistanceUI() {
        return this.tDistance;
    }

    public Text getStatusUI() {
        return this.tStatus;
    }

    public Text getScoreUI() {
        return this.tScore;
    }

}
