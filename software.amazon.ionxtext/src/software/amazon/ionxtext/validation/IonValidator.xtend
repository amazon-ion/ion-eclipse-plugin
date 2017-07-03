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
package software.amazon.ionxtext.validation

import org.eclipse.emf.ecore.EClass
import org.eclipse.xtext.validation.Check
import software.amazon.ionxtext.ion.BadDecimal
import software.amazon.ionxtext.ion.BadFloat
import software.amazon.ionxtext.ion.BadInt
import software.amazon.ionxtext.ion.BadNulltype
import software.amazon.ionxtext.ion.BadTimestamp
import software.amazon.ionxtext.ion.IonPackage
//import software.amazon.ionxtext.ion.Timestamp
import software.amazon.ion.Timestamp
/**
 * This class contains custom validation rules. 
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
class IonValidator 
    extends AbstractIonValidator 
{
    def badNumericStopper(String ionTypeName, String content, EClass klass) {
        val badChar = content.substring(content.length - 1)

        // TODO This error message isn't always correct.
        //   Example:  1997-2  implicates '2'
        // TODO Escape the badChar (eg if it's a control character).
        val msg = ionTypeName + " is followed by an invalid character near '" + badChar + "'" 

        error(msg, klass.getEStructuralFeature(0))
    }

    @Check
    def badIntStopper(BadInt token) {
        badNumericStopper("int", token.content, IonPackage.Literals.BAD_INT)
    }

    @Check
    def badFloatStopper(BadFloat token) {
        badNumericStopper("float", token.content, IonPackage.Literals.BAD_FLOAT)
    }

    @Check
    def badDecimalStopper(BadDecimal token) {
        badNumericStopper("decimal", token.content, IonPackage.Literals.BAD_DECIMAL)
    }

    @Check
    def badTimestampStopper(BadTimestamp token) {
        badNumericStopper("timestamp", token.content, IonPackage.Literals.BAD_TIMESTAMP)
    }

    @Check
    def badNull(BadNulltype token) {
        error("Invalid null variant", null)
    }

    @Check
    def checkTimestamp(software.amazon.ionxtext.ion.Timestamp token) {
        try {
            var x = software.amazon.ion.Timestamp::valueOf(token.value);
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage(), null);
        }
    }
}
