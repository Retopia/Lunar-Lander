package Main;

/**
 *
 * @author Preston Tang
 */
public class Time {

    //x miliseconds
    private static long necessaryTime = 50_000000;
    private long lastTime;

    //Returns change in time as double
    private long getDeltaTime() {
        return (System.nanoTime() - lastTime);
    }

    //Updates lastTime
    private void updateTime() {
        this.lastTime = System.nanoTime();
    }

    //Public constructor for Time object
    public Time() {
        this.lastTime = System.nanoTime();
    }

    //Returns true if a second has passed and updates time,
    //otherwise returns false and does nothing.
    public boolean hasNecessaryTimePassed() {
        if (getDeltaTime() >= necessaryTime) {
            updateTime();
            return true;
        } else {
            return false;
        }
    }
}
