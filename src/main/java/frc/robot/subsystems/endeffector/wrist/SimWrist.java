package frc.robot.subsystems.endeffector.wrist;

public class SimWrist implements WristBase {

  private double testAngle;

  public SimWrist() {
    testAngle = 0;
  }

  @Override
  public void rotateWrist(double value) {
    System.out.println("rotating wrist");
    testAngle++;
  }

  @Override
  public void stopWrist() {}

  @Override
  public double getWristAngle() {
    return testAngle;
  }

  public void setWristPosition(double targetAngle) {}
}
