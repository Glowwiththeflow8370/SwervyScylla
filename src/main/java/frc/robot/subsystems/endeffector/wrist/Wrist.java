// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.endeffector.wrist;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.DoubleSupplier;

public class Wrist extends SubsystemBase {

  WristBase wrist;

  /** Creates a new Wrist. */
  public Wrist(WristBase wrist) {
    this.wrist = wrist;
  }

  public void RotateWrist(double value) {
    wrist.rotateWrist(value);
  }

  public void StopWrist() {
    wrist.stopWrist();
  }

  public double GetAngle() {
    return wrist.getWristAngle();
  }

  public void setWristPosition(double targetAngle) {
    wrist.setWristPosition(targetAngle);
  }

  public Command manualWrist(Wrist wrist, DoubleSupplier value) {
    return Commands.run(
        () -> {
          // System.out.println("Wrist run value: " + value.getAsDouble());
          wrist.RotateWrist(value.getAsDouble());
        },
        wrist);
  }

  public Command posWrist(Wrist wrist, Double value) {
    return Commands.run(
        () -> {
          wrist.setWristPosition(value);
          System.out.println("Running Wrist");
        },
        wrist);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Wrist angle", wrist.getWristAngle());
  }
}
