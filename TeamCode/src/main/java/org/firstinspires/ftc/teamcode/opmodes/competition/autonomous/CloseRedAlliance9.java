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
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.configs.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.configs.subsystems.Intake;
import org.firstinspires.ftc.teamcode.configs.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.configs.utils.Alliance;
import static org.firstinspires.ftc.teamcode.configs.utils.RobotPoses.Red.Close.Solo.*;

import java.util.List;

@Autonomous(name = "CloseRedSolo9", group = "RED")
@Configurable
public class CloseRedAlliance9 extends LinearOpMode {
    private List<LynxModule> hubs;

    /* ================================ Subsystems ================================ */
    private Drivetrain drivetrain;
    private Launcher launcher;
    private Intake intake;
    private Alliance alliance;
    /* ================================ PathChains ================================ */
    private PathChain preLoadsPose;
    private PathChain intakeSecond;
    private PathChain openGate;
    private PathChain shootSecond;
    private PathChain intakeFirst;
    private PathChain shootFirst;


    public void initialize()
    {
        alliance = Alliance.RED; // alliance for vision and localization
        drivetrain = new Drivetrain(hardwareMap, alliance); // construct drivetrain object
        intake = new Intake(hardwareMap, launcher); // construct intake object
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
        intakeSecond = drivetrain.follower.pathBuilder()
                .addPath(new BezierCurve(drivetrain.follower.getPose(), intakeSecondControl1, intakeSecondControl1 ,intakeSecondControl1, intakeSecondPose))
                .setConstantHeadingInterpolation(intakeFirstPose.getHeading())
                .build();
        openGate = drivetrain.follower.pathBuilder()
                .addPath(new BezierCurve())
        shootSecond = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(new Pose(128.800, 80.800), shootFirstPose))
                .setLinearHeadingInterpolation(intakeFirstPose.getHeading(),shootFirstPose.getHeading() + Math.toRadians(2))
                .setTValueConstraint(0.925)
                .build();
        intakeFirst = drivetrain.follower.pathBuilder()
                .addPath(new BezierCurve(shootFirstPose,intakeSecondControl1,intakeSecondPose))
                .setTValueConstraint(0.85)
                .setTimeoutConstraint(500)
                .setConstantHeadingInterpolation(intakeSecondPose.getHeading())
                .build();
        shootFirst = drivetrain.follower.pathBuilder()
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
                        intake.intakeCommandAuton(),
                        follow(drivetrain.follower, intakeFirst,true,0.5),
                        waitMs(350),
                        intake.reverseIntakeCommandAuton(),
                        waitMs(50),
                        intake.intakeCommandAuton(),
                        waitMs(300),                        //follow(drivetrain.follower, shakeSecond, true, 0.7),
                        intake.disableIntakeCommandAuton(),
                        parallel(
                                follow(drivetrain.follower, shootFirst),
                                launcher.runFlywheelMid(),
                                launcher.openGate()
                        ),
                        launcher.buildShootCommand(117.5),
                        waitMs(25),
                        launcher.buildShootCommand(117.5),
                        waitMs(25),
                        launcher.buildShootCommand(117.5),
                        instant(launcher::disable),
                        intake.intakeCommandAuton(),
                        follow(drivetrain.follower, intakeSecond,true,0.5),
                        waitMs(350),
                        intake.reverseIntakeCommandAuton(),
                        waitMs(75),
                        intake.intakeCommandAuton(),
                        waitMs(300),
                        intake.disableIntakeCommandAuton(),
                        parallel(
                                follow(drivetrain.follower, shootFirst),
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
            Scheduler.execute();
        }
    }
}