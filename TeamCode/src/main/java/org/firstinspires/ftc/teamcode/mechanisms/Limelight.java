package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Limelight {

    private Limelight3A limelight;

    private double tx = 0;
    private double ta = 0;
    private double distance = 0;

    private boolean rotateOn = false;

    private final double tolerance = -0.25;   // offset
    private final double kP = 0.022;          // rotate multiplier

    public void init(HardwareMap hwMap) {
        limelight = hwMap.get(Limelight3A.class, "limelight");

        limelight.setPollRateHz(10);
        limelight.start();
        limelight.pipelineSwitch(0); // goal tags, 20 + 24
    }

    public void update() {

        LLResult result = limelight.getLatestResult();

        if (result != null && result.isValid()) {
            tx = result.getTx();
            ta = result.getTa();

            // Your distance formula
            distance = 0.4528733 +
                    (72.94012 - 0.4528733) /
                            (1 + Math.pow((ta / 3.113176), 1.254381));

        } else {
            tx = 0;
        }
    }

    // Toggle auto-rotate
    public void toggleAutoRotate() {
        rotateOn = !rotateOn;
    }

    // Returns rotation correction value
    public double getRotateCorrection() {

        if (!rotateOn) return 0;

        return (tx - tolerance) * kP;
    }

    public boolean isAutoRotateOn() {
        return rotateOn;
    }

    public double getTx() {
        return tx;
    }

    public double getTa() {
        return ta;
    }

    public double getDistance() {
        return distance;
    }
}
