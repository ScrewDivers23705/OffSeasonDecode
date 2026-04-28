package org.firstinspires.ftc.teamcode.configs.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.configs.utils.RobotConstants.KickerConstants;

public class Kicker {
    private DcMotorEx kicker;
    private ElapsedTime timer;

    public Kicker(HardwareMap hwMap)
    {
        kicker = hwMap.get(DcMotorEx.class, "kicker");
        kicker.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    /* ===================== funcs =================== */

    public void enable() {kicker.setPower(0.4);}
    public void disable() {kicker.setPower(0);}
    public void reverse() {kicker.setPower(-0.4);}
    public Command openKickerCommand()
    {
        return sequential(
            instant(this::enable),
            waitMs(KickerConstants.EXPAND_TIME),
            instant(this::disable)
        );
    }
    public Command closeKickerCommand()
    {
        return sequential(
                instant(this::reverse),
                waitMs(KickerConstants.COMPACT_TIME),
                instant(this::disable)
        );
    }
}
