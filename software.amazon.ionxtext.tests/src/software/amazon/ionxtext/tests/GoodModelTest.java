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

import software.amazon.ionxtext.tests.UnitTestUtils.And;
import software.amazon.ionxtext.tests.UnitTestUtils.FileIsNot;

/**
 * Perform tests on the good Ion text files. The folder containing the files to
 * test are defined in the constructor. We utilize a unit testing framework to
 * perform Xtext grammar testing. More info about the framework:
 * http://code.google.com/a/eclipselabs.org/p/xtext-utils/wiki/Unit_Testing
 *
 * @author jmma
 *
 */
@RunWith(XtextRunner2.class)
@InjectWith(IonInjectorProvider.class)
public class GoodModelTest
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


    /**
     * Constructor - Define the folder to search for test files
     */
    public GoodModelTest()
        throws Exception
    {
        // Declare the resourceRoot to IonTests test data locally
        super("file:/" + new File(".").getAbsolutePath());
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


    /**
     * Returns the expected type of the root element of the given resource.
     */
    protected Class<? extends EObject> getRootObjectType(final URI uri)
    {
	// TODO configure root object type
        return null;
    }
}
