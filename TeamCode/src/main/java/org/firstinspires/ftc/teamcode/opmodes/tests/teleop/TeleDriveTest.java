package org.firstinspires.ftc.teamcode.opmodes.tests.teleop;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import dev.frozenmilk.dairy.core.command.CommandScheduler;
import dev.frozenmilk.dairy.core.command.InstantCommand;
import dev.frozenmilk.dairy.core.command.button.Trigger;
import dev.frozenmilk.dairy.ftc.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.configs.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.configs.subsystems.Intake;

@TeleOp (name="Drive Test", group = "Tests")
public class TeleDriveTest extends OpMode {
    private Drivetrain dt;
    private Intake intake;

    public void init()
    {
        dt = new Drivetrain(hardwareMap);
        intake = new Intake(hardwareMap);

        Scheduler.reset();


        Scheduler.schedule(dt.driveCommand(
                () -> -gamepad1.left_stick_y,
                () -> -gamepad1.left_stick_x,
                () -> -gamepad1.right_stick_x,
                () -> false
        ));


    }


    public void loop()
    {
        if (gamepad2.left_bumper && !Scheduler.isScheduled(intake.intakeCommand()))

        Scheduler.execute();
    }

}
