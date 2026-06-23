package org.firstinspires.ftc.teamcode.configs.subsystems;


import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.commands.Commands.waitMs;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.configs.utils.LookUpTable;
import org.firstinspires.ftc.teamcode.configs.utils.RobotConstants.ShooterConstants;


@Configurable
public class Launcher{

    private DcMotorEx launcher; // flywheel motor
    private CRServo leftFeeder; // left crservo feeder
    private CRServo rightFeeder; // right crservo feeder
    private Servo hood;
    private Intake intake;
    private boolean active = false; // is shooter active
    private double targetRPM = 0; // target velocity for flywheel
    private boolean isBusy = false;
    public double currentVoltage = 12.8;

    public LookUpTable lookUpTable; // lookuptable for vel/angle from dist


    private com.qualcomm.robotcore.hardware.VoltageSensor batteryVoltageSensor;

    public Launcher(HardwareMap hwMap, Intake mainIntake)
    {
        launcher = hwMap.get(DcMotorEx.class, "launcher");
        leftFeeder = hwMap.get(CRServo.class, "left_feeder");
        rightFeeder = hwMap.get(CRServo.class, "right_feeder");
        hood = hwMap.get(Servo.class, "hood");
        intake = mainIntake;

        launcher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        rightFeeder.setDirection(CRServo.Direction.REVERSE);

        batteryVoltageSensor = hwMap.voltageSensor.iterator().next();
        currentVoltage = batteryVoltageSensor.getVoltage();

        lookUpTable = ShooterConstants.addPoints();
    }

    /* ======================= GETTERS/SETTERS =======================  */
    public double getRPM() {return 60 * (getVelocity()/28);}
    public double getVelocity() {return launcher.getVelocity();}
    public void setPower(double power) {launcher.setPower(power);}
    public void setTarget(double target) { targetRPM = target;}
    public double getTarget() {return targetRPM;}
    public void enable() {active = true;}
    public void disable() {active = false;}
    public boolean isReady() {return active && targetRPM > 50 && Math.abs(targetRPM - getRPM()) < ShooterConstants.RPM_TOLERANCE;}
    public void runFeeders()
    {
        leftFeeder.setPower(-ShooterConstants.FULL_SPEED);
        rightFeeder.setPower(ShooterConstants.FULL_SPEED);
        intake.feed();
    }
    public void stopFeeders()
    {
        intake.disable();
        leftFeeder.setPower(ShooterConstants.FULL_SPEED);
        rightFeeder.setPower(-ShooterConstants.FULL_SPEED);
    }
    public boolean isBusy() {return isBusy;}
    /* ======================= COMMANDS =======================  */

    public void periodic()
    {
        if (active)
        {
            // calculate power using pidf controller and makeing sure to now use more power than motor can take (0-1)
            double currentRPM = getRPM();
            double error = targetRPM - currentRPM;

            double rawPower = (ShooterConstants.kP * error) + (ShooterConstants.kV * targetRPM) + Math.signum(targetRPM) * ShooterConstants.kS; // pidf calculation

            double voltageCompensatedPower = rawPower * (12.75 / currentVoltage); // compensate for diffrent battery volatges so would still be accurate

            setPower(Math.max(-1,Math.min(voltageCompensatedPower,1))); // dosen't go over motor limits
        }
       else
           launcher.setPower(-0.2);
    }
    public Command SHOOTBYVALUEFORTEST(double RPM, double hoodAngle)
    {
        return sequential(
                instant(() -> isBusy = true), //set launcher busy
                instant(() -> {intake.reverseMotor();}),
                waitMs(35),
                instant(() -> intake.disable()),
                parallel(
                        instant(() -> {
                            targetRPM = RPM;// get rpm from the lookuptable
                            active = true; // set motor as active
                        }),
                        instant(() -> {
                            hood.setPosition(hoodAngle); // set hood position as value from lookuptable
                        }),
                        instant(() -> {
                            intake.openGate();
                        })
                ),
                waitUntil(this::isReady), // wait until flywheel is in correct speed

                instant(this::runFeeders), // start feeding artifacts for flywheel

                waitMs(ShooterConstants.FEED_TIME_MILLISECONDS), // wait until artifact completly passed through

                instant(() -> {
                    this.stopFeeders();  // stop feeders to not make 2 artifacts pass
                    active = false; // turns off the shooters
                    isBusy = false; // set as not busy and free to use
                    targetRPM = 0;
                })
        );
    }
    public Command openGate() {
        return sequential(
                instant(() -> intake.openGate())
        );
    }
    public Command stopFlywheel(){
        return instant(() -> active = false);
    }
    public Command runFlywheelClose(){
        return sequential(
                instant(() -> targetRPM = 2300),
                instant(() -> active = true)
        );
    }
    public Command runFlywheelMid(){
        return sequential(
                instant(() -> targetRPM = 2800),
                instant(() -> active = true)
        );
    }
    public Command runFlywheelFar(){
        return sequential(
                instant(() -> targetRPM = 3600),
                instant(() -> active = true)
        );
    }
    public Command buildShootCommand(double distance)
    {
        return sequential(
            instant(() -> isBusy = true), //set launcher busy
            instant(() -> {intake.reverseMotor();}),
            waitMs(35),
            instant(() -> intake.disable()),
            parallel(
                instant(() -> {
                    targetRPM = lookUpTable.get(distance)[1]; // get rpm from the lookuptable
                    active = true; // set motor as active
                }),
                instant(() -> {
                    hood.setPosition(lookUpTable.get(distance)[0]); // set hood position as value from lookuptable
                }),
                instant(() -> {
                   intake.openGate();
                })
            ),
            waitUntil(this::isReady), // wait until flywheel is in correct speed

            instant(this::runFeeders), // start feeding artifacts for flywheel

            waitMs(ShooterConstants.FEED_TIME_MILLISECONDS), // wait until artifact completly passed through

            instant(() -> {
                this.stopFeeders();  // stop feeders to not make 2 artifacts pass
                active = false; // turns off the shooters
                isBusy = false; // set as not busy and free to use
                targetRPM = 0;
            })
        );
    }

    public Command buildRapidFireCommand(double distance)
    {
        return sequential(
                instant(() -> isBusy = true), //set launcher busy

                parallel(
                        instant(() -> {
                            targetRPM = lookUpTable.get(distance)[1]; // get rpm from the lookuptable
                            active = true; // set motor as active
                        }),
                        instant(() -> {
                            hood.setPosition(lookUpTable.get(distance)[0]); // set hood position as value from lookuptable
                        }),
                        instant(() -> {
                            intake.openGate();
                        })
                ),
                waitUntil(this::isReady), // wait until flywheel is in correct speed

                instant(this::runFeeders), // start feeding artifacts for flywheel
                waitMs(ShooterConstants.FEED_TIME_MILLISECONDS * 1.35), // wait until artifact completely passed through
                instant(this::stopFeeders),
                waitMs(750),
                instant(this::runFeeders), // start feeding artifacts for flywheel
                waitMs(ShooterConstants.FEED_TIME_MILLISECONDS * 1.2), // wait until artifact completely passed through
                instant(this::stopFeeders),
                waitMs(750),
                instant(this::runFeeders), // start feeding artifacts for flywheel
                waitMs(ShooterConstants.FEED_TIME_MILLISECONDS * 1.1), // wait until artifact completely passed through



                instant(() -> {
                    this.stopFeeders();  // stop feeders to not make 2 artifacts pass
                    active = false; // turns off the shooters
                    isBusy = false; // set as not busy and free to use
                    targetRPM = 0;
                })
        );
    }


}
