package org.erhsroboticsclub.robo2013.imaging;

import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import org.erhsroboticsclub.robo2013.utilties.MathX;
import org.erhsroboticsclub.robo2013.utilties.Messenger;

public class ImageProcessing {

    ParticleAnalysisReport targetParticles[] = null;
    CriteriaCollection criteriaCollection = new CriteriaCollection();
    ParticleAnalysisReport topTarget, bottomTarget, leftTarget, rightTarget;
    Messenger msg = new Messenger();
    static final double VERTICAL_FOV = 34.42900061182182;//camera field of view in degrees
    static final double CAMERA_PIXEL_WIDTH = 640;
    static final double CAMERA_PIXEL_HEIGHT = 480;
    static final double CAMERA_HEIGHT = 61;
    static final double PIXELS_PER_DEGREE = CAMERA_PIXEL_HEIGHT / VERTICAL_FOV;
    static final double TARGET_HEIGHT = 100; // NOT ACTUAL VALUE

    public ImageProcessing() {
        criteriaCollection.addCriteria(
                MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        criteriaCollection.addCriteria(
                MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
    }

    /**
     * fills the targetParticles[] array with all found particle analysis reports
     *
     * @param camera the camera to get the particle analysis report from
     * @throws Exception
     */
    public void getParticleAnalysisReports(AxisCamera camera) throws Exception {
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
    }

    /**
     * get the horizontal distance to the target
     *
     * @param targetReport the particle analysis report to use
     * @param targetHeight the height of the target to use
     * @return distance to target, in inches
     */
    public double getDistance(ParticleAnalysisReport targetReport, double targetHeight) {
        double ph = targetReport.boundingRectHeight;
        double delta = targetHeight - CAMERA_HEIGHT;
        double R = TARGET_HEIGHT / MathX.tan(ph / PIXELS_PER_DEGREE);
        double D = 0;

        for (int i = 0; i < 4; i++) {
            double theta = MathX.asin(delta / R);
            double new_ph = ph / MathX.cos(theta);
            R = TARGET_HEIGHT / MathX.tan(new_ph / PIXELS_PER_DEGREE);
            D = MathX.sqrt(R * R - delta * delta);
        }

        return D;
    }

    public ParticleAnalysisReport getTopTarget(ParticleAnalysisReport[] particles) {
        ParticleAnalysisReport topTarget = particles[0];
        for (int i = 0; i < particles.length; i++) {
            ParticleAnalysisReport particle = particles[i];

            if (particle.center_mass_y > topTarget.center_mass_y) {
                topTarget = particle;
            }
        }
        return topTarget;
    }

    public ParticleAnalysisReport getBottomTarget(ParticleAnalysisReport[] particles) {
        ParticleAnalysisReport bottomTarget = particles[0];
        for (int i = 0; i < particles.length; i++) {
            ParticleAnalysisReport particle = particles[i];

            if (particle.center_mass_y < bottomTarget.center_mass_y) {
                bottomTarget = particle;
            }
        }
        return bottomTarget;
    }

    public ParticleAnalysisReport getLeftTarget(ParticleAnalysisReport[] particles) {
        ParticleAnalysisReport leftTarget = particles[0];
        for (int i = 0; i < particles.length; i++) {
            ParticleAnalysisReport particle = particles[i];

            if (particle.center_mass_x < leftTarget.center_mass_x) {
                leftTarget = particle;
            }
        }
        return leftTarget;
    }

    public ParticleAnalysisReport getRightTarget(ParticleAnalysisReport[] particles) {
        ParticleAnalysisReport rightTarget = particles[0];
        for (int i = 0; i < particles.length; i++) {
            ParticleAnalysisReport particle = particles[i];

            if (particle.center_mass_x > rightTarget.center_mass_x) {
                rightTarget = particle;
            }
        }
        return rightTarget;
    }

    public boolean isTopTarget(ParticleAnalysisReport target) {
        
        return false;
    }

    public boolean isBottomTarget(ParticleAnalysisReport targetReport) {

        return false;
    }

    public boolean isLeftTarget(ParticleAnalysisReport targetReport) {

        return false;
    }

    public boolean isRightTarget(ParticleAnalysisReport targetReport) {

        return false;
    }
}
