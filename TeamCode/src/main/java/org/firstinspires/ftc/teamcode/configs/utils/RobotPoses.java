package org.firstinspires.ftc.teamcode.configs.utils;

import com.pedropathing.geometry.Pose;

public class RobotPoses {
    public static class Red{
        public static class Close {
            public static Pose startPose = RobotConstants.DrivetrainConstants.RED_START_CLOSE_POSE;
            public static Pose shootPreloadPose = new Pose(101,100,Math.toRadians(45));
            public static Pose controlPoint1 = new Pose(86.152, 80.110);
            public static Pose intakeFirst = new Pose(125.934,81.790,Math.toRadians(0));
        }
        public class Far{

        }
    }
    public class Blue{

    }
}
