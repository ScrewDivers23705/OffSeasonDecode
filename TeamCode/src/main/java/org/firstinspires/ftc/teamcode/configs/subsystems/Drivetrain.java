package org.firstinspires.ftc.teamcode.configs.subsystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.configs.pedroPathing.Constants;

public class Drivetrain {
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
}