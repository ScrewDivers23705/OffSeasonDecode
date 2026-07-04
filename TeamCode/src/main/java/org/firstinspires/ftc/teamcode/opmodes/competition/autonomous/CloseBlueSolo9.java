package org.firstinspires.ftc.teamcode.opmodes.competition.autonomous;




import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;

import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.configs.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.configs.subsystems.Intake;
import org.firstinspires.ftc.teamcode.configs.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.configs.utils.Alliance;
import org.firstinspires.ftc.teamcode.configs.utils.RobotConstants;

import static org.firstinspires.ftc.teamcode.configs.utils.RobotPoses.Blue.Close.Solo.*;

import java.util.List;

@Autonomous(name = "CloseBlueSolo9", group = "BLUE")
@Configurable
public class CloseBlueSolo9 extends LinearOpMode {
    private List<LynxModule> hubs;

    /* ================================ Subsystems ================================ */
    private Drivetrain drivetrain;
    private Launcher launcher;
    private Intake intake;
    private Alliance alliance;
    /* ================================ PathChains ================================ */
    private PathChain preLoadsPose;
    private PathChain intakeClose;
    private PathChain shootClose;
    private PathChain intakeSecond;
    private PathChain shootSecond;


    public void initialize()
    {
        alliance = Alliance.BLUE; // alliance for vision and localization
        drivetrain = new Drivetrain(hardwareMap, alliance); // construct drivetrain object
        intake = new Intake(hardwareMap); // construct intake object
        launcher = new Launcher(hardwareMap, intake); // construct the launcher object
        Scheduler.reset();

        drivetrain.follower.setPose(startPose);

        buildPaths();

        hubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule h : hubs)
            h.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        Scheduler.reset(); // Clean schedule before running

    }
    private void buildPaths()
    {
        preLoadsPose = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPreloadPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPreloadPose.getHeading())
                .build();
        intakeClose = drivetrain.follower.pathBuilder()
                .addPath(new BezierCurve(drivetrain.follower.getPose(), intakeFirstControl1, intakeFirstControl1 ,intakeFirstControl1, intakeFirstPose))
                .setConstantHeadingInterpolation(intakeFirstPose.getHeading())
                .build();
        shootClose = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(intakeFirstPose, shootFirstPose))
                .setLinearHeadingInterpolation(intakeFirstPose.getHeading(),shootFirstPose.getHeading() + Math.toRadians(2))
                .setTValueConstraint(0.925)
                .build();
        intakeSecond = drivetrain.follower.pathBuilder()
                .addPath(new BezierCurve(shootFirstPose,intakeSecondControl1,intakeSecondPose))
                .setTValueConstraint(0.85)
                .setTimeoutConstraint(500)
                .setConstantHeadingInterpolation(intakeSecondPose.getHeading())
                .build();
        shootSecond = drivetrain.follower.pathBuilder()
                .addPath(new BezierCurve(intakeSecondPose,shootSecondControl1,shootSecondPose))
                .setLinearHeadingInterpolation(intakeSecondPose.getHeading(), shootSecondPose.getHeading() + Math.toRadians(1))
                .setTValueConstraint(0.98)
                .setTimeoutConstraint(150)
                .build();
    }
    private void buildCommands()
    {
        schedule(
                sequential(
                        parallel(
                                follow(drivetrain.follower,preLoadsPose),
                                launcher.runFlywheelClose()
                        ),
                        launcher.buildShootCommand(89.5),
                        waitMs(25),
                        launcher.buildShootCommand(89.5),
                        waitMs(25),
                        launcher.buildShootCommand(89.5),
                        instant(launcher::disable),
                        intake.intakeCommandAuton(launcher),
                        follow(drivetrain.follower,intakeClose,true,0.5),
                        waitMs(350),
                        intake.reverseIntakeCommandAuton(),
                        waitMs(75),
                        intake.intakeCommandAuton(launcher),
                        waitMs(300),
                        intake.disableIntakeCommandAuton(),
                        parallel(
                                follow(drivetrain.follower, shootClose),
                                launcher.runFlywheelMid(),
                                launcher.openGate()
                        ),
                        launcher.buildShootCommand(115),
                        waitMs(25),
                        launcher.buildShootCommand(115),
                        waitMs(25),
                        launcher.buildShootCommand(115),
                        instant(launcher::disable),
                        intake.intakeCommandAuton(launcher),
                        follow(drivetrain.follower,intakeSecond,true,0.5),
                        waitMs(350),
                        intake.reverseIntakeCommandAuton(),
                        waitMs(50),
                        intake.intakeCommandAuton(launcher),
                        waitMs(300),                        //follow(drivetrain.follower, shakeSecond, true, 0.7),
                        intake.disableIntakeCommandAuton(),
                        parallel(
                                follow(drivetrain.follower, shootSecond),
                                launcher.runFlywheelMid(),
                                launcher.openGate()
                        ),
                        launcher.buildShootCommand(117.5),
                        waitMs(25),
                        launcher.buildShootCommand(117.5),
                        waitMs(25),
                        launcher.buildShootCommand(117.5),
                        instant(launcher::disable)
                )
        );
    }

    public void runOpMode() {

        initialize();

        waitForStart();
        buildCommands();
        while (opModeIsActive()) {
            for (LynxModule h : hubs) h.clearBulkCache();
            drivetrain.periodic();
            launcher.periodic();
            telemetry.addData("CUR RPM", launcher.getRPM());
            telemetry.addData("TARGET RPM", launcher.getRPM());
            Scheduler.execute();
        }
        RobotConstants.DrivetrainConstants.autonEndPose = drivetrain.follower.getPose();

    }
}