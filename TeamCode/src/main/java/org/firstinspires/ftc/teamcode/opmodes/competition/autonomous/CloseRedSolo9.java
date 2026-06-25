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
                                new Pose(128.000, 81),
                                new Pose(128.1, 80.77),
                                new Pose(128.000, 80.80)
                        )
                )
                .setTValueConstraint(0.8)
                .setTimeoutConstraint(350)
                .setTangentHeadingInterpolation()
                .build();
        shootClose = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(new Pose(128.800, 80.800), shootFirstPose))
                .setLinearHeadingInterpolation(intakeFirstPose.getHeading(),shootFirstPose.getHeading())
                .setTValueConstraint(0.925)
                .build();
        intakeSecond = drivetrain.follower.pathBuilder()
                .addPath(new BezierCurve(shootFirstPose,intakeSecondControl1,intakeSecondPose))
                .setConstantHeadingInterpolation(intakeSecondPose.getHeading())
                .build();
        shakeSecond = drivetrain.follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(132.5, 55.8),
                                new Pose(132.6, 55.5),
                                new Pose(132.5, 55.6)
                        )
                )
                .setTValueConstraint(0.85)
                .setTimeoutConstraint(350)
                .setTangentHeadingInterpolation()
                .build();
        shootSecond = drivetrain.follower.pathBuilder()
                .addPath(new BezierCurve(new Pose(132.5,55.6),shootSecondControl1,shootSecondPose))
                .setLinearHeadingInterpolation(intakeSecondPose.getHeading(), shootSecondPose.getHeading())
                .setTValueConstraint(0.98)
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
                        launcher.shootAutonCommand(92, 310),
                        launcher.shootAutonCommand(92, 310),
                        launcher.shootAutonCommand(92, 310),
                        instant(launcher::disable),
                        intake.intakeCommandAuton(),
                        follow(drivetrain.follower,intakeClose,true,0.6),
                        waitMs(500),
                        follow(drivetrain.follower, shakeFirst, true , 0.7),
                        waitMs(600),
                        intake.disableIntakeCommandAuton(),
                        parallel(
                                follow(drivetrain.follower, shootClose),
                                launcher.runFlywheelMid()
                        ),
                        launcher.openGate(),
                        waitMs(75),
                        launcher.shootAutonCommand(107, 260),
                        launcher.shootAutonCommand(107, 270),
                        launcher.shootAutonCommand(107, 325),
                        instant(launcher::disable),
                        intake.intakeCommandAuton(),
                        follow(drivetrain.follower,intakeSecond,true,0.6),
                        waitMs(500),
                        //follow(drivetrain.follower, shakeSecond, true, 0.7),
                        waitMs(600),
                        intake.disableIntakeCommandAuton(),
                        parallel(
                                follow(drivetrain.follower, shootSecond),
                                launcher.runFlywheelMid()
                        ),
                        launcher.openGate(),
                        waitMs(75),
                        launcher.shootAutonCommand(115, 260),
                        launcher.shootAutonCommand(115, 270),
                        launcher.shootAutonCommand(115, 310),
                        instant(launcher::disable),
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