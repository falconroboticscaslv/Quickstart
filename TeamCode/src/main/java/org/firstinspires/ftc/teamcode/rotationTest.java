package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp

public class rotationTest extends OpMode {
    private Limelight3A limelight;
    private DcMotor backRight;
    private DcMotor backLeft;
    private DcMotor frontLeft;
    private DcMotor frontRight;

    //Set Variables
    double flp = 0;
    double frp = 0;
    double blp = 0;
    double brp = 0;

    double speed = 1;
    double turn = 0.9;

    double tx = 0;
    double ta = 0;
    double forward, strafe, rotate;

    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class,"limelight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        // Set motor directions
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void start() {
        limelight.setPollRateHz(10);

        limelight.start();
    }

    @Override
    public void loop() {
        LLResult llResult = limelight.getLatestResult();
        tx = llResult.getTx();
        ta = llResult.getTa();
        if (llResult != null && llResult.isValid()) {
            telemetry.addData("Target X", tx);
            telemetry.addData("Target Area", ta);
        }
        telemetry.update();
        //-------------------Movement---------------------------

        forward = -gamepad1.left_stick_y * speed;
        strafe = gamepad1.left_stick_x * speed;
        rotate = gamepad1.right_stick_x * turn + (tx*0.015);

        double denominator = Math.max(Math.abs(forward) + Math.abs(strafe) + Math.abs(rotate), 1);

        flp /= denominator;
        frp /= denominator;
        blp /= denominator;
        brp /= denominator;

        flp = forward + strafe + rotate;
        frp = forward - strafe - rotate;
        blp = forward - strafe + rotate;
        brp = forward + strafe - rotate;

        // Set motor powers
        frontLeft.setPower(-flp);
        frontRight.setPower(-frp);
        backLeft.setPower(-blp);
        backRight.setPower(-brp);

        //Reset powers
        flp = 0;
        frp = 0;
        blp = 0;
        brp = 0;
    }
}