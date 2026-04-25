package org.firstinspires.ftc.teamcode.configs.utils;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class RobotConstants {
    //constants for shooter
    public static class Shooter{
        public static final double FEED_TIME_SECONDS = 0.25; // servo time to get ball to launcher
        public static final  double STOP_SPEED = 0.0; // cr servo stop
        public static final double FULL_SPEED = 1.0; // cr servo speed
        public static final double kP = 3; // launcher kp
        public static final double FF = 13; // launcher feedforward

        public enum LaunchState
        {
            IDLE,
            SPIN_UP,
            LAUNCH,
            LAUNCHING,
        }
        public LookUpTable addPoints(LookUpTable lookUpTable)
        {
            //TODO transfer all points from old file
        }
    }

    public static class Intake{
        public static final double CLOSE_POS = 0.49; // gate pos to stop artifacts
        public static final double OPEN_POS = 0.0; // gate pos to let artifacts go through
        public static final double FORWARD_POWER = -1.0; // max intake speed
        public static final double REVERSE_POWER = 1.0; // max revers intake speed
    }
}
