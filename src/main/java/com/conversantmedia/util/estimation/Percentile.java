package com.conversantmedia.util.estimation;

/*
 * #%L
 * Conversant Disruptor
 * ~~
 * Conversantmedia.com © 2016, Conversant, Inc. Conversant® is a trademark of Conversant, Inc.
 * ~~
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.PrintStream;
import java.util.Arrays;

/**
 * Implementation of "Simulatenous Estimation of Several Persentiles," by Kimmo E. E. Raatikainen
 *
 * This is very useful for profiling the performance of timing characteristics
 *
 * Created by jcairns on 5/28/14.
 */
public class Percentile {
    private static float[] DEFAULT_PERCENTILE = { 0.05F, 0.5F, 0.683F, 0.75F, 0.85F, 0.954F, 0.99F};

    private final float[] quantiles;

    private final int m;

    private final float[] q; // heights
    private final int[]   n; // actual positions
    private final float[] f; // increments of desired positions
    private final float[] d; // desired positions

    private final float[] e; // estimates

    private boolean isInitializing;

    private int     ni; // which x is initialized so far

    public Percentile() {
        this(DEFAULT_PERCENTILE);
    }

    public Percentile(final float[] quantiles) {

        m = quantiles.length;

        this.quantiles = Arrays.copyOf(quantiles, m);

        final int N = 2*m+3;
        q = new float[N+1];
        n = new int[N+1];
        f = new float[N+1];
        d = new float[N+1];

        e = new float[m];

        clear();
    }


    /**
     * clear existing samples
     */
    public void clear() {

        for(int i=1; i<=2*m+3; i++) {
            n[i] = i+1;
        }

        f[1] = 0F;
        f[2*m+3] = 1F;

        for(int i=1; i<=m; i++) {
            f[2*i+1] = quantiles[i-1];
        }

        for(int i=1; i<=m+1; i++) {
            f[2*i] = (f[2*i-1] + f[2*i+1])/2F;
        }

        for(int i=1; i<=2*m+3; i++) {
            d[i] = 1F + 2*(m+1)*f[i];
        }
        isInitializing = true;
        ni = 1;

    }

    /**
     * Add a measurement to estimate
     *
     * @param x - the value of the measurement
     */
    public void add(final float x) {
        if(isInitializing) {
            q[ni++] = x;

            if(ni == 2*m+3+1) {
                Arrays.sort(q);
                isInitializing=false;
            }
        } else {
            addMeasurement(x);
        }
    }

    /**
     * @return float[] - percentiles requested at initialization
     */
    public float[] getQuantiles() {
        return quantiles;
    }

    /**
     * @return boolean - true if sufficient samples have been seen to form an estimate
     */
    public boolean isReady() {
        return !isInitializing;
    }

    /**
     * @return int - the number of samples in the estimate
     */
    public int getNSamples() {
        if(!isInitializing)
            return n[2*m+3]-1;
        else {
            return ni-1;
        }
    }

    /**
     * get the estimates based on the last sample
     *
     * @return float[]
     *
     * @throws InsufficientSamplesException - if no estimate is currently available due to insufficient data
     */
    public float[] getEstimates() throws InsufficientSamplesException {
        if(!isInitializing) {
            for (int i = 1; i <= m; i++) {
                e[i-1] = q[2*i+1];
            }
            return e;
        } else {
            throw new InsufficientSamplesException();
        }
    }

    /**
     * @return float - the minimum sample seen in the distribution
     */
    public float getMin() {
        return q[1];
    }

    /**
     * @return float - the maximum sample seen in the distribution
     */
    public float getMax() {
        return q[2*m+3];
    }

    private void addMeasurement(final float x) {
        int k=1;

        if(x < q[1]) {
            k = 1;
            q[1] = x;
        } else if(x >= q[2*m+3]) {
            k = 2*m+2;
            q[2*m+3] = x;
        } else {
            for(int i=1; i<=2*m+2; i++) {
                if((q[i] <= x) && (x < q[i+1])) {
                    k=i;
                    break;
                }
            }
        }

        for(int i=k+1; i<=2*m+3; i++) {
            n[i] = n[i]+1;
        }

        for(int i=1; i<=2*m+3; i++) {
            d[i] = d[i] + f[i];
        }

        for(int i=2; i<=2*m+2; i++) {
            final float dval = d[i] - n[i];
            final float dp = n[i+1] - n[i];
            final float dm = n[i-1] - n[i];
            final float qp = (q[i+1] - q[i])/dp;
            final float qm = (q[i-1] - q[i])/dm;
            if((dval >= 1F) && (dp > 1F)) {

                final float qt = q[i] + ((1F - dm) * qp
                        +(dp - 1F)*qm)/(dp - dm);

                if((q[i-1] < qt) && (qt < q[i+1])) {
                    q[i] = qt;
                } else {
                    q[i] = q[i] + qp;
                }
                n[i] = n[i]+1;
            } else if((dval <= -1) && dm < -1) {
                final float qt = q[i] - ((1F + dp)*qm -
                        (dm + 1F)*qp)/(dp - dm);
                if((q[i-1] < qt) && (qt < q[i+1])) {
                    q[i] = qt;
                } else {
                    q[i] = q[i] - qm;
                }
                n[i] = n[i]-1;
            }
        }
    }

    /**
     * print a nice histogram of percentiles
     *
     * @param out - output stream
     * @param name - data set name
     * @param p - percentile
     *
     */

    public static void print(final PrintStream out, final String name, final Percentile p) {
        if(p.isReady()) {
            try {
                final StringBuilder sb = new StringBuilder(512);
                final float[] q = p.getQuantiles();
                final float[] e = p.getEstimates();
                final int SCREENWIDTH = 80;

                sb.append(name);
                sb.append(", min(");
                sb.append(p.getMin());
                sb.append("), max(");
                sb.append(p.getMax());
                sb.append(')');
                sb.append("\n");

                final float max = e[e.length-1];
                for(int i = 0; i<q.length; i++) {
                    sb.append(String.format("%4.3f", q[i]));
                    sb.append(": ");
                    final int len = (int) (e[i]/max*SCREENWIDTH);
                    for(int j = 0; j<len; j++) {
                        sb.append('#');
                    }
                    sb.append(" ");
                    sb.append(String.format("%4.3f\n", e[i]));
                }

                out.println(sb.toString());
            } catch(InsufficientSamplesException e) {
                // this can never occur
            }
        }
    }

    /**
     * Indicates too few measurements have been added to compute the requested
     * estimation
     */
    public class InsufficientSamplesException extends Exception {
        private InsufficientSamplesException() {
        }
    }

}
