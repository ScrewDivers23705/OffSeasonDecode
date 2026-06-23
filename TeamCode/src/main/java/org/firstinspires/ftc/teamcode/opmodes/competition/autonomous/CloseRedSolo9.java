package org.firstinspires.ftc.teamcode.opmodes.competition.autonomous;




import static com.pedropathing.ivy.Scheduler.schedule;
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
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.configs.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.configs.subsystems.Intake;
import org.firstinspires.ftc.teamcode.configs.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.configs.subsystems.Vision;
import org.firstinspires.ftc.teamcode.configs.utils.Alliance;
import static org.firstinspires.ftc.teamcode.configs.utils.RobotPoses.Red.Close.Solo.*;
import org.firstinspires.ftc.teamcode.configs.utils.TelemetryUtils;
@Autonomous(name = "CloseRedSolo9", group = "RED")
@Configurable
public class CloseRedSolo9 extends LinearOpMode {


    /* ================================ Subsystems ================================ */
    private Drivetrain drivetrain;
    private Launcher launcher;
    private Intake intake;
    private Vision vision;
    private TelemetryUtils comms;
    private Alliance alliance;
    /* ================================ PathChains ================================ */
    private PathChain preLoadsPose;
    private PathChain intakeClose;
    private PathChain shootClose;
    private PathChain intakeSecond;
    private PathChain shootSecond;
    private PathChain leave;
    private PathChain shakeFirst;
    private PathChain shakeSecond;


    public void initialize()
    {
        alliance = Alliance.RED; // alliance for vision and localization
        drivetrain = new Drivetrain(hardwareMap, alliance); // construct drivetrain object
        intake = new Intake(hardwareMap, launcher); // construct intake object
        launcher = new Launcher(hardwareMap, intake); // construct the launcher object
        vision = new Vision(hardwareMap, alliance); // construct the camera object
        comms = new TelemetryUtils(telemetry, drivetrain, launcher, vision, intake); // construct the telemtryutils object sending it all the data
        Scheduler.reset();

        drivetrain.follower.setPose(startPose);

        buildPaths();
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
        shakeFirst = drivetrain.follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(127.000, 82),
                                new Pose(128.071, 81.568),
                                new Pose(128.000, 81.600)
                        )
                )
                .setTValueConstraint(0.95)
                .setTangentHeadingInterpolation()
                .build();
        shootClose = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(intakeFirstPose, shootFirstPose))
                .setLinearHeadingInterpolation(intakeFirstPose.getHeading(),shootFirstPose.getHeading())
                .build();
        intakeSecond = drivetrain.follower.pathBuilder()
                .addPath(new BezierCurve(shootFirstPose,intakeSecondControl1,intakeSecondPose))
                .setConstantHeadingInterpolation(intakeSecondPose.getHeading())
                .setTValueConstraint(0.95)
                .build();
        shakeSecond = drivetrain.follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(132.5, 57),
                                new Pose(132.571, 56.568),
                                new Pose(133.5, 56.600)
                        )
                )
                .setTValueConstraint(0.95)
                .setTangentHeadingInterpolation()
                .build();
        shootSecond = drivetrain.follower.pathBuilder()
                .addPath(new BezierCurve(intakeSecondPose,shootSecondControl1,shootSecondPose))
                .setLinearHeadingInterpolation(intakeSecondPose.getHeading(), shootSecondPose.getHeading())
                .build();
        leave = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(shootPreloadPose,leavePose))
                .setLinearHeadingInterpolation(shootSecondPose.getHeading(),leavePose.getHeading())
                .build();
    }
    private void buildCommands()
    {
        schedule(
                sequential(
                        follow(drivetrain.follower,preLoadsPose),
                        launcher.buildShootCommand(90),
                        launcher.buildShootCommand(90),
                        launcher.buildShootCommand( 90),
                        intake.intakeCommandAuton(),
                        follow(drivetrain.follower,intakeClose,true,0.7),
                        waitMs(400),
                        follow(drivetrain.follower, shakeFirst, true , 0.75),
                        waitMs(400),
                        intake.disableIntakeCommandAuton(),
                        parallel(
                                follow(drivetrain.follower, shootClose),
                                launcher.runFlywheelMid()
                        ),
                        launcher.openGate(),
                        waitMs(50),
                        launcher.buildShootCommand(110),
                        launcher.buildShootCommand(110),
                        launcher.buildShootCommand( 110),
                        intake.intakeCommandAuton(),
                        follow(drivetrain.follower,intakeSecond,true,0.7),
                        waitMs(400),
                        follow(drivetrain.follower, shakeSecond, true, 0.75),
                        waitMs(400),
                        intake.disableIntakeCommandAuton(),
                        waitMs(25),
                        parallel(
                                follow(drivetrain.follower, shootSecond),
                                launcher.runFlywheelMid()
                        ),
                        launcher.openGate(),
                        waitMs(50),
                        launcher.buildShootCommand(125),
                        launcher.buildShootCommand(125),
                        launcher.buildShootCommand( 125),
                        follow(drivetrain.follower,leave)
                )
        );
    }

    public void runOpMode() {

        initialize();

        waitForStart();
        buildCommands();
        while (opModeIsActive()) {
            Scheduler.execute();
            drivetrain.periodic();
            launcher.periodic();
            vision.periodic();
            comms.periodic();
        }
    }
}