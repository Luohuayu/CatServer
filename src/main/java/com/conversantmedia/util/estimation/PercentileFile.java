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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by jcairns on 3/4/15.
 */
public final class PercentileFile {

    public static void main(final String[] arg) throws IOException {


        for(int i=0; i<arg.length; i++) {
            final String fileName = arg[i];
            final Percentile pFile = new Percentile();
            final BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null) {
                final float sample = Float.parseFloat(line.trim());
                pFile.add(sample);
            }
            br.close();

            Percentile.print(System.out, fileName, pFile);
        }

    }

}
