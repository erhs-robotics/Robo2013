package org.erhsroboticsclub.robo2013.imaging;

import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import org.erhsroboticsclub.robo2013.utilities.MathX;
import org.erhsroboticsclub.robo2013.utilities.Messenger;

public class ImageProcessing {

    Messenger msg = new Messenger();
    static final double CAMERA_PIXEL_WIDTH = 640;
    static final double CAMERA_PIXEL_HEIGHT = 480;
    static final double CAMERA_DISPLACEMENT = .17;//meters
    static final double FOCAL_LENGTH = .0042;//meters

    public ImageProcessing() {
    }

    public double getDistance(AxisCamera leftCam, AxisCamera rightCam) {
        ParticleAnalysisReport[] leftReport, rightReport;
        double distance = 0;

        try {
            leftReport = getPARs(leftCam);
            msg.printLn("LR done");
            //Thread.sleep(10000);
            rightReport = getPARs(rightCam);
            msg.printLn("RR done");
            if (leftReport.length > 1) {
                System.out.println("WARN: " + leftReport.length + "left reports found!");
            }
            if (rightReport.length > 1) {
                System.out.println("WARN: " + rightReport.length + "right reports found!");
            }
            double disparity = getDisparity(leftReport[0], rightReport[0]);
            distance = calcDistance(disparity);
            
        } catch (Exception e) {
            msg.printLn("Failed to get PAR's!");
        }



        return distance;
    }

    /**
     * fills the targetParticles[] array with all found particle analysis
     * reports
     *
     * @param camera the camera to get the particle analysis report from
     * @throws Exception
     */
    public ParticleAnalysisReport[] getPARs(AxisCamera camera) throws Exception {
        CriteriaCollection criteriaCollection = new CriteriaCollection();

        criteriaCollection.addCriteria(
                MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        criteriaCollection.addCriteria(
                MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);

        ParticleAnalysisReport targetParticles[];
        int erosionCount = 2;
        boolean useConnectivity8 = false;  // use connectivity4 instead
        // get color image
        // seperate the light and dark image creating a binary image,
        // fill rectanges created from colorImage rgb threshold,
        // clean out residual small objects,
        // convexHull,
        // apply a particle filter based on Criteria Collection constraints
        BinaryImage filteredImage = camera.getImage()
                .thresholdRGB(0, 42, 71, 255, 0, 255)
                .removeSmallObjects(useConnectivity8, erosionCount)
                .convexHull(useConnectivity8)
                .particleFilter(criteriaCollection);
        targetParticles = filteredImage.getOrderedParticleAnalysisReports();
        filteredImage.free();
        return targetParticles;
    }

    private double getDisparity(ParticleAnalysisReport left, ParticleAnalysisReport right) {
        double center_x, left_x, right_x, disparity;
        center_x = CAMERA_PIXEL_WIDTH / 2;
        left_x = left.center_mass_x;
        right_x = right.center_mass_x;
        msg.printLn("CI: " + center_x);  // center image
        msg.printLn("TL: " + left_x);    // target left
        msg.printLn("TR: " + right_x);  // target right

        disparity = (left_x - center_x) + (center_x - right_x);
        msg.printLn("D " + right_x);  // disparity

        return disparity;
    }

    //Distance based on the pixel distances x and y
    private static double calcDistance(double disparity) {
        
        return FOCAL_LENGTH * (CAMERA_DISPLACEMENT / disparity - 1);        
    }
}
