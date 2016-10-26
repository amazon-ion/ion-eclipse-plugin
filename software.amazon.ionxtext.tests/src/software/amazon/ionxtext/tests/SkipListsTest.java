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

import static org.junit.Assert.assertTrue;
import static software.amazon.ionxtext.tests.UnitTestUtils.SKIPPED_BAD_FILES;
import static software.amazon.ionxtext.tests.UnitTestUtils.SKIPPED_GOOD_FILES;
import static software.amazon.ionxtext.tests.UnitTestUtils.getTestdataFile;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests the global skip lists to ensure they are in sync with the actual ion-tests data.
 */
@RunWith(JUnit4.class)
public class SkipListsTest
{
    private void checkTestFilesExist(String testdataDir, String[] files)
    {
        File dir = getTestdataFile(testdataDir);
        for (String filename : files)
        {
            File subject = new File(dir, filename);
            assertTrue("Test file doesn't exist: " + subject,
                       subject.isFile());
        }
    }

    @Test
    public void skippedGoodFilesExist()
    {
        checkTestFilesExist("good", SKIPPED_GOOD_FILES);
    }

    @Test
    public void skippedBadFilesExist()
    {
        checkTestFilesExist("bad", SKIPPED_BAD_FILES);
    }
}
