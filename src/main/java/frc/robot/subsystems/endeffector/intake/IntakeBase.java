package frc.robot.subsystems.endeffector.intake;

public interface IntakeBase {
  // This is making me go insane, however
  // the reason for all of these interfaces
  // and multiple classes are for two reasons
  // - Cleaness of code
  // - Enableing of robot simulation
  // and maybe even robot replay (but that is
  // above my paygrade atm)
  public default void runIntake(double value) {}

  public default void stopIntake() {}
}
