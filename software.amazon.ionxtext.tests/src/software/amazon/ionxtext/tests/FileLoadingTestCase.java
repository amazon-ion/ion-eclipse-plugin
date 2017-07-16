/* Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

import java.io.File;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.junit.Assert;
import org.junit.Rule;

import com.google.inject.Inject;


@InjectWith(IonInjectorProvider.class)
public class FileLoadingTestCase
{
    public static final IonInjectorProvider PROVIDER = new IonInjectorProvider();

    @Rule
    public final InjectorProviderRule injected = new InjectorProviderRule(PROVIDER, this);

    @Inject
    public FileResourceHelper resourceHelper;

    @Inject
    public IResourceServiceProvider.Registry serviceProviderRegistry;


    File myTestFile;

    public void setTestFile(File file)
    {
        myTestFile = file;
    }


    //=========================================================================


    void parseSuccessfully()
        throws Exception
    {
        Resource resource = resourceHelper.resource(myTestFile);

        if (resource.getErrors().size() != 0)
        {
            String message =
                "Unexpected errors in " + myTestFile.getPath() +
                ": " + resource.getErrors();
            Assert.fail(message);
        }

        List<Issue> validationIssues = validate(resource);
        if (validationIssues.size() != 0)
        {
            String message =
                    "Unexpected errors in " + myTestFile.getPath() +
                    ": " + validationIssues;
            Assert.fail(message);
        }
    }

    void parseUnsuccessfully()
        throws Exception
    {
        Resource resource = resourceHelper.resource(myTestFile);

        if (resource.getErrors().size() == 0)
        {
            List<Issue> validationIssues = validate(resource);
            if (validationIssues.size() == 0)
            {
                String message =
                    "No errors were reported in " + myTestFile.getPath();
                Assert.fail(message);
            }
        }
    }

    IResourceValidator validator(Resource resource)
    {
        URI uri = resource.getURI();
        IResourceServiceProvider provider =
            serviceProviderRegistry.getResourceServiceProvider(uri);
        return provider.getResourceValidator();
    }

    List<Issue> validate(Resource resource)
    {
        IResourceValidator validator = validator(resource);
        return validator.validate(resource, CheckMode.ALL, null);
    }
}
