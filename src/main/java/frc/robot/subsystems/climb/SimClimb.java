package frc.robot.subsystems.climb;

public class SimClimb implements ClimbBase {

  public SimClimb() {}

  @Override
  public void runClimb(double value) {
    // System.out.println("running climb");
  }

  @Override
  public void stopClimb() {}

  @Override
  public double getClimbAngle() {
    return 0.0;
  }
}
