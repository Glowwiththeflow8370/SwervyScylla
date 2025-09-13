package frc.robot.subsystems.elevator;

public interface ElevatorBase {
  public default void runElevator(double value) {}

  public default void stopElevator() {}

  public default void setElevatorPosition(double targetPosition) {}

  public default void setVoltage(double voltage) {}

  public default double getElevatorEncoderValues() {
    return 0.0;
  }

  public default void updateSignalValues() {
    System.out.println("Your Mother");
  }

  public default void resetEncoder() {}
}
