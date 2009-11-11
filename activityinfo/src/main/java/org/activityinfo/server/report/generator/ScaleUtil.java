package org.activityinfo.server.report.generator;

/**
 * @author Alex Bertram
 */
public class ScaleUtil {

    public static class Scale {
        double valmin;
        double step;
        double valmax;
    }


//
//    SUBROUTINE SCALE(FMN, FMX, N, VALMIN, STEP, VALMAX, IFAULT)
//  C
//  C     ALGORITHM AS 96  APPL. STATIST. (1976) VOL.25, NO.1
//  C
//  C     Given extreme values FMN, FMX, and the need for a scale with N
//  C     marks, calculates value for the lowest scale mark (VALMIN) and
//  C     step length (STEP) and highest scale mark (VALMAX).
//  C

    public static Scale computeScale(double fmn, double fmx, int n) {

//    REAL FMN, FMX, VALMIN, STEP, VALMAX
//    INTEGER N, IFAULT
        Scale scale = new Scale();

//  C
//  C     Units for step lengths
//  C
//    REAL UNIT(11)
//  C     Local variables
//    INTEGER NUNIT, I, J
//    REAL TOL, ZERO, HALF, ONE, TEN, BIAS, FMAX, FMIN, RN, X, S, RANGE
        int nunit, i, j;
        double  fmax, fmin, rn, x, s, range;

//  C
//  C     Array length unit()
//  C
//    DATA NUNIT /11/
//  C
//  C     Local constant, defining effective equality of values.
//  C
//    DATA TOL /5.0E-6/
//    DATA ZERO /0.0/, HALF /0.5/, ONE /1.0/, TEN /10.0/
//    DATA BIAS /1.0E-4/
//    DATA UNIT /1.0, 1.2, 1.6, 2.0, 2.5, 3.0, 4.0, 5.0, 6.0, 8.0,
//   *        10.0/

        final double tol = 5.0E-6;
        final double bias = 1.0E-4;
        final double[] unit = new double[] { 1, 1.2, 1.6, 2.0, 2.5, 3, 5, 6, 8, 10 };

//  C
//    FMAX = FMX
//    FMIN = FMN
//    IFAULT = 1

        fmax = fmx;
        fmin = fmn;


//  C
//  C     Test for valid parameter values
//  C
//    IF (FMAX .LT. FMIN .OR. N .LE. 1) RETURN
//    IFAULT = 0
//    RN = N - 1
//    X = ABS(FMAX)
//    IF (X .EQ. ZERO) X = ONE
//    IF ((FMAX - FMIN) / X .GT. TOL) GO TO 20

        if( fmax < fmin)
            throw new IllegalArgumentException("fmax cannot be less than fmin.");
        if( n < 1)
            throw new IllegalArgumentException("n must be greater than or equal to n");

        rn = n - 1;
        x = Math.abs(fmax);
        if(x == 0)
            x = 1;
        if( ! ((fmax - fmin) / x > tol)) {

    //  C
    //  C     All values effectively equal
    //  C
    //    IF (FMAX .LT. ZERO) THEN
    //      FMAX = ZERO
    //    ELSE IF (FMAX .EQ. ZERO) THEN
    //      FMAX = ONE
    //    ELSE
    //      FMIN = ZERO
    //    END IF

            if(fmax < 0) {
                fmax = 0;
            } else if (fmax == 0) {
                fmax = 1;
            } else {
                fmin = 0;
            }
        }

//  C
// 20 STEP = (FMAX - FMIN) / RN
//    S = STEP

        scale.step = (fmax - fmin) / rn;
        s = scale.step;

//  C
//  C     Find power of 10
//  C
// 25 IF (S .GE. ONE) GO TO 30
//    S = S * TEN
//    GO TO 25

        while ( ! (s >= 1) ) {
            s = s * 10;
        }

// 30 IF (S .LT. TEN) GO TO 35
//    S = S / TEN
//    GO TO 30

        while( ! (s<10)) {
            s = s / 10d;
        }

//  C
//  C     Calculate STEP
//  C
// 35 X = S - BIAS
//    DO 40 I = 1, NUNIT
//      IF (X .LE. UNIT(I)) GO TO 45
// 40 CONTINUE
// 45 STEP = STEP * UNIT(I) / S
//    RANGE = STEP * RN

        x = s - bias;
        for(i=0; i!= unit.length; ++i) {
            if(x < unit[i])
                break;
        }
        scale.step = scale.step * unit[i] / s;
        range = scale.step * rn;

//  C
//  C     Make first estimate of VALMIN
//  C
//    X = HALF * (ONE + (FMIN + FMAX - RANGE) / STEP)
//    J = X - BIAS
//    IF (X .LT. ZERO) J = J - 1
//    VALMIN = STEP * FLOAT(J)

        x = 0.5 * (1d + (fmin + fmax - range) / scale.step);
        j = (int) (x - bias);
        if( x < 0) j = j - 1;
        scale.valmin = scale.step * j;

//  C
//  C     Test if VALMIN could be zero
//  C
//    IF (FMIN .GE. ZERO .AND. RANGE .GE. FMAX) VALMIN = ZERO
//    VALMAX = VALMIN + RANGE

        if( fmin > 0 && range > fmax) scale.valmin = 0;
        scale.valmax = scale.valmin + range;

//  C
//  C     Test if VALMAX could be zero
//  C
//    IF (FMAX .GT. ZERO .OR. RANGE .LT. -FMIN) RETURN
//    VALMAX = ZERO
//    VALMIN = -RANGE
//    RETURN
//    END

        if(fmax > 0 || range < -fmin) return scale;

        scale.valmax = 0;
        scale.valmin = -range;

        return scale;

    }


}
