package org.firstinspires.ftc.teamcode.configs.utils;

import com.pedropathing.geometry.Pose;

public class RobotPoses {
    public static class Red{
        public static class Close {
            public static Pose startPose = RobotConstants.DrivetrainConstants.RED_START_CLOSE_POSE;
            public static Pose shootPreloadPose = new Pose(101,100,Math.toRadians(37));
        }
        public class Far{

        }
    }
    public class Blue{

    }
}
