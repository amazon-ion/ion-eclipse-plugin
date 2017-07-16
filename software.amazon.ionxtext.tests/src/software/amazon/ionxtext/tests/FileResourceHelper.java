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
import java.io.FileInputStream;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.junit4.util.ResourceHelper;

class FileResourceHelper
    extends ResourceHelper
{
    public Resource resource(File file)
        throws Exception
    {
        return resource(file, createResourceSet());
    }

    public Resource resource(File file, ResourceSet resourceSetToUse)
        throws Exception
    {
        FileInputStream in = new FileInputStream(file);
        return resource(in, computeUnusedUri(resourceSetToUse), null, resourceSetToUse);
    }
}
