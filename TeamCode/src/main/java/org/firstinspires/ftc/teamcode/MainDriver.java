//Main Driver code for FTC Team 19810

// Import packages
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;

@TeleOp(name = "MainDriver")
public class MainDriver extends LinearOpMode {
    MecanumDrive drive = new MecanumDrive();

    double forward, strafe, rotate;
    private DcMotor intake;
    private DcMotor outtake;
    private Limelight3A limelight;
    private Servo servo;
    private Servo servo2;
    private Servo outtakeServo;

    @Override
    public void runOpMode() {

        // Get hardware
        drive.init(hardwareMap);

        servo = hardwareMap.get(Servo.class, "servo");
        servo2 = hardwareMap.get(Servo.class, "servo2");
        outtakeServo = hardwareMap.get(Servo.class,"outtakeServo");

        DcMotorEx intake = hardwareMap.get(DcMotorEx.class, "intake");
        DcMotorEx outtake = hardwareMap.get(DcMotorEx.class, "outtake");
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(10);
        limelight.start();
        limelight.pipelineSwitch(0); //apriltags 20, 24 (goal tags)

        // Main Loop
        waitForStart();
        if (opModeIsActive()) {
            double speed = 1;
            double turn = 0.9;

            double tx = 0;
            double ta = 0;

            boolean rotateOn = false;

            double distance = 0;

            servo.setPosition(0);
            servo2.setPosition(0.365);
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
                //test
                // Outtake Toggle
                if (gamepad1.yWasPressed()) {
                    outtakeOn = !outtakeOn;
                }

                //Outtake counter
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

                //Outtake velocity changing
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

                //Intake toggle
                if (gamepad1.dpadRightWasPressed()) {
                    if (intakeMax < 2) {
                        intakeMax = intakeMax+1;
                    } else {
                        intakeMax = 0;
                    }
                }

                //Intake counter
                if (gamepad1.dpadLeftWasPressed()) {
                    if (intakeMax > 0) {
                        intakeMax = outtakeMax-1;
                    } else {
                        intakeMax = 2;
                    }
                }
                if (gamepad1.aWasPressed()){
                    intakeOn = !intakeOn;
                }

                //Intake velocity changing
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

                if (llResult != null && llResult.isValid()) {
                    tx = llResult.getTx();
                    ta = llResult.getTa();
                    distance = 0.4528733+(72.94012-0.4528733)/(1+Math.pow((ta/3.113176),1.254381));

                    telemetry.addData("Target X", tx);
                    telemetry.addData("Target Area", ta);
                    telemetry.addData("Distance", distance);
                }
                telemetry.addData("Auto-Rotate", rotateOn);
                telemetry.addData("Outtake velocity counter", outtakeMax);
                telemetry.addData("Intake velocity counter", intakeMax);
                telemetry.update();

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
            }
        }
    }
}