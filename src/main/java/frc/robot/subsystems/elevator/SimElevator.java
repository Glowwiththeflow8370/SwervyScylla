package frc.robot.subsystems.elevator;

public class SimElevator implements ElevatorBase {
  public SimElevator() {}

  @Override
  public void runElevator(double value) {
    System.out.println("running elevator");
  }

  @Override
  public void stopElevator() {}

  public void setElevatorPosition(double targetPosition) {}

  @Override
  public double getElevatorEncoderValues() {
    return 0.0;
  }
  // public StatusSignal<Angle> getElevatorEncoderValues2() {
  //   return 0.0;
  // }

  public void resetEncoder() {}
}
