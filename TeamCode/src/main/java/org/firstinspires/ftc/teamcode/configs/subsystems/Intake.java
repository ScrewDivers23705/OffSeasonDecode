package org.firstinspires.ftc.teamcode.configs.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.lazy;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
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
    private int state = 0;
    // Constructor for the hardware
    public Intake(HardwareMap hwMap)
    {
        intake = hwMap.get(DcMotor.class, "intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        gate = hwMap.get(Servo.class, "gate");
    }
    public void enable(Launcher launcher)
    {
        intake.setPower(RobotConstants.IntakeConstants.FORWARD_POWER);
        gate.setPosition(RobotConstants.IntakeConstants.CLOSE_POS);
        launcher.reverseFeeders();
        state = 1;
    }
    public void feed()
    {
        intake.setPower(-0.9);
        state =1;
    }
    public void disable()
    {
        intake.setPower(0);
        state = 0;
    }
    public void reverse()
    {
        intake.setPower(RobotConstants.IntakeConstants.REVERSE_POWER);
        gate.setPosition(RobotConstants.IntakeConstants.OPEN_POS);
        state = -1;
    }
    public void setPower(double power)
    {
        state = 1;
        if(power > 0)
            state = -1;
        intake.setPower(power);
    }
    public void reverseMotor()
    {
        intake.setPower(RobotConstants.IntakeConstants.REVERSE_POWER);
        gate.setPosition(RobotConstants.IntakeConstants.OPEN_POS);
        state = -1;
    }
    public void forwardMotor()
    {
        intake.setPower(RobotConstants.IntakeConstants.FORWARD_POWER);
        state = 1;
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


    public Command intakeCommand(BooleanSupplier isHeld, Launcher launcher) {
        return Command.build()
                .setStart(() -> enable(launcher))
                .setDone(() -> !isHeld.getAsBoolean())
                .setEnd(endCondition -> {
                    this.disable();

                })
                .requiring(this)
                .requiring(launcher) // <--- THIS IS HOW YOU ADD MULTIPLE REQUIREMENTS
                .setPriority(1);
    }

    public Command outtakeCommand(BooleanSupplier isHeld, Launcher launcher) {
        return Command.build()
                .setStart(() -> {
                    this.reverse();
                })
                .setDone(() -> !isHeld.getAsBoolean())
                .setEnd(endCondition -> {
                    this.disable();
                })
                .requiring(this)
                .requiring(launcher) // Locks out the launcher here too
                .setPriority(0);
    }

    public Command intakeCommandAuton(Launcher launcher)
    {
        return sequential(
                instant(() -> enable(launcher)),
                instant(launcher::reverseFeeders)
        );
    }
    public Command disableIntakeCommandAuton()
    {
        return sequential(
                instant(() -> this.disable())
        );
    }
    public Command reverseIntakeCommandAuton()
    {
        return sequential(
                instant(() -> this.reverseMotor())
        );
    }
}
