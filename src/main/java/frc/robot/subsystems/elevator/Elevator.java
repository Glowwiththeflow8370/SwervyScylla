// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.elevator;

import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import java.util.function.DoubleSupplier;
import org.littletonrobotics.junction.Logger;

public class Elevator extends SubsystemBase {

  private ElevatorBase elevator;

  private SysIdRoutine sysId;

  // private LoggedTunableNumber ElevatorAngle;

  public Elevator(ElevatorBase elevator) {
    this.elevator = elevator;

    // Configure SysId (not yet used)
    sysId =
        new SysIdRoutine(
            new SysIdRoutine.Config(
                null,
                null,
                null,
                (state) -> Logger.recordOutput("Elevator/SysIdState", state.toString())),
            new SysIdRoutine.Mechanism((voltage) -> runOpenLoop(voltage.in(Volts)), null, this));
  }

  public void runOpenLoop(double Volts) {
    elevator.setVoltage(Volts);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // Update signal values
    elevator.updateSignalValues();
    SmartDashboard.putNumber("elevator angle", elevator.getElevatorEncoderValues());
    // System.out.println(elevator.getElevatorEncoderValues());
    // SmartDashboard.putNumber("ele2", elevator.getElevatorEncoderValues2());
  }

  public void RunElevator(double value) {
    elevator.runElevator(value);
  }

  public double getEncoderValues() {
    return elevator.getElevatorEncoderValues();
  }

  public void StopElevator() {
    elevator.stopElevator();
  }

  public void resetElevator() {
    elevator.resetEncoder();
  }

  public void setElevatorPosition(double targetPosition) {
    elevator.setElevatorPosition(targetPosition);
  }

  public Command manualElevator(Elevator elevator, DoubleSupplier value) {
    return Commands.run(
        () -> {
          // System.out.println("elevator run value: " + value.getAsDouble());
          elevator.RunElevator(value.getAsDouble());
        },
        elevator);
  }

  public Command posElevator(Elevator elevator, Double value) {
    return Commands.run(
        () -> {
          elevator.setElevatorPosition(value);
          System.out.println("Running elevator");
        },
        elevator);
  }
}
