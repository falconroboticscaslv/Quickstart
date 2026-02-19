package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class OuttakeSystem {

    Limelight limelight = new Limelight();
    private DcMotorEx outtake;
    private Servo outtakeServo;
    private int outtakeMax = 1;
    private boolean outtakeOn = false;

    private boolean angleToggle = false;

    public void init(HardwareMap hwMap) {
        outtake = hwMap.get(DcMotorEx.class, "outtake");
        outtakeServo = hwMap.get(Servo.class, "outtakeServo");

        outtake.setDirection(DcMotorSimple.Direction.REVERSE);
        outtake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        outtakeServo.setPosition(0.86);
    }

    public void toggle() {
        outtakeOn = !outtakeOn;
    }

    public void toggleAngle(){
        angleToggle = !angleToggle;
    }

    public double getCurrentVelocity() {
        return outtake.getVelocity();
    }

    public void update() {
        double rpm = 1100 + (limelight.getDistance() * 20);
        rpm = Math.max(1100, Math.min(2800, rpm));
        double ticksPerSecond = rpm * 28.0 / 60.0;

        if (outtakeOn) {
            outtake.setVelocity(ticksPerSecond);

        } else {
            outtake.setPower(0);
        }

        if (angleToggle) {
            outtakeServo.setPosition(1);
        } else {
            outtakeServo.setPosition(0.86);
        }
    }
}
