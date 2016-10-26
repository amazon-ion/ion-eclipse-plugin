/* Copyright 2012-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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
import static software.amazon.ionxtext.tests.UnitTestUtils.IS_ION_TEXT;
import static software.amazon.ionxtext.tests.UnitTestUtils.IS_NOT_SKIPPED_BAD_FILE;
import static software.amazon.ionxtext.tests.UnitTestUtils.IS_NOT_SKIPPED_GOOD_FILE;
import static software.amazon.ionxtext.tests.UnitTestUtils.testdataFiles;

import java.io.File;
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

/**
 * Tests the Xtext grammar of Ion by parsing all of the ion-tests data files.
 */
@RunWith(XtextRunner2.class)
@InjectWith(IonInjectorProvider.class)
public class IonTestsParsingTest
    extends XtextTest
{
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

        File[] fileNames = testdataFiles(new And(IS_ION_TEXT,
                                                 IS_NOT_SKIPPED_GOOD_FILE),
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

        File[] fileNames = testdataFiles(new And(IS_ION_TEXT,
                                                 IS_NOT_SKIPPED_BAD_FILE),
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
