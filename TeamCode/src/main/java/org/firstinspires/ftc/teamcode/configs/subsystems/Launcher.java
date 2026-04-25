package org.firstinspires.ftc.teamcode.configs.subsystems;


import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.configs.utils.LookUpTable;
import org.firstinspires.ftc.teamcode.configs.utils.RobotConstants;
public class Launcher {

    private DcMotorEx launcher; // flywheel motor
    private CRServo leftFeeder; // lefet crservo feeder
    private CRServo rightFeeder; // right crservo feeder

    public LookUpTable lookUpTable; // lookuptable for vel/angle from dist
    RobotConstants.Shooter.LaunchState launchState;
    ElapsedTime feederTimer = new ElapsedTime();

    public Launcher(HardwareMap hwMap)
    {
        launcher = hwMap.get(DcMotorEx.class, "launcher");
        leftFeeder = hwMap.get(CRServo.class, "left_feeder");
        rightFeeder = hwMap.get(CRServo.class, "right_feeder");

        lookUpTable = new LookUpTable(2);

        rightFeeder.setDirection(CRServo.Direction.REVERSE);

        launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        launchState = RobotConstants.Shooter.LaunchState.IDLE;
    }

}
