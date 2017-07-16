/* Copyright 2016-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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
package software.amazon.ionxtext.tests

import com.google.inject.Inject
import org.eclipse.emf.common.util.EList
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ResourceHelper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import software.amazon.ionxtext.ion.Datagram
import software.amazon.ionxtext.ion.Value

import static org.junit.Assert.assertEquals
import static org.junit.Assert.fail


@RunWith(XtextRunner)
@InjectWith(IonInjectorProvider)
class IonParsingTest {

    @Inject
    private ResourceHelper resourceHelper


    @Test
    def void parseEmptyLongString()
    {
        checkString("''''''", "''''''")
    }

    @Test
    def void parseGeneralLongString()
    {
        checkString("'''a'''", "'''a'''")
        checkString("'''a' '''", "'''a' '''")
    }

    def void checkString(String ionText, String expectedContent)
    {
        val values = parse(ionText)
        assertEquals(1, values.size)
        val str = values.get(0) as software.amazon.ionxtext.ion.String
        assertEquals(expectedContent, str.value)
    }

    def EList<Value> parse(CharSequence text)
    {
        val resource = resourceHelper.resource(text)

        if (resource.errors.size != 0)
        {
            Assert.fail('''Unexpected errors: «resource.errors»''')
        }

        assertEquals("resource contents size", 1, resource.contents.size)

        val dg = resource.contents.get(0) as Datagram
        dg.values
    }
}
