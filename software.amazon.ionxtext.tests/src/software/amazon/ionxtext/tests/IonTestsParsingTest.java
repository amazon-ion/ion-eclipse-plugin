/*
 * Copyright 2012-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at:
 *
 *     http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.ionxtext.tests;

import static org.junit.Assert.fail;
import static software.amazon.ionxtext.tests.UnitTestUtils.TEXT_ONLY_FILTER;
import static software.amazon.ionxtext.tests.UnitTestUtils.testdataFiles;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipselabs.xtext.utils.unittesting.XtextRunner2;
import org.eclipselabs.xtext.utils.unittesting.XtextTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import software.amazon.ionxtext.ion.Datagram;
import software.amazon.ionxtext.tests.UnitTestUtils.And;
import software.amazon.ionxtext.tests.UnitTestUtils.FileIsNot;

/**
 * Tests the Xtext grammar of Ion by parsing all of the ion-tests data files.
 */
@RunWith(XtextRunner2.class)
@InjectWith(IonInjectorProvider.class)
public class IonTestsParsingTest
    extends XtextTest
{
    public static final FilenameFilter GOOD_SKIP_LIST =
        new FileIsNot(
            "blank.ion",
            "decimalsWithUnderscores.ion",
            "empty.ion",
            "eolCommentCr.ion",
            "strings.ion",
            "symbolEmpty.ion",
            "symbolEmptyWithCR.ion",
            "symbolEmptyWithCRLF.ion",
            "symbolEmptyWithLF.ion",
            "symbolEmptyWithLFLF.ion",
            "utf16.ion",
            "utf32.ion",

            "equivs/symbols.ion",
            "equivs/textNewlines.ion"
        );


    public static final FilenameFilter BAD_SKIP_LIST =
        new FileIsNot(
            "binaryIntWithMultipleUnderscores.ion",
            "binaryIntWithTrailingUnderscore.ion",
            "binaryIntWithUnderscoreAfterRadixPrefix.ion",
            "binaryIntWithUnderscoreInsideRadixPrefix.ion",
            "blob_1.ion",
            "blob_12.ion",
            "blob_3.ion",
            "blob_4.ion",
            "dateWith2DigitYear.ion",
            "dateWithZ.ion",
            "dateWithTerminatingBackslashNL.ion",
            "decimalWithTerminatingBackslashNL.ion",
            "decimalWithTrailingUnderscore.ion",
            "decimalWithUnderscoreAfterDecimalPoint.ion",
            "decimal_1.ion",
            "decimal_10.ion",
            "decimal_14.ion",
            "decimal_2.ion",
            "decimal_3.ion",
            "decimal_4.ion",
            "floatWithTerminatingBackslashNL.ion",
            "float_1.ion",
            "float_11.ion",
            "float_2.ion",
            "float_3.ion",
            "float_7.ion",
            "float_8.ion",
            "hexIntWithMultipleUnderscores.ion",
            "hexIntWithTrailingUnderscore.ion",
            "hexIntWithUnderscoreInsideRadixPrefix.ion",
            "hexWithTerminatingBackslashNL.ion",
            "intWithMultipleUnderscores.ion",
            "intWithTerminatingBackslashNL.ion",
            "intWithTerminatingSlash.ion",
            "intWithTrailingUnderscore.ion",
            "int_1.ion",
            "int_2.ion",
            "int_6.ion",
            "int_7.ion",
            "int_9.ion",
            "invalidVersionMarker_ion_0_0.ion",
            "invalidVersionMarker_ion_1234_0.ion",
            "invalidVersionMarker_ion_1_1.ion",
            "invalidVersionMarker_ion_2_0.ion",
            "listBackslashNL.ion",
            "nonLeapYear.ion",
            "nullDotTimestamps.ion",
            "nullDotTimestampsInSexp.ion",
            "sexpBackslashNL.ion",
            "structBackslashNL.ion",
            "symbol_1.ion",
            "symbol_2.ion",
            "dateDaysInMonth_1.ion",
            "dateDaysInMonth_2.ion",
            "dateDaysInMonth_3.ion",
            "dateDaysInMonth_4.ion",
            "dateDaysInMonth_5.ion",
            "timestamp/outOfRange/day_1.ion",
            "timestamp/outOfRange/day_2.ion",
            "timestamp/outOfRange/hours_2.ion",
            "timestamp/outOfRange/minutes_2.ion",
            "timestamp/outOfRange/month_1.ion",
            "timestamp/outOfRange/month_2.ion",
            "timestamp/outOfRange/offsetHours_1.ion",
            "timestamp/outOfRange/offsetHours_2.ion",
            "timestamp/outOfRange/offsetMinutes_1.ion",
            "timestamp/outOfRange/offsetMinutes_2.ion",
            "timestamp/outOfRange/offsetMinutes_3.ion",
            "timestamp/outOfRange/offsetMinutes_4.ion",
            "timestamp/outOfRange/seconds_2.ion",
            "timestamp/outOfRange/year_1.ion",
            "timestamp/outOfRange/year_2.ion",
            "timestamp/outOfRange/year_3.ion",
            "timestamp/preciseTimeWithLowercaseZulu.ion",
            "timestamp/timeToHoursOffset.ion",
            "timestamp/timeToHoursWithNoZone.ion",
            "timestamp/timeToHoursZulu.ion",
            "timestamp/timeToMillisWithLowercaseZulu.ion",
            "timestamp/timeToMillisWithNoZone.ion",
            "timestamp/timeToMinutesWithLowercaseZulu.ion",
            "timestamp/timeToMinutesWithNoZone.ion",
            "timestamp/timeToSecondsWithLowercaseZulu.ion",
            "timestamp/timeToSecondsWithNoZone.ion",
            "timestamp/timeWithHourZone.ion",
            "timestamp/timestamp_0000-00-00.ion",
            "timestamp/timestamp_0000-00-00T.ion",
            "timestamp/timestamp_0000-00-00T00_00Z.ion",
            "timestamp/timestamp_0000-00-00T00_00_00.0000Z.ion",
            "timestamp/timestamp_0000-00-00T00_00_00Z.ion",
            "timestamp/timestamp_0000-00-01.ion",
            "timestamp/timestamp_0000-00-01T.ion",
            "timestamp/timestamp_0000-00T.ion",
            "timestamp/timestamp_0000-01-00.ion",
            "timestamp/timestamp_0000-01-00T.ion",
            "timestamp/timestamp_0000-01-01.ion",
            "timestamp/timestamp_0000-01-01T.ion",
            "timestamp/timestamp_0000-01T.ion",
            "timestamp/timestamp_0000-12-31.ion",
            "timestamp/timestamp_0000T.ion",
            "timestamp/timestamp_0001-00-00.ion",
            "timestamp/timestamp_0001-00-00T.ion",
            "timestamp/timestamp_0001-00-01.ion",
            "timestamp/timestamp_0001-00-01T.ion",
            "timestamp/timestamp_0001-00T.ion",
            "timestamp/timestamp_0001-01-00.ion",
            "timestamp/timestamp_0001-01-00T.ion",
            "timestamp/timestamp_1.ion",
            "timestamp/timestamp_10.ion",
            "timestamp/timestamp_11.ion",
            "timestamp/timestamp_12.ion",
            "timestamp/timestamp_18.ion",
            "timestamp/timestamp_19.ion",
            "timestamp/timestamp_20.ion",
            "timestamp/timestamp_21.ion",
            "timestamp/timestamp_22.ion",
            "timestamp/timestamp_23.ion",
            "timestamp/timestamp_27.ion",
            "timestamp/timestamp_31.ion",
            "timestamp/timestamp_35.ion",
            "timestamp/timestamp_39.ion",
            "timestamp/timestamp_4.ion",
            "timestamp/timestamp_45.ion",
            "timestamp/timestamp_46.ion",
            "timestamp/timestamp_47.ion",
            "timestamp/timestamp_5.ion",
            "timestamp/timestamp_50.ion",
            "timestamp/timestamp_55.ion",
            "timestamp/timestamp_58.ion",
            "timestamp/timestamp_6.ion",
            "timestamp/timestamp_63.ion",
            "timestamp/timestamp_64.ion",
            "timestamp/timestamp_65.ion",
            "timestamp/timestamp_66.ion",
            "timestamp/timestamp_67.ion",
            "timestamp/timestamp_68.ion",
            "timestamp/timestamp_69.ion",
            "timestamp/timestamp_7.ion",
            "timestamp/timestamp_73.ion",
            "timestamp/timestamp_8.ion",
            "timestamp/timestamp_9.ion",
            "timestampWithTerminatingBackslashNL.ion",
            "timestampWithTerminatingSlash.ion",
            "topLevelBackslashNL.ion",
            "utf8/surrogate_5.ion"
        );


    public IonTestsParsingTest()
        throws Exception
    {
        // Have the framework load files relative to the project root.
        super("file:/" + new File(".").getAbsolutePath());
    }

    /**
     * Returns the expected type of the root element of the data files.
     */
    @Override
    protected Class<? extends EObject> getRootObjectType(final URI uri)
    {
        return Datagram.class;
    }

    @Test
    public void goodFilesParseSuccessfully()
    {
        ignoreSerializationDifferences();
        ignoreFormattingDifferences();

        File[] fileNames = testdataFiles(new And(TEXT_ONLY_FILTER,
                                                 GOOD_SKIP_LIST),
                                         "good");
        Arrays.sort(fileNames);

        for (File file : fileNames)
        {
            testFile(file.getPath());
        }
    }


    @Test
    public void badFilesParseUnsuccessfully()
    {
        ignoreSerializationDifferences();
        ignoreFormattingDifferences();

        File[] fileNames = testdataFiles(new And(TEXT_ONLY_FILTER,
                                                 BAD_SKIP_LIST),
                                         "bad");
        Arrays.sort(fileNames);

        for (File file : fileNames)
        {
            try
            {
                testFile(file.getPath());
            }
            catch (AssertionError e)
            {
                continue;
            }

            fail("Expected file to have errors: " + file);
        }
    }
}
