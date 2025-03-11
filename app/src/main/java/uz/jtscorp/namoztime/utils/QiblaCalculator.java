package uz.jtscorp.namoztime.utils;

public class QiblaCalculator {
    private static final double KAABA_LATITUDE = 21.4225; // Ka'ba kengligi
    private static final double KAABA_LONGITUDE = 39.8262; // Ka'ba uzunligi

    public static float calculateQiblaDirection(double latitude, double longitude) {
        double phiK = Math.toRadians(KAABA_LATITUDE);
        double lambdaK = Math.toRadians(KAABA_LONGITUDE);
        double phi = Math.toRadians(latitude);
        double lambda = Math.toRadians(longitude);

        double y = Math.sin(lambdaK - lambda);
        double x = Math.cos(phi) * Math.tan(phiK) - Math.sin(phi) * Math.cos(lambdaK - lambda);

        double qibla = Math.atan2(y, x);
        qibla = Math.toDegrees(qibla);

        // Shimoldan soat yo'nalishi bo'yicha burchakni qaytarish
        float direction = (float) ((qibla + 360) % 360);

        return direction;
    }
} 