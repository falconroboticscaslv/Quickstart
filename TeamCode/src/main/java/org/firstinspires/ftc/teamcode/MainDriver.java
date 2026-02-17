//Main Driver code for FTC Team 19810

// Import packages
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "MainDriver")
public class MainDriver extends LinearOpMode {

    private DcMotor backRight;
    private DcMotor backLeft;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor intake;
    private DcMotor outtake;
    private Limelight3A limelight;
    private Servo servo;
    private Servo servo2;
    private Servo servo3;
    private Servo outtakeServo;



    @Override
    public void runOpMode() {

        // Get hardware
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");

        servo = hardwareMap.get(Servo.class, "servo");
        servo2 = hardwareMap.get(Servo.class, "servo2");
        servo3 = hardwareMap.get(Servo.class, "servo3");
        outtakeServo = hardwareMap.get(Servo.class,"outtakeServo");

        DcMotorEx intake = hardwareMap.get(DcMotorEx.class, "intake");
        DcMotorEx outtake = hardwareMap.get(DcMotorEx.class, "outtake");
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(10);
        limelight.start();
        limelight.pipelineSwitch(0); //apriltags 20, 24 (goal tags)

        // Set motor directions
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        // Main Loop
        waitForStart();
        if (opModeIsActive()) {

            //Set Variables
            double flp = 0;
            double frp = 0;
            double blp = 0;
            double brp = 0;

            double speed = 1;
            double turn = 0.9;

            double tx = 0;

            double forward, strafe, rotate;

            int pipeline = 0;

            boolean rotateOn = false;

            //double distance;

            servo.setPosition(0);
            servo2.setPosition(0.365);
            servo3.setPosition(0.5);
            outtakeServo.setPosition(0.86);


            boolean intakeOn = false;
            boolean outtakeOn = false;
            boolean servoOn = false;
            int outtakeMax = 1;
            int intakeMax = 1;
            double tolerance = -0.25;

            while (opModeIsActive()) {

                // intake and outtakes
                outtake.setDirection(DcMotorSimple.Direction.REVERSE);
                outtake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                intake.setDirection(DcMotorSimple.Direction.FORWARD);
                intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                intake.setPower((gamepad1.right_trigger * -1));
                outtake.setPower((gamepad1.left_trigger * -1));

                //Shooting distance adjustment
                if (gamepad1.bWasPressed()) {
                    servoOn = !servoOn;
                }

                if (servoOn) {
                    outtakeServo.setPosition(1);
                } else {
                    outtakeServo.setPosition(0.86);
                }

                if (gamepad1.right_bumper) {
                    intake.setPower(1);
                }

                if (gamepad1.left_bumper) {
                    outtake.setPower(1);
                }

                // Servos
                if (gamepad1.x) {
                    servo.setPosition(0.33);
                    servo2.setPosition(0.075);
                } else {
                    servo.setPosition(0);
                    servo2.setPosition(0.365);
                }

                // Outtake Toggle
                if (gamepad1.yWasPressed()) {
                    outtakeOn = !outtakeOn;
                }

                if (gamepad1.dpadUpWasPressed()) {
                    if (outtakeMax < 2) {
                        outtakeMax = outtakeMax+1;
                    } else {
                        outtakeMax = 0;
                    }
                }

                if (gamepad1.dpadDownWasPressed()) {
                    if (outtakeMax > 0) {
                        outtakeMax = outtakeMax-1;
                    } else {
                        outtakeMax = 2;
                    }
                }

                if (gamepad1.dpadRightWasPressed()) {
                    if (intakeMax < 2) {
                        intakeMax = intakeMax+1;
                    } else {
                        intakeMax = 0;
                    }
                }

                if (gamepad1.dpadLeftWasPressed()) {
                    if (intakeMax > 0) {
                        intakeMax = outtakeMax-1;
                    } else {
                        intakeMax = 2;
                    }
                }

                if (outtakeOn) {
                    if (outtakeMax < 1) {
                        outtake.setVelocity(1500);
                    } else if (outtakeMax <2) {
                        outtake.setVelocity(1800);
                    } else {
                        outtake.setVelocity(3000);
                    }
                }
                else {
                    outtake.setPower(0);
                }

                // Intake Toggle
                if (gamepad1.aWasPressed()){
                    intakeOn = !intakeOn;
                }

                if (intakeOn) {
                    if (intakeMax < 1) {
                        intake.setVelocity(700);
                    } else if (intakeMax <2) {
                        intake.setVelocity(1000);
                    } else {
                        intake.setVelocity(1500);
                    }
                }
                else {
                    intake.setPower(0);
                }

                //-----------------Limelight stuff-------------
                LLResult llResult = limelight.getLatestResult();
                tx = llResult.getTx();
                if (llResult != null && llResult.isValid()) {
                    telemetry.addData("Target X", tx);
                    telemetry.addData("Target Area", llResult.getTx());
                }
                telemetry.addData("Auto-Rotate", rotateOn);
                telemetry.addData("Pipeline", pipeline);
                telemetry.addData("Outtake velocity counter", outtakeMax);
                telemetry.addData("Intake velocity counter", intakeMax);
                telemetry.update();

                if (rotateOn) {
                    if (pipeline < 1) {
                        servo3.setPosition(0.5);
                    } else {
                        servo3.setPosition(0.365);
                    }
                } else {
                    servo3.setPosition(0.4);
                }
                //---------------Pipeline switching--------------
                if (gamepad2.rightBumperWasPressed()) {
                    if (pipeline < 1) {
                        pipeline = 1;
                        servo3.setPosition(0.365);
                    } else {
                        pipeline = 0;
                        servo3.setPosition(0.5);
                    }
                }
                limelight.pipelineSwitch(pipeline);

                //------------------Auto-Align----------------------

                if(gamepad2.leftBumperWasPressed()) {
                    rotateOn = !rotateOn;
                }

                if (!rotateOn) {
                    tx = 0;
                } else {
                    tx = tx - tolerance;
                }

                //-------------------Movement-----------------
                forward = -gamepad2.left_stick_y * speed;
                strafe = gamepad2.left_stick_x * speed;
                rotate = gamepad2.right_stick_x * turn + (tx*0.022);

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
                frontLeft.setPower(flp);
                frontRight.setPower(frp);
                backLeft.setPower(blp);
                backRight.setPower(brp);

                //Reset powers
                flp = 0;
                frp = 0;
                blp = 0;
                blp = 0;
            }
        }
    }
}