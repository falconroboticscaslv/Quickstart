package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;

@TeleOp
public class limelightTest extends OpMode {
    MecanumDrive drive = new MecanumDrive();
    double forward, strafe, rotate;
    private Limelight3A limelight;

    //Set Variables
    double speed = 1;
    double turn = 0.9;

    double tx = 0;
    double ta = 0;
    double distance = 0;
    boolean tFound = false;

    @Override
    public void init() {
        drive.init(hardwareMap);
        limelight = hardwareMap.get(Limelight3A.class,"limelight");
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
        distance = 0.4528733+(72.94012-0.4528733)/(1+Math.pow((ta/3.113176),1.254381));
        if (llResult != null && llResult.isValid()) {
            tFound = true;
            telemetry.addData("Target X", tx);
            telemetry.addData("Target Area", ta);
            telemetry.addData("Distance", distance);
        } else {
            tFound = false;
        }
        telemetry.addData("Target found", tFound);
        telemetry.update();
        //-------------------Movement---------------------------
        forward = -gamepad1.left_stick_y * speed;
        strafe = gamepad1.left_stick_x * speed;
        rotate = gamepad1.right_stick_x * turn + (tx*0.022);
    }
}