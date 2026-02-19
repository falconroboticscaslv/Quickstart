package org.firstinspires.ftc.teamcode.autonomous; // make sure this aligns with class location

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous (name = "Blue Audience 1")
public class AutoBA1 extends OpMode {

    private Follower follower;
    private Timer pathTimer, opmodeTimer, waitTimer;

    private int pathState;
    private final Pose startPose = new Pose(56.2201834, 8, Math.toRadians(90)); // Start Pose of our robot.
    private final Pose scorePose = new Pose(55.7798165, 14.6422018, Math.toRadians(112));
    private final Pose align1Pose = new Pose(35.8073394, 35.7155963, Math.toRadians(180));
    private final Pose pickup1Pose = new Pose(12.5688073, 35.0655045, Math.toRadians(0));
    private PathChain move1, alignPickup1, pickup1, returnStartPos;

    public void buildPaths() {
        move1 = follower.pathBuilder()
                .addPath(new BezierLine(startPose, scorePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();

        alignPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, align1Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), align1Pose.getHeading())
                .build();

        pickup1 = follower.pathBuilder()
                .addPath(new BezierLine(align1Pose, pickup1Pose))
                .setLinearHeadingInterpolation(align1Pose.getHeading(), pickup1Pose.getHeading())
                .build();

        returnStartPos = follower.pathBuilder()
                .addPath(new BezierLine(pickup1Pose, startPose))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), startPose.getHeading())
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(move1);
                setPathState(1);
                waitTimer.resetTimer();
                break;
            case 1:

                if(!follower.isBusy() && waitTimer.getElapsedTimeSeconds()>2) {

                    follower.followPath(alignPickup1,true);
                    setPathState(2);
                }
                break;
            case 2:
                if(!follower.isBusy()) {
                    follower.followPath(pickup1, 0.7, true);
                    setPathState(3);
                }
                break;
            case 3:
                if(!follower.isBusy()) {
                    follower.followPath(returnStartPos,true);
                    setPathState(4);
                }
                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void loop() {

        // These loop the movements of the robot, these must be called continuously in order to work
        follower.update();
        autonomousPathUpdate();

        // Feedback to Driver Hub for debugging
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }
    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        waitTimer = new Timer();
        opmodeTimer.resetTimer();


        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);

    }

    @Override
    public void init_loop() {}

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void stop() {
    }}