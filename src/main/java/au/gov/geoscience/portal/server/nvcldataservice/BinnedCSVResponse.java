package au.gov.geoscience.portal.server.nvcldataservice;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Represents a NVCL CSV download response down sampled into fixed sized bins of X metres.
 * @author Josh Vote (CSIRO)
 *
 */
public class BinnedCSVResponse implements Serializable {

    private double binSize;
    private Bin[] binnedValues;

    /**
     * Length of the bined region in metres
     * @return
     */
    public double getBinSize() {
        return binSize;
    }

    /**
     * One bin for each scalar value
     * @return
     */
    public Bin[] getBinnedValues() {
        return binnedValues;
    }

    /**
     * Length of the bined region in metres
     * @param binSize
     */
    public void setBinSize(double binSize) {
        this.binSize = binSize;
    }

    /**
     * One bin for each scalar value
     * @param bins
     */
    public void setBinnedValues(Bin[] bins) {
        this.binnedValues = bins;
    }

    /**
     * A (sort-of) typed array of (non null) values for scalar. The data is presented
     * in an "aggregate" form where each value in a list represents the "average" data over
     * a window of X metres.
     *
     * @author Josh Vote (CSIRO)
     *
     */
    public class Bin {
        private boolean numeric;
        private String name;
        private List<Map<String, Integer>> stringValues;
        private List<Double> numericValues;
        private List<String> highStringValues;
        private List<Double> startDepths;

        public Bin(String name, List<Double> startDepths, boolean numeric, List<Map<String, Integer>> stringValues, List<String> highStringValues, List<Double> numericValues) {
            super();
            this.name = name;
            this.numeric = numeric;
            this.stringValues = stringValues;
            this.startDepths = startDepths;
            this.highStringValues = highStringValues;
            this.numericValues = numericValues;
        }
        /**
         * The name of the scalar used to generate this bin
         * @return
         */
        public String getName() {
            return name;
        }
        /**
         * Each item (n) corresponds to the Start depth in metres of the first sample used to make the n'th aggregate window
         * @return
         */
        public List<Double> getStartDepths() {
            return startDepths;
        }
        /**
         * Each item (n) corresponds to the Start depth in metres of the first sample used to make the n'th aggregate window
         * @param startDepths
         */
        public void setStartDepths(List<Double> startDepths) {
            this.startDepths = startDepths;
        }
        /**
         * Whether this entire Bin consists of numeric values or string values.
         * If true - stringValues will not be set. If false, numericValues will not be set
         * @return
         */
        public boolean isNumeric() {
            return numeric;
        }

        /**
         * Whether this entire Bin consists of numeric values or string values.
         * If true - stringValues will not be set. If false, numericValues will not be set
         * @param numeric
         */
        public void setNumeric(boolean numeric) {
            this.numeric = numeric;
        }

        /**
         * Each item (n) corresponds to the counts of each unique string in the n'th aggregate window
         * @return
         */
        public List<Map<String, Integer>> getStringValues() {
            return stringValues;
        }

        /**
         * Each item (n) corresponds to the counts of each unique string in the n'th aggregate window
         * @param stringValues
         */
        public void setStringValues(List<Map<String, Integer>> stringValues) {
            this.stringValues = stringValues;
        }

        /**
         * Each item (n) corresponds to the average numeric value in the n'th aggregate window
         * @return
         */
        public List<Double> getNumericValues() {
            return numericValues;
        }

        /**
         * Each item (n) corresponds to the average numeric value in the n'th aggregate window
         * @param numericValues
         */
        public void setNumericValues(List<Double> numericValues) {
            this.numericValues = numericValues;
        }

        /**
         * Each item (n) corresponds to the most common string value in the n'th aggregate window
         * @return
         */
        public List<String> getHighStringValues() {
            return highStringValues;
        }

        /**
         * Each item (n) corresponds to the most common string value in the n'th aggregate window
         * @param highStringValues
         */
        public void setHighStringValues(List<String> highStringValues) {
            this.highStringValues = highStringValues;
        }
    }
}
