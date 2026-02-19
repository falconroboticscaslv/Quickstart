package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IntakeSystem {
    private DcMotorEx intake;
    private int intakeMax = 1;
    private boolean intakeOn = false;

    public void init(HardwareMap hwMap) {
        intake = hwMap.get(DcMotorEx.class, "intake");

        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void toggle(){
        intakeOn = !intakeOn;
    }

    public void velocityUp(){
        intakeMax = (intakeMax + 1) % 3;
    }

    public void velocityDown() {
        intakeMax = (intakeMax - 1 + 3) % 3;
    }

    public int getTargetVelocity() {
        if (intakeMax == 0) return 1500;
        if (intakeMax == 1) return 1800;
        return 3000;
    }

    public double getCurrentVelocity() {
        return intake.getVelocity();
    }

    public void update() {
        if (intakeOn) {
            if (intakeMax == 0) {
                intake.setVelocity(700);
            } else if (intakeMax == 1) {
                intake.setVelocity(1000);
            } else {
                intake.setVelocity(1500);
            }
        }
    }
}
