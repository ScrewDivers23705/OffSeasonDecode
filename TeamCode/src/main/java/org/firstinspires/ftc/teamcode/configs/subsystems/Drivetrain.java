package org.firstinspires.ftc.teamcode.configs.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.behaviors.BlockedBehavior;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
import com.pedropathing.ivy.behaviors.InterruptedBehavior;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.configs.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.configs.utils.Alliance;
import org.firstinspires.ftc.teamcode.configs.utils.RobotConstants;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class Drivetrain{
    public final Follower follower;
    private DcMotorEx frontLeft, frontRight, backLeft, backRight;
    private Alliance alliance;
    public Drivetrain(HardwareMap hwMap, Alliance alliance)
    {
        frontLeft = hwMap.get(DcMotorEx.class, "left_front");
        frontRight = hwMap.get(DcMotorEx.class, "right_front");
        backLeft = hwMap.get(DcMotorEx.class, "left_back");
        backRight = hwMap.get(DcMotorEx.class, "right_back");

        follower = Constants.createFollower(hwMap);
        follower.startTeleopDrive();
        follower.setStartingPose(RobotConstants.DrivetrainConstants.autonEndPose);
        this.alliance = alliance;

    }

    public void drive(double forward, double strafe, double turn, boolean fieldCentric)
    {
        //double headingRadians = follower.getHeading();
        //if (alliance == Alliance.BLUE)
        //   headingRadians += Math.PI;

        if (fieldCentric)
        {
            follower.setTeleOpDrive(forward, strafe, turn,false, alliance == Alliance.BLUE ? Math.toRadians(180) : 0);
            /*
            double x = strafe * Math.cos(headingRadians) + forward * Math.sin(headingRadians);
            double y = strafe * -Math.sin(headingRadians) + forward * Math.cos(headingRadians);

            y *= 1.1;

            double denominator = Math.max(Math.abs(x) + Math.abs(y) + Math.abs(turn), 1);

            frontLeft.setPower((y + x + turn) / denominator);
            frontRight.setPower((y - x - turn)/ denominator);
            backLeft.setPower((y - x + turn) / denominator);
            backRight.setPower((y + x - turn) / denominator);
            */
        }
        else
            follower.setTeleOpDrive(forward,strafe,turn,true);
    }
    public void periodic()
    {
        follower.update();
    }

    public void setAlliance(Alliance a) {this.alliance = a;}
    /* ======================= COMMANDS =======================  */

    public Command driveCommand(DoubleSupplier forward, DoubleSupplier strafe, DoubleSupplier turn, BooleanSupplier field)
    {
        return Command.build()
                .setExecute(() -> {
                    drive(forward.getAsDouble(), strafe.getAsDouble(), turn.getAsDouble(), field.getAsBoolean()); // drive
                })
                .setDone(() -> false)
                .setEnd(endCondition -> drive(0,0,0,false))
                .requiring(this)
                .setPriority(0)
                .setInterruptedBehavior(InterruptedBehavior.SUSPEND)
                .setConflictBehavior(ConflictBehavior.OVERRIDE)
                .setBlockedBehavior(BlockedBehavior.CANCEL);
    }
}