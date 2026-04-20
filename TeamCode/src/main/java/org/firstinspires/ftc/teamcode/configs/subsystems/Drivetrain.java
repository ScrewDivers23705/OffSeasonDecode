package org.firstinspires.ftc.teamcode.configs.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.behaviors.BlockedBehavior;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
import com.pedropathing.ivy.behaviors.InterruptedBehavior;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.configs.pedroPathing.Constants;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class Drivetrain{
    public final Follower follower;
    public Drivetrain(HardwareMap hwMap)
    {
        follower = Constants.createFollower(hwMap);
        follower.startTeleopDrive();
    }

    public void drive (double forward, double strafe, double turn, boolean fieldCentric)
    {
        follower.setTeleOpDrive(forward,strafe,turn,!fieldCentric);
    }
    public void update()
    {
        follower.update();
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
                .setInterruptedBehavior(InterruptedBehavior.END)
                .setConflictBehavior(ConflictBehavior.OVERRIDE)
                .setBlockedBehavior(BlockedBehavior.CANCEL);
    }
}