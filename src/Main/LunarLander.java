package Main;

import Agents.GeneticAgent;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Preston Tang
 */
public class LunarLander extends Application {

    public static boolean paused = false;

    private final double GROUNDHEIGHT = 560;
    private final double LANDINGSPEED = 1.5;

    private int generation = 0;

    private final int childrenNum = 6;

    private final int geneNumber = 256;

    private final double mutationRate = 0.07;
    private final double gravity = 9.8 / 1.4;

    private long lastTime = System.nanoTime();
    private boolean isResetting = false;

    @Override
    public void start(Stage stage) {
        Pane base = new Pane();
        base.setCache(true);
        base.setCacheHint(CacheHint.SPEED);

        ImageView background = new ImageView();
        background.setImage(new Image(getClass().getResourceAsStream("/Sprites/space.png")));
        background.setFitWidth(1200);
        background.setFitHeight(560);
        background.setX(0);
        background.setY(0);
        background.setCache(true);
        background.setCacheHint(CacheHint.SPEED);

        //Old background color was #1c1b1b
        Scene scene = new Scene(base, 1185, 600);

        stage.setTitle("LunarLander");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        Rectangle[] borders = new Rectangle[7];
        for (int i = 0; i < borders.length; i++) {
            borders[i] = new Rectangle();
            borders[i].setWidth(5);
            borders[i].setHeight(560);
            borders[i].setX(150 + (i * 150));
            borders[i].setY(0);
            borders[i].setFill(Color.WHITE);
            borders[i].setCache(true);
            borders[i].setCacheHint(CacheHint.SPEED);
        }
        ImageView ground = new ImageView();
        ground.setImage(new Image(getClass().getResourceAsStream("/Sprites/surface-green.png")));
        ground.setFitWidth(1200);
        ground.setFitHeight(55);
        ground.setX(0);
        ground.setY(GROUNDHEIGHT);
        ground.setCache(true);
        ground.setCacheHint(CacheHint.SPEED);

        //Width, Height, x, y
        Enterprise[] ships = new Enterprise[8];
        for (int i = 0; i < ships.length; i++) {
            ships[i] = new Enterprise(base, 50, 75, 50 + (i * 150) + 3, 75);
            ships[i].setGravity(gravity);
            ships[i].setBlasting(false);
            ships[i].setMaxSpeed(2);
            ships[i].setTerminalVelocity(3);
            ships[i].setSpeed(3);
            ships[i].setCache(true);
            ships[i].setCacheHint(CacheHint.SPEED);
        }

        Text generationDisplay = new Text("Generation: " + generation);
        InputStream fontStream = getClass().getResourceAsStream("/Fonts/8bitoperator.ttf");
        Font f;
        if (fontStream != null) {
            f = Font.loadFont(fontStream, 13);
            generationDisplay.setFont(f);
            try {
                fontStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        generationDisplay.setFill(Color.WHITE);
        generationDisplay.setX(3);
        generationDisplay.setY(580);

        GeneticAgent[] gas = new GeneticAgent[8];

        for (int i = 0; i < gas.length; i++) {
            gas[i] = new GeneticAgent(ships[i], geneNumber, mutationRate);
            System.out.println(Arrays.toString(gas[i].getGenes()));
        }

        System.out.println("\n\n");

        base.getChildren().addAll(background, ground, generationDisplay);
        base.getChildren().addAll(ships);

        ground.toBack();
        background.toBack();
        generationDisplay.toFront();

        base.getChildren().addAll(borders);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE && !ships[0].isDestroyed()
                    && ships[0].getFuel() > 0 && !ships[0].isSuccess()) {
                ships[0].setBlasting(true);
            }
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.SPACE && !ships[0].isDestroyed()
                    && ships[0].getFuel() > 0 && !ships[0].isSuccess()) {
                ships[0].setBlasting(false);
            }
        });

        Time t = new Time();

        //Game Loop
        AnimationTimer animator = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isResetting) {
                    return;  // Skip physics updates during reset
                }
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                for (int i = 0; i < ships.length; i++) {
                    if (!ships[i].finished) {
                        if (!(ships[i].getFuel() <= 0.0)) {
                            //Crash Handling
                            if (ships[i].getY() + ships[i].getFitHeight() - 8 > GROUNDHEIGHT) {
                                if (!ships[i].finished) {
                                    if (ships[i].getSpeed() > LANDINGSPEED) {
                                        ships[i].setLandingSpeed(ships[i].getSpeed());
                                        ships[i].setDestroyed();
                                        ships[i].finished = true;
                                        ships[i].getInfoBar().getStatusUI().setFill(Color.RED);
                                    } else {
                                        ships[i].setLandingSpeed(ships[i].getSpeed());
                                        ships[i].setSuccess();
                                        ships[i].finished = true;
                                        ships[i].getInfoBar().getStatusUI().setFill(Color.LIME);
                                    }
                                }
                            }

                            //Gravity accounted for frame loss
                            ships[i].setSpeed(ships[i].getSpeed() + (ships[i].getGravity() * deltaTime));
                            if (ships[i].getSpeed() > ships[i].getTerminalVelocity()) {
                                ships[i].setSpeed(ships[i].getTerminalVelocity());
                            }
                            ships[i].setY(ships[i].getY() + (ships[i].getSpeed() * deltaTime * 60));
                        } else {
                            ships[i].setBlasting(false);

                            if (!ships[i].getStatus().equals("FREE FALL")) {
                                //Custom Status
                                ships[i].setStatus("FREE FALL");
                                ships[i].getInfoBar().getStatusUI().setFill(Color.ORANGE);
                            }

                            //Crash Handling
                            if (ships[i].getY() + ships[i].getFitHeight() - 8 > GROUNDHEIGHT) {
                                if (!ships[i].finished) {
                                    if (ships[i].getSpeed() > LANDINGSPEED) {
                                        ships[i].setLandingSpeed(ships[i].getSpeed());
                                        ships[i].setDestroyed();
                                        ships[i].finished = true;
                                        ships[i].getInfoBar().getStatusUI().setFill(Color.RED);
                                    } else {
                                        ships[i].setLandingSpeed(ships[i].getSpeed());
                                        ships[i].setSuccess();
                                        ships[i].finished = true;
                                        ships[i].getInfoBar().getStatusUI().setFill(Color.LIME);
                                    }
                                }
                            }

                            //Gravity accounted for frame loss
                            ships[i].setSpeed(ships[i].getSpeed() + (ships[i].getGravity() * deltaTime));
                            if (ships[i].getSpeed() > ships[i].getTerminalVelocity()) {
                                ships[i].setSpeed(ships[i].getTerminalVelocity());
                            }
                            ships[i].setY(ships[i].getY() + (ships[i].getSpeed() * deltaTime * 60));
                        }

                        //Activate Agent Stuff
                        if (t.hasNecessaryTimePassed()) {
                            for (int r = 0; r < gas.length; r++) {
                                gas[r].action();
                            }
                        }

                        //Text Changes
                        ships[i].getInfoBar().getFuelUI().setText("FUEL: " + (int) ships[i].getFuel());
                        ships[i].getInfoBar().getSpeedUI().setText("SPEED: " + Math.abs(Math.round(ships[i].getSpeed() * 100.0) / 100.0));
                        ships[i].getInfoBar().getDistanceUI().setText("DISTANCE: " + ships[i].getDistanceFromGround());
                        ships[i].getInfoBar().getStatusUI().setText("STATUS: " + ships[i].getStatus());

                        //Movement Handling
                        if (ships[i].isBlasting()) {
                            if (ships[i].getSpeed() < -ships[i].getMaxSpeed()) {
                                ships[i].setSpeed(-ships[i].getMaxSpeed());
                            } else {
                                ships[i].setSpeed(ships[i].getSpeed() - (12.0 * deltaTime)); // 0.2 * 60 = 12 units per second
                                ships[i].setFuel(ships[i].getFuel() - (12.0 * deltaTime));
                            }
                        }
                    }
                }

                int dead = 0;
                for (int i = 0; i < gas.length; i++) {
                    if (gas[i].getShip().finished) {
                        gas[i].determineScore();
                        gas[i].getShip().getInfoBar().getScoreUI().setText("SCORE: " + new DecimalFormat(".#").format(gas[i].getScore()));
                        gas[i].getShip().getInfoBar().getSpeedUI().setText("SPEED: " + gas[i].getShip().getLandingSpeed());
                        dead++;
                    }
                }

                //All dead
                if (dead == 8) {
                    dead = 0;

                    //For gene tracing
                    for (int i = 0; i < gas.length; i++) {
                        System.out.println(Arrays.toString(gas[i].getGenes()));
                    }

                    System.out.println("\n\n");

                    //To get the best score
                    //Sorted in descending order
                    Arrays.sort(gas, Collections.reverseOrder());

                    gas[0].getShip().getInfoBar().getScoreUI().setFill(Color.DEEPSKYBLUE);
                    gas[1].getShip().getInfoBar().getScoreUI().setFill(Color.DEEPSKYBLUE);

                    Platform.runLater(() -> {
                        isResetting = true;  // Pause physics updates

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }

                        base.getChildren().clear();

                        for (int i = 0; i < ships.length; i++) {
                            ships[i] = new Enterprise(base, 50, 75, 50 + (i * 150) + 3, 75);
                            ships[i].setGravity(gravity);
                            ships[i].setBlasting(false);
                            ships[i].setMaxSpeed(2);
                            ships[i].setTerminalVelocity(3);
                            ships[i].setSpeed(3);
                            ships[i].setCache(true);
                            ships[i].setCacheHint(CacheHint.SPEED);
                        }

                        int[][] genes = gas.clone()[0].crossover(gas.clone()[1], childrenNum);

                        for (int i = 0; i < gas.length; i++) {
                            gas[i] = new GeneticAgent(ships[i], geneNumber, mutationRate);
                            gas[i].setGenes(genes[i]);
                        }

                        generation++;

                        //Adding back UI
                        generationDisplay.setText("Generation: " + generation);

                        base.getChildren().addAll(background, ground, generationDisplay);
                        base.getChildren().addAll(ships);
                        ground.toBack();
                        background.toBack();
                        generationDisplay.toFront();
                        base.getChildren().addAll(borders);

                        lastTime = System.nanoTime();
                        isResetting = false;
                    });
                }
            }
        };

        animator.start();
    }

    public static void main(String[] args) {
        try {
            System.setProperty("javafx.animation.pulse", "60");
        } catch (AccessControlException e) {

        }
        launch(args);
    }
}
