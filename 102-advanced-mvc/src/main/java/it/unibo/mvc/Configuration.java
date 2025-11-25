package it.unibo.mvc;

/**
 * Encapsulates the concept of configuration.
 */
public final class Configuration {

    private final int max; 
    private final int min;
    private final int attempts;

    private Configuration(final int max, final int min, final int attempts) {
        this.max = max;
        this.min = min;
        this.attempts = attempts;
    }

    /**
     * @return the maximum value
     */
    public int getMax() {
        return max;
    }

    /**
     * @return the minimum value
     */
    public int getMin() {
        return min;
    }

    /**
     * @return the number of attempts
     */
    public int getAttempts() {
        return attempts;
    }

    /**
     * @return true if the configuration is consistent
     */
    public boolean isConsistent() {
        return attempts > 0 && min < max;
    }

    /**
     * .
     */
    public static class Builder {

        private static final int MIN = 0;
        private static final int MAX = 100;
        private static final int ATTEMPTS = 10;

        private int min = MIN;
        private int max = MAX;
        private int attempts = ATTEMPTS;
        private boolean consumed;

        /**
         * @param min1 the minimum value
         * @return this builder, for method chaining
         */
        public Builder setMin(final int min1) {
            this.min = min1;
            return this;
        }

        /**
         * @param max1 the maximum value
         * @return this builder, for method chaining
         */
        public Builder setMax(final int max1) {
            this.max = max1;
            return this;
        }

        /**
         * @param attempts1 the attempts count
         * @return this builder, for method chaining
         */
        public Builder setAttempts(final int attempts1) {
            this.attempts = attempts1;
            return this;
        }

        /**
         * @return a configuration
         */
        public final Configuration build() {
            if (consumed) {
                throw new IllegalStateException("The builder can only be used once");
            }
            consumed = true;
            return new Configuration(max, min, attempts);
        }
    }
}

