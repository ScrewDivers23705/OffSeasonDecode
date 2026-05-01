package org.firstinspires.ftc.teamcode.configs.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.behaviors.BlockedBehavior;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
import com.pedropathing.ivy.behaviors.InterruptedBehavior;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.configs.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.configs.utils.Alliance;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class Drivetrain{
    public final Follower follower;
    private Alliance alliance;
    public Drivetrain(HardwareMap hwMap, Alliance alliance)
    {
        follower = Constants.createFollower(hwMap);
        follower.startTeleopDrive();

        this.alliance = alliance;
    }

    public void drive (double forward, double strafe, double turn, boolean fieldCentric)
    {
        if (fieldCentric)
        {
            double offset = getAngularOffset(); // get offset

            double fieldForward = forward * Math.cos(offset) - strafe * Math.sin(offset);
            double fieldStrafe = forward * Math.sin(offset) + strafe * Math.cos(offset);

            follower.setTeleOpDrive(fieldForward, fieldStrafe, turn, false);
        }
        else
            follower.setTeleOpDrive(forward,strafe,turn,true);
    }
    public void update()
    {
        follower.update();
    }

    private double getAngularOffset(){
        return (alliance == Alliance.BLUE) ? 0.0 : Math.PI;
    }

    /* ======================= COMMANDS =======================  */

    public Command driveCommand(DoubleSupplier forward, DoubleSupplier strafe, DoubleSupplier turn, BooleanSupplier field)
    {
        return Command.build()
                .setExecute(() -> {
                    drive(forward.getAsDouble(), strafe.getAsDouble(), turn.getAsDouble(), field.getAsBoolean()); // drive
                    update(); // update pedro
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