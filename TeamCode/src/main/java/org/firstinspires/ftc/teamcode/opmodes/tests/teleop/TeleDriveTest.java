package org.firstinspires.ftc.teamcode.opmodes.tests.teleop;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.configs.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.configs.subsystems.Intake;

@TeleOp (name="Drive Test", group = "Tests")
public class TeleDriveTest extends OpMode {
    private Drivetrain dt;
    private Intake intake;

    public void init()
    {
        dt = new Drivetrain(hardwareMap); // construct drivetrain class
        intake = new Intake(hardwareMap); // construct intake class

        Scheduler.reset(); // Clean schedule before running

        Scheduler.schedule(dt.driveCommand(  // Creates the movement schedule
                () -> -gamepad1.left_stick_y,
                () -> -gamepad1.left_stick_x,
                () -> -gamepad1.right_stick_x,
                () -> false
        ));

    }


    public void loop()
    {
        if (gamepad2.left_bumper && !Scheduler.isScheduled(intake.intakeCommand()))
            Scheduler.schedule(intake.intakeCommand());
        Scheduler.execute();
    }

}
