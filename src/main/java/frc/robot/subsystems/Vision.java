package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import frc.robot.variables.Objects;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.variables.MagicNumbers;

public class Vision {

    NetworkTableInstance table = NetworkTableInstance.getDefault();
    NetworkTable cameraTable = table.getTable("chameleon-vision").getSubTable("joemama");
    double targetRotation;
    double boundingHeight;
    double boundingWidth;
    double fittedHeight;
    double fittedWidth;
    double pitch;
    double[] position;
    double yaw;
    public boolean autoLineup = false;

    public Vision() {
        
    }

    public double getTargetRotation() {
        targetRotation = cameraTable.getEntry("targetRotation").getDouble(0.0);
        SmartDashboard.putNumber("Target Rotation: ", targetRotation);
        return cameraTable.getEntry("targetRotation").getDouble(0.0);
    } 

    public Boolean isValid() {
        return cameraTable.getEntry("isValid").getBoolean(false);
    } 

    public double getBoundingHeight() {
        boundingHeight = cameraTable.getEntry("targetBoundingHeight").getDouble(0.0);
        SmartDashboard.putNumber("Bounding Height", boundingHeight);
        return boundingHeight;
    } 

    public double getBoundingWidth() {
        boundingWidth = cameraTable.getEntry("targetBoundingWidth").getDouble(0.0);
        SmartDashboard.putNumber("Bounding Width", boundingWidth);
        return boundingWidth;
    } 

    public double getFittedHeight() {
        fittedHeight = cameraTable.getEntry("targetFittedHeight").getDouble(0.0);
        if(fittedHeight < cameraTable.getEntry("targetFittedWidth").getDouble(0.0)){
            fittedHeight = cameraTable.getEntry("targetFittedHeight").getDouble(0.0);
        }else{
            fittedHeight = cameraTable.getEntry("targetFittedWidth").getDouble(0.0);
        }
        SmartDashboard.putNumber("Fitted Height: ", fittedHeight);
        return fittedHeight;
    } 

    public double getFittedWidth() {
        fittedWidth = cameraTable.getEntry("targetFittedWidth").getDouble(0.0);
        SmartDashboard.putNumber("Fitted Width: ", fittedWidth);
        return fittedWidth;
    } 

    public double getPitch() {
        pitch = cameraTable.getEntry("targetPitch").getDouble(0.0);
        SmartDashboard.putNumber("Pitch: ", pitch);
        return pitch;
    } 

    public double[]  getPosition() {
        double[] theDefault = {0.0,0.0,0.0};
        position = cameraTable.getEntry("targetPose").getDoubleArray(theDefault);
        SmartDashboard.putNumber("X: ", position[0]);
        SmartDashboard.putNumber("Y: ", position[1]);
        SmartDashboard.putNumber("Angle: ", position[2]);
        return position;
    } 

    public double getYaw() {
        yaw = cameraTable.getEntry("targetYaw").getDouble(0.0);
        SmartDashboard.putNumber("Yaw: ", yaw);
        return yaw;
    }

    public void setDriverMode(Boolean liveStream) {
        cameraTable.getEntry("driverMode").setBoolean(liveStream);
    }

    public double spinUp() {
        double result = (.00020216 * (getYaw() * getYaw())) + (0.04 * getYaw()) - 0.126355;
        if(result > 0){
            result = result * ((-0.03426222222 * getYaw()) + 1.106555555555);
        }else if(result < 0){ 
            result = result * ((-0.03426222222 * getYaw()) + 1.106555555555);
        }
        if(result < 5.11 && result > 1.11){
            result = result * 0.6;
        }else{
            result = result * 0.4;
        }
        System.out.println("Spin up getYaw" + getYaw());
        return result;
    }

	public void autonomousLineUp() {
        double result = 1;
        // double result = (-8.333333333 * (Objects.vision.getYaw() * Objects.vision.getYaw())) +1;
        Objects.driveTrain.tankDrive(-Objects.vision.spinUp() * result, Objects.vision.spinUp() * result);
    }

    public double autoShoot(double targetHeight) {
        double distance = heightToDistance(targetHeight);
        double rpmToShoot = autoVelocity(distance);
        SmartDashboard.putNumber("Outtake RPM", rpmToShoot);
        Objects.outtake.farShoot();
        double result = 0;
        return result;
    }

    public double autoVelocity(double distance) {
        double d = distance;
        if(d < 14)
        {
            return (12.5 * (d * d)) - (d * 187.5) + 4325;
        }else{
            return (4.02 * (d*d)) - (90.97 * d) + 4675.26;
        }
    }

    public double heightToDistance(double height) {
        return ((height * height) * 0.0077) - (1.175 * height) + 60.31;
    }

    public boolean validTarget() {
        return validTarget(fittedHeight, fittedWidth);
    }

    public boolean validTarget(double height, double width) {
        double ratio = height / width;
        if(ratio == MagicNumbers.targetRatio) {
            return true;
        } else {
            return false;
        }
    }
}
