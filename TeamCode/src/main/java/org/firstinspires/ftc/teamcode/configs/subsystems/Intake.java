package org.firstinspires.ftc.teamcode.configs.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.configs.utils.RobotConstants;

public class Intake {

    private DcMotor intake;
    private Servo gate;
    public int state = 0;
    // Constructor for the hardware
    public Intake(HardwareMap hwMap)
    {
        intake = hwMap.get(DcMotor.class, "intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        gate = hwMap.get(Servo.class, "gate");
    }
    public void enable()
    {
        intake.setPower(RobotConstants.Intake.FORWARD_POWER);
        gate.setPosition(RobotConstants.Intake.CLOSE_POS);
        state = 1;
    }
    public void disable()
    {
        intake.setPower(0);
        gate.setPosition(RobotConstants.Intake.OPEN_POS);
        state = 0;
    }
    public void reverse()
    {
        intake.setPower(RobotConstants.Intake.REVERSE_POWER);
        gate.setPosition(RobotConstants.Intake.OPEN_POS);
        state = -1;
    }
    public void openGate()
    {
        gate.setPosition(RobotConstants.Intake.OPEN_POS);
    }
    public void closeGate()
    {
        gate.setPosition(RobotConstants.Intake.CLOSE_POS);
    }


}
