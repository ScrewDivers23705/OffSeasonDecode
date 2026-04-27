package org.firstinspires.ftc.teamcode.opmodes.tests.teleop;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.configs.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.configs.subsystems.Intake;
import org.firstinspires.ftc.teamcode.configs.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.configs.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.configs.subsystems.Vision;

@TeleOp (name="Drive Test", group = "Tests")
public class TeleDriveTest extends OpMode {
    private Drivetrain dt;
    private Launcher launcher;
    private Intake intake;
    private Vision vision;
    private Kicker kicker;
    int tag = 24;

    private Command intakeCmd;
    private Command outtakeCmd;

    public void init()
    {
        dt = new Drivetrain(hardwareMap); // construct drivetrain object
        intake = new Intake(hardwareMap); // construct intake object
        launcher = new Launcher(hardwareMap, intake); // construct the launcher object
        vision = new Vision(hardwareMap); // construct the camera object
        kicker = new Kicker(hardwareMap);

        Scheduler.reset(); // Clean schedule before running

        Scheduler.schedule(dt.driveCommand(  // Creates the movement schedule
                () -> -gamepad1.left_stick_y,
                () -> -gamepad1.left_stick_x,
                () -> gamepad1.right_trigger > 0.5 ? vision.getAutoRotate() : -gamepad1.right_stick_x, // checks if right triggers is held and if so attempts to auto rotate
                () -> false
        ));

        intakeCmd = intake.intakeCommand(() -> gamepad2.left_trigger > 0.5);
        outtakeCmd = intake.outtakeCommand(() -> gamepad2.left_bumper);

    }


    public void loop()
    {
        { // Driver command (lo nahag 2)
            if (gamepad1.left_trigger_pressed && !Scheduler.isScheduled(kicker.openKickerCommand()))
            {
                Scheduler.schedule(kicker.openKickerCommand());
            }
        }

        { // Operator commands (nahag 2)
            if (gamepad2.left_trigger > 0.5 && !Scheduler.isScheduled(intakeCmd))
                Scheduler.schedule(intakeCmd);
            if (gamepad2.left_bumper && !Scheduler.isScheduled(outtakeCmd))
                Scheduler.schedule(outtakeCmd);

            if (gamepad2.right_trigger > 0.5 && !launcher.isBusy())
                Scheduler.schedule(launcher.buildRapidFireCommand(vision.getDistance()));
            if (gamepad2.right_bumper && !launcher.isBusy())
                Scheduler.schedule(launcher.buildShootCommand(vision.getDistance()));
        }

        { // all the periodic commands
            launcher.periodic();
            vision.periodic();

            Scheduler.execute(); //run everything scheduled

        }
        { // telemtry

        }
    }

    public void stop()
    {
        Scheduler.reset();
    }

}
