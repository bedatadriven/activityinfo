package org.sigmah.shared.map;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public abstract class AbstractCoordinateEditor {

    protected String posHemiChars;
    protected String negHemiChars;
    protected String decimalSeperators;

    protected String noNumberErrorMessage;
    protected String tooManyNumbersErrorMessage;
    protected String noHemisphereMessage;
    protected String invalidSecondsMessage;
    protected String invalidMinutesMessage;

    protected double minValue = -180.0;
    protected double maxValue = +180.0;


    public enum Notation {
        DDd,
        DMd,
        DMS
    }

    protected Notation notation = Notation.DMS;

    protected abstract Double parseDouble(String s);

    protected abstract String formatDDd(double value);

    protected abstract String formatShortFrac(double value);

    protected abstract String formatInt(double value);

    public Double parse(String value) throws CoordinateFormatException {

        if(value == null) {
            return null;
        }

        StringBuffer tokens[] = new StringBuffer[] {
                new StringBuffer(),
                new StringBuffer(),
                new StringBuffer() };
        int tokenIndex = 0;
        int i;

        /*
         * To assure correctness, we're going to insist that the
         * user explicitly enter the hemisphere (+/-/N/S).
         *
         * However, if the bounds dictate that the coordinate is
         * in one hemisphere, then we can assume the assign.
         *
         */
        double sign = 0;
        if(maxValue < 0) {
            sign = -1;
        } else if(minValue > 0) {
            sign = +1;
        }



        for(i = 0; i!=value.length(); ++i) {
            char c = value.charAt(i);

            if(c == '-' || negHemiChars.indexOf(c) != -1) {
                sign = -1;
            } else if(c == '+' || posHemiChars.indexOf(c) != -1) {
                sign = 1;
            } else if(Character.isDigit(c) || decimalSeperators.indexOf(c) != -1) {
                if(tokenIndex > 2) {
                    throw new CoordinateFormatException(tooManyNumbersErrorMessage);
                }
                tokens[tokenIndex].append(c);
            } else if(tokenIndex !=2 && tokens[tokenIndex].length() > 0) {
                tokenIndex++;
            }
        }

        if(sign == 0) {
            throw new CoordinateFormatException(noHemisphereMessage);
        }

        if(tokens[0].length() == 0) {
            throw new CoordinateFormatException(noNumberErrorMessage);
        }

        double coordinate = Double.parseDouble(tokens[0].toString());
        notation = Notation.DDd;

        if(tokens[1].length() != 0) {
            double minutes = parseDouble(tokens[1].toString());
            if(minutes < 0 || minutes > 60.0) {
                throw new CoordinateFormatException(invalidMinutesMessage);
            }
            coordinate += minutes / 60.0;
            notation = Notation.DMd;

        }
        if(tokens[2].length() != 0) {
            double seconds = parseDouble(tokens[2].toString());
            if(seconds < 0.0 || seconds > 60.0) {
                throw new CoordinateFormatException(invalidSecondsMessage);
            }
            notation = Notation.DMS;
            coordinate += seconds / 3600.0;
        }

        return coordinate * sign;
    }


    /**
     * Converts a coordinate value into Degree-Minute-decimal notation
     *
     * @param value
     * @return An array of three double values : { sign, degrees, minutes }
     */
    public static double[] convertToDMd(double value) {

        double degrees = Math.floor(Math.abs(value));
        double minutes = (Math.abs(value)-degrees);

        return new double[] { Math.signum(value), degrees, minutes } ;
    }

    /**
     * Converts a coordinate value into Degree-Minute-Second notation
     *
     * @param value
     * @return An array of four double values: { sign, degrees, minutes, seconds }
     */
    public static double[] convertToDMS(double value) {

        double absv = Math.abs(value);

        double degrees = Math.floor(absv);
        double minutes = ((absv - degrees) * 60.0);
        double seconds = ((minutes - Math.floor(minutes)) * 60.0);
        minutes = Math.floor(minutes);

        return new double[] { Math.signum(value), degrees, minutes, seconds };
    }

    public String format(double coordinate) {

        if(notation == Notation.DDd) {
            return formatDDd(coordinate);

        } else {
            StringBuilder sb = new StringBuilder();
            double DMS[];

            if (notation == Notation.DMd) {

                DMS = convertToDMd(coordinate);
                sb.append(formatInt(Math.abs(DMS[1]))).append("° ");
                sb.append(formatShortFrac(DMS[2])).append("' ");

            } else {
                DMS = convertToDMS(coordinate);
                sb.append(formatInt(Math.abs(DMS[1]))).append("° ");
                sb.append(formatInt(DMS[2])).append("' ");
                sb.append(formatShortFrac(DMS[3])).append("\" ");
            }

            if(DMS[0] < 0) {
                sb.append(negHemiChars.charAt(0));
            } else {
                sb.append(posHemiChars.charAt(0));
            }

            return sb.toString();
        }
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public Notation getNotation() {
        return notation;
    }

    public void setNotation(Notation notation) {
        this.notation = notation;
    }
}
