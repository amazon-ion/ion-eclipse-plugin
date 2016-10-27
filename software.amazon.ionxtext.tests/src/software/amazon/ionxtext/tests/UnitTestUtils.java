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

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Utility methods for Unit Testing.
 *
 * @author jmma
 *
 */
public class UnitTestUtils
{
    private static final File ION_TESTS_PATH = new File("../ion-tests");
    private static final File ION_TESTS_IONTESTDATA_PATH = new File(ION_TESTS_PATH, "iontestdata");

    static
    {
        if (!ION_TESTS_IONTESTDATA_PATH.exists())
        {
            throw new RuntimeException("Cannot locate test data directory.");
        }
    }


    public static final String TEXT_FILE_EXTENSION = ".ion";
    public static final String BINARY_FILE_EXTENSION = ".10n";

    /**
     * Filter to accept only text Ion files.
     */
    public static final FilenameFilter IS_ION_TEXT = new FilenameFilter()
    {
        @Override
        public boolean accept(File dir, String name)
        {
            return name.endsWith(TEXT_FILE_EXTENSION);
        }
    };

    public static final String[] SKIPPED_GOOD_FILES =
        loadSkipListFrom(IonTestsParsingTest.class, "skipped-good-files.txt");

    public static final FilenameFilter IS_NOT_SKIPPED_GOOD_FILE =
        new FileIsNot(SKIPPED_GOOD_FILES);

    public static final String[] SKIPPED_BAD_FILES =
        loadSkipListFrom(IonTestsParsingTest.class, "skipped-bad-files.txt");

    public static final FilenameFilter IS_NOT_SKIPPED_BAD_FILE =
        new FileIsNot(SKIPPED_BAD_FILES);


    public static final class And implements FilenameFilter
    {
        private final FilenameFilter[] myFilters;

        public And(FilenameFilter... filters) { myFilters = filters; }

        @Override
        public boolean accept(File dir, String name)
        {
            for (FilenameFilter filter : myFilters)
            {
                if (! filter.accept(dir, name)) return false;
            }
            return true;
        }
    }

    /**
     * A FilenameFilter that filters file names based on an optional parent
     * directory pathname, and a required file name. Refer to
     * {@link FileIsNot#FileIsNot(String...)} constructor.
     */
    public static final class FileIsNot implements FilenameFilter
    {
        private final String[] mySkips;

        /**
         * {@code filesToSkip} must be of the form ".../filename.ion" where
         * ... is optional and is the parent directory pathname of the file
         * to skip. The leading slash is also optional.
         * <p>
         * Examples of valid parameters are:
         * <ul>
         *      <li>{@code iontestdata/good/non-equivs/filename.ion}</li>
         *      <li>{@code non-equivs/filename.ion}</li>
         *      <li>{@code filename.ion}</li>
         * </ul>
         *
         * @param filesToSkip
         */
        public FileIsNot(String... filesToSkip) { mySkips = filesToSkip; }

        @Override
        public boolean accept(File dir, String name)
        {
            for (String skip : mySkips)
            {
                // Remove leading slash if it already exists
                if (name.startsWith("/"))
                {
                    name = name.substring(1);
                }

                String fullFilePath = "/" + dir.getPath() + "/" + name;

                if (fullFilePath.endsWith(skip)) return false;
            }
            return true;
        }
    }


    /**
     * Gets a {@link File} contained in the test data suite.
     *
     * @param path
     *            is relative to the testdata directory.
     */
    public static File getTestdataFile(String path)
    {
        return new File(ION_TESTS_IONTESTDATA_PATH, path);
    }


    /**
     * Loads all the filtered files in the folder and append them to a List.
     *
     * @param folderName
     * @return
     */
    public static List<String> loadTestData(String folderName, FilenameFilter filter) {
        List<String> list = new ArrayList<>();

        File dir = getTestdataFile(folderName);

        if (!dir.isDirectory()) {
            fail(dir.getAbsolutePath() + " is not a directory");
        }

        for (File f : dir.listFiles(filter)) {
            String fileName = f.getName();
            list.add(fileName);
        }

        return list;
    }

    private static void testdataFiles(FilenameFilter filter,
                                      File dir,
                                      List<File> results,
                                      boolean recurse)
    {
        String[] fileNames = dir.list();
        if (fileNames == null)
        {
            String message = "Not a directory: " + dir.getAbsolutePath();
            throw new IllegalArgumentException(message);
        }

        // Sort the fileNames so they are listed in order.
        // This is not a functional requirement but it helps humans scanning
        // the output looking for a specific file.
        Arrays.sort(fileNames);

        for (String fileName : fileNames)
        {
            File testFile = new File(dir, fileName);
            if (testFile.isDirectory())
            {
                if (recurse)
                {
                    testdataFiles(filter, testFile, results, recurse);
                }
            }
            else if (filter == null || filter.accept(dir, fileName))
            {
                results.add(testFile);
            }
        }
    }

    public static File[] testdataFiles(FilenameFilter filter,
                                       boolean recurse,
                                       String... testdataDirs)
    {
        ArrayList<File> files = new ArrayList<File>();

        for (String testdataDir : testdataDirs)
        {
            File dir = getTestdataFile(testdataDir);
            if (! dir.isDirectory())
            {
                String message =
                    "testdataDir is not a directory: "
                        + dir.getAbsolutePath();
                throw new IllegalArgumentException(message);
            }

            testdataFiles(filter, dir, files, recurse);
        }

        return files.toArray(new File[files.size()]);
    }


    public static File[] testdataFiles(FilenameFilter filter,
                                       String... testdataDirs)
    {
        return testdataFiles(filter, /* recurse */ true, testdataDirs);
    }


    public static File[] testdataFiles(String... testdataDirs)
    {
        return testdataFiles(null, testdataDirs);
    }

    public static String[] loadSkipListFrom(Class<?> klass, String filename)
    {
        // The URL->InputStream->Reader approach is required to make this work
        // inside OSGi as well as outside.  We can't assume that files exist
        // for this data, they may be inside a Jar bundle.
        URL url = klass.getResource(filename);
        try (InputStream in = url.openStream();
             Reader r = new InputStreamReader(in);
             BufferedReader br = new BufferedReader(r);
             Stream<String> lines = br.lines())
        {
            return lines.toArray(String[]::new);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
