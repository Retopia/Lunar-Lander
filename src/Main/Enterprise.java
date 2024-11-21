package Main;

import java.text.DecimalFormat;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

/**
 *
 * @author Preston Tang
 */
public class Enterprise extends ImageView {

    private final InfoBar bar;

    private Image image;

    private double fuel;

    private String status;

    private boolean blasting, destroyed, success;

    public boolean finished;

    private double ySpeed;
    private double gravity;
    private double terminalVelocity;
    private double maxUpSpeed;

    private double landingSpeed;

    public Enterprise(Pane p, double width, double height, double x, double y) {
        image = makeTransparent(new Image(getClass().getResourceAsStream("/Sprites/enterprise-neutral.png")));

        bar = new InfoBar(x);
        p.getChildren().add(bar);
        bar.toFront();

        this.destroyed = false;

        this.fuel = 100;

        this.status = "IDLE";

        this.setImage(image);
        this.setPreserveRatio(true);

        //To Improve Performance
        this.setSmooth(false);
        this.setCache(true);

        this.setFitWidth(width);
        this.setFitHeight(height);
        this.setX(x);
        this.setY(y);
    }

    public double getSpeed() {
        return this.ySpeed;
    }

    public void setSpeed(double ySpeed) {
        this.ySpeed = ySpeed;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public double getGravity() {
        return gravity;
    }

    public void setTerminalVelocity(double terminalVelocity) {
        this.terminalVelocity = terminalVelocity;
    }

    public double getTerminalVelocity() {
        return terminalVelocity;
    }

    public void setMaxSpeed(double maxUpSpeed) {
        this.maxUpSpeed = maxUpSpeed;
    }

    public double getMaxSpeed() {
        return maxUpSpeed;
    }

    public void setBlasting(boolean blasting) {
        this.blasting = blasting;
        if (blasting) {
            this.status = "POWERED";
            image = makeTransparent(new Image(getClass().getResourceAsStream("/Sprites/enterprise-powered.png")));
        } else {
            this.status = "IDLE";
            image = makeTransparent(new Image(getClass().getResourceAsStream("/Sprites/enterprise-neutral.png")));
        }
        this.setImage(image);
    }

    public boolean isBlasting() {
        return blasting;
    }

    public InfoBar getInfoBar() {
        return this.bar;
    }

    public void setDestroyed() {
        this.destroyed = true;
        this.status = "DESTROYED";
        bar.getStatusUI().setText("STATUS: DESTROYED");
        image = makeTransparent(new Image(getClass().getResourceAsStream("/Sprites/enterprise-broken.png")));
        this.setImage(image);
        this.ySpeed = 0;
        this.gravity = 0;
        this.blasting = false;
    }

    public boolean isDestroyed() {
        return this.destroyed;
    }

    public void setSuccess() {
        this.success = true;
        this.status = "SUCCESS";
        image = makeTransparent(new Image(getClass().getResourceAsStream("/Sprites/enterprise-neutral.png")));
        this.setImage(image);
        this.ySpeed = 0;
        this.gravity = 0;
        this.blasting = false;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        bar.getStatusUI().setText("STATUS: " + status);
        this.status = status;
    }

    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    public double getFuel() {
        if (fuel < 0.0) {
            fuel = 0.0;
        }
        return fuel;
    }

    public double getDistanceFromGround() {
        //Weird bug
        if ((int) (495.0 - this.getY()) == 1.0) {
            return 0.0;
        }

        return (int) (495.0 - this.getY());
    }

    public void setLandingSpeed(double landingSpeed) {
        this.landingSpeed = Double.valueOf(new DecimalFormat("#.#").format(landingSpeed));
    }

    public double getLandingSpeed() {
        return this.landingSpeed;
    }

    //https://stackoverflow.com/questions/35905882/how-to-delete-a-color-of-an-image-with-javafx-make-the-background-transparent
    private Image makeTransparent(Image inputImage) {
        int W = (int) inputImage.getWidth();
        int H = (int) inputImage.getHeight();
        WritableImage outputImage = new WritableImage(W, H);
        PixelReader reader = inputImage.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                int argb = reader.getArgb(x, y);

                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                if (r >= 0xFF
                        && g >= 0xFF
                        && b >= 0xFF) {
                    argb &= 0x00FFFFFF;
                }

                writer.setArgb(x, y, argb);
            }
        }

        return outputImage;
    }

}
