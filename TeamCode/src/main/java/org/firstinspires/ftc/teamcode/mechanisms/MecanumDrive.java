package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MecanumDrive {
    private DcMotor frontRight, frontLeft, backRight, backLeft;

    public void init(HardwareMap hwMap) {
        frontLeft = hwMap.get(DcMotor.class,"frontLeft");
        frontRight = hwMap.get(DcMotor.class, "frontRight");
        backLeft = hwMap.get(DcMotor.class, "backLeft");
        backRight = hwMap.get(DcMotor.class, "backRight");

        // Set motor directions
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void drive(double forward, double strafe, double rotate) {
        double flp = forward + strafe + rotate;
        double frp = forward - strafe - rotate;
        double blp = forward - strafe + rotate;
        double brp = forward + strafe - rotate;

        double maxPower = 1.0;
        double maxSpeed = 1.0;

        maxPower = Math.max(maxPower, Math.abs(flp));
        maxPower = Math.max(maxPower, Math.abs(frp));
        maxPower = Math.max(maxPower, Math.abs(blp));
        maxPower = Math.max(maxPower, Math.abs(brp));

        frontLeft.setPower(maxSpeed * (flp/maxPower));
        frontRight.setPower(maxSpeed * (frp/maxPower));
        backLeft.setPower(maxSpeed * (blp/maxPower));
        backRight.setPower(maxSpeed * (brp/maxPower));
    }
}
