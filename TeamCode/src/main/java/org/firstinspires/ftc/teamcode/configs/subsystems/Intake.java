package org.firstinspires.ftc.teamcode.configs.subsystems;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ClassUtil;

import org.firstinspires.ftc.teamcode.configs.utils.RobotConstants;

import java.util.function.BooleanSupplier;

import kotlinx.coroutines.channels.ChannelResult;

public class Intake {

    private DcMotor intake;
    private Servo gate;
    private CRServo leftFeeder, rightFeeder;
    private int state = 0;
    // Constructor for the hardware
    public Intake(HardwareMap hwMap)
    {
        intake = hwMap.get(DcMotor.class, "intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftFeeder = hwMap.get(CRServo.class, "left_feeder");
        rightFeeder = hwMap.get(CRServo.class, "right_feeder");
        leftFeeder.setDirection(DcMotorSimple.Direction.REVERSE);
        gate = hwMap.get(Servo.class, "gate");
    }
    public void enable()
    {
        intake.setPower(RobotConstants.IntakeConstants.FORWARD_POWER);
        gate.setPosition(RobotConstants.IntakeConstants.CLOSE_POS);
        leftFeeder.setPower(RobotConstants.ShooterConstants.FULL_SPEED);
        rightFeeder.setPower(-RobotConstants.ShooterConstants.FULL_SPEED);
        state = 1;
    }
    public void feed()
    {
        intake.setPower(-0.5);
        state =1;
    }
    public void disable()
    {
        intake.setPower(0);
        leftFeeder.setPower(0);
        rightFeeder.setPower(0);
        state = 0;
    }
    public void reverse()
    {
        intake.setPower(RobotConstants.IntakeConstants.REVERSE_POWER);
        gate.setPosition(RobotConstants.IntakeConstants.OPEN_POS);
        leftFeeder.setPower(RobotConstants.ShooterConstants.FULL_SPEED);
        rightFeeder.setPower(-RobotConstants.ShooterConstants.FULL_SPEED);
        state = -1;
    }
    public void openGate()
    {
        gate.setPosition(RobotConstants.IntakeConstants.OPEN_POS);
    }
    public void closeGate()
    {
        gate.setPosition(RobotConstants.IntakeConstants.CLOSE_POS);
    }
    public int getState() { return this.state;}


    /* ======================= COMMANDS =======================  */


    public Command intakeCommand(BooleanSupplier isHeld)
    {
        return Command.build()
                .setStart(this::enable)
                .setDone(() -> !isHeld.getAsBoolean())
                .setEnd(endCondition -> disable())
                .requiring(this)
                .setPriority(1);
    }

    public Command outtakeCommand(BooleanSupplier isHeld)
    {
        return Command.build()
                .setStart(this::reverse)
                .setDone(() -> !isHeld.getAsBoolean())
                .setEnd(endCondition -> disable())
                .requiring(this)
                .setPriority(0);
    }

}
