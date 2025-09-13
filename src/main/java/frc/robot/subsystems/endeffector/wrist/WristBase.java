package frc.robot.subsystems.endeffector.wrist;

public interface WristBase {

  public default void rotateWrist(double value) {}

  public default void stopWrist() {}

  public default double getWristAngle() {
    return 0.11;
  }

  public default void setWristPosition(double targetAngle) {}
}
