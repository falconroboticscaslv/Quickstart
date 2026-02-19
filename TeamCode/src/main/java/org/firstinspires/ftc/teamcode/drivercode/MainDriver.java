package org.firstinspires.ftc.teamcode.drivercode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.IntakeSystem;
import org.firstinspires.ftc.teamcode.mechanisms.Limelight;
import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.OuttakeSystem;

@TeleOp(name = "MainDriver")
public class MainDriver extends LinearOpMode {
    MecanumDrive drive = new MecanumDrive();
    IntakeSystem intake = new IntakeSystem();
    OuttakeSystem outtake = new OuttakeSystem();
    Limelight limelight = new Limelight();

    double forward, strafe, rotate;
    private Servo servo;

    @Override
    public void runOpMode() {

        // Get hardware
        drive.init(hardwareMap);
        intake.init(hardwareMap);
        outtake.init(hardwareMap);
        limelight.init(hardwareMap);

        servo = hardwareMap.get(Servo.class, "servo");

        // Main Loop
        waitForStart();
        if (opModeIsActive()) {
            double speed = 1;
            double turn = 0.9;

            servo.setPosition(0);
            double tolerance = -0.25;

            while (opModeIsActive()) {

                //Shooting distance adjustment
                if (gamepad1.bWasPressed()) {
                    outtake.toggleAngle();
                }

                // Servos
                if (gamepad1.x) {
                    servo.setPosition(0.33);
                } else {
                    servo.setPosition(0);
                }

                // Outtake Toggle
                if (gamepad1.yWasPressed()) {
                    outtake.toggle();
                }

                //Intake toggle
                if (gamepad1.dpadRightWasPressed()) {
                    intake.velocityUp();
                }

                //Intake counter
                if (gamepad1.dpadLeftWasPressed()) {
                    intake.velocityDown();
                }
                if (gamepad1.aWasPressed()){
                    intake.toggle();
                }

                intake.update();
                outtake.update();
                limelight.update();

                //-----------------Limelight stuff-------------
                telemetry.addData("Target X", limelight.getTx());
                telemetry.addData("Target Area", limelight.getTa());
                telemetry.addData("Distance", limelight.getDistance());
                telemetry.addData("Auto-Rotate", limelight.isAutoRotateOn());
                telemetry.addData("Outtake velocity", outtake.getCurrentVelocity());
                telemetry.addData("Target intake vel.", intake.getTargetVelocity());
                telemetry.addData("Intake vel.", intake.getCurrentVelocity());
                telemetry.update();
                //-------------------Movement-----------------
                forward = -gamepad2.left_stick_y * speed;
                strafe = gamepad2.left_stick_x * speed;
                rotate = gamepad2.right_stick_x * turn + limelight.getRotateCorrection();
            }
        }
    }
}