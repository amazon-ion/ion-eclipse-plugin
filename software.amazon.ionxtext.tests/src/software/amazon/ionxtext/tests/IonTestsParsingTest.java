/* Copyright 2012-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

import static software.amazon.ionxtext.tests.UnitTestUtils.IS_ION_TEXT;
import static software.amazon.ionxtext.tests.UnitTestUtils.IS_NOT_SKIPPED_BAD_FILE;
import static software.amazon.ionxtext.tests.UnitTestUtils.IS_NOT_SKIPPED_GOOD_FILE;
import static software.amazon.ionxtext.tests.UnitTestUtils.SKIPPED_BAD_FILES;
import static software.amazon.ionxtext.tests.UnitTestUtils.SKIPPED_GOOD_FILES;
import static software.amazon.ionxtext.tests.UnitTestUtils.getTestdataFile;
import static software.amazon.ionxtext.tests.UnitTestUtils.testdataFiles;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

import software.amazon.ionxtext.tests.UnitTestUtils.And;

/**
 * Tests the Xtext grammar of Ion by parsing all of the ion-tests data files.
 */
@RunWith(XtextRunner.class)
@InjectWith(IonInjectorProvider.class)
public class IonTestsParsingTest
{
    @Inject
    private FileResourceHelper resourceHelper;

    @Inject
    private IResourceServiceProvider.Registry serviceProviderRegistry;

    public IonTestsParsingTest()
    {
    }


    @Test
    public void goodFilesParseSuccessfully()
        throws Exception
    {
        File[] fileNames = testdataFiles(new And(IS_ION_TEXT,
                                                 IS_NOT_SKIPPED_GOOD_FILE),
                                         "good");
        Arrays.sort(fileNames);

        for (File file : fileNames)
        {
            parseSuccessfully(file, file.getPath());
        }
    }


    @Test
    public void badFilesParseUnsuccessfully()
        throws Exception
    {
        File[] fileNames = testdataFiles(new And(IS_ION_TEXT,
                                                 IS_NOT_SKIPPED_BAD_FILE),
                                         "bad");
        Arrays.sort(fileNames);

        for (File file : fileNames)
        {
            parseUnsuccessfully(file, file.getPath());
        }
    }


    @Test
    public void skippedGoodFilesParseUnsuccessfully()
        throws Exception
    {
        File dir = getTestdataFile("good");
        for (String filename : SKIPPED_GOOD_FILES)
        {
            File file = new File(dir, filename);
            parseUnsuccessfully(file, filename);
        }
    }

    @Test
    public void skippedBadFilesParseSuccessfully()
        throws Exception
    {
        File dir = getTestdataFile("bad");
        for (String filename : SKIPPED_BAD_FILES)
        {
            File file = new File(dir, filename);
            parseSuccessfully(file, filename);
        }
    }

    //=========================================================================

    private void parseSuccessfully(File file, String displayName)
        throws Exception
    {
        Resource resource = resourceHelper.resource(file);

        if (resource.getErrors().size() != 0)
        {
            String message =
                "Unexpected errors in " + displayName +
                ": " + resource.getErrors();
            Assert.fail(message);
        }

        List<Issue> validationIssues = validate(resource);
        if (validationIssues.size() != 0)
        {
            String message =
                    "Unexpected errors in " + displayName +
                    ": " + validationIssues;
            Assert.fail(message);
        }
    }

    private void parseUnsuccessfully(File file, String displayName)
        throws Exception
    {
        Resource resource = resourceHelper.resource(file);

        if (resource.getErrors().size() == 0)
        {
            List<Issue> validationIssues = validate(resource);
            if (validationIssues.size() == 0)
            {
                Assert.fail("No errors were reported in " + displayName);
            }
        }
    }

    private IResourceValidator validator(Resource resource)
    {
        URI uri = resource.getURI();
        IResourceServiceProvider provider =
            serviceProviderRegistry.getResourceServiceProvider(uri);
        return provider.getResourceValidator();
    }

    private List<Issue> validate(Resource resource)
    {
        IResourceValidator validator = validator(resource);
        return validator.validate(resource, CheckMode.ALL, null);
    }
}
