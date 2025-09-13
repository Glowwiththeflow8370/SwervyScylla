package frc.robot.subsystems.climb;

public interface ClimbBase {

  public class ClimbBaseInputs {}

  // Basic climb operation methods
  // Super simple and (i require them to
  // be in both sims and actual things)
  public default void runClimb(double value) {}

  public default void stopClimb() {}

  // Get the angle of the climb
  public default double getClimbAngle() {
    return 0.0;
  }
}
