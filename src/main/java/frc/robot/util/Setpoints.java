package frc.robot.util;

public enum Setpoints {
  // Discount looged numbers lol
  // Should now be set up for "easier"
  // setpoint setup (probs not needed tho lol)
  IDLE(2000.0, 63.0),
  SOURCE(4000.0, 100.0),
  L1_REEF(1450.0, 120.0),
  L2_REEF(6000.0, 170.0),
  L3_REEF(9500.0, 170.0),
  L4_REEF(15500.0, 190.0);
  private double elevatorPos, wristPos;

  private Setpoints(double elevatorPos, double wristPos) {
    this.elevatorPos = elevatorPos;
    this.wristPos = wristPos;
  }

  public double getElevatorPos() {
    return elevatorPos;
  }

  public double getWristPos() {
    return wristPos;
  }
}
