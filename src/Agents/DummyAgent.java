package Agents;

import Main.Enterprise;

/**
 *
 * @author Preston Tang
 */
//This Agent Blasts for x Second, Rests for x Second.
public class DummyAgent {

    private final Enterprise prise;

    private boolean canRun;

    public DummyAgent(Enterprise prise) {
        this.prise = prise;
        canRun = true;
    }

    public void action(boolean blasting, double time) {
        canRun = false;
        Thread t = new Thread(() -> {
            try {
                Thread.sleep((long) time);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            if (!prise.isSuccess() && !prise.isDestroyed() && !(prise.getFuel() <= 0)) {
                prise.setBlasting(blasting);
            }
            canRun = true;
        });
        t.start();
    }

    public boolean isRunnable() {
        return this.canRun;
    }

}
