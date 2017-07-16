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

import org.eclipse.xtext.junit4.IInjectorProvider;
import org.eclipse.xtext.junit4.IRegistryConfigurator;
import org.junit.rules.ExternalResource;

/**
 * A JUnit rule that emulates the {@class XtextRunner}, for situations when
 * it's not feasible to use that runner.
 */
public final class InjectorProviderRule
    extends ExternalResource
{
    private final IInjectorProvider myInjectorProvider;
    private final Object            myTarget;

    public InjectorProviderRule(IInjectorProvider provider, Object target)
    {
        myInjectorProvider = provider;
        myTarget = target;
    }

    @Override
    protected void before()
    {
        myInjectorProvider.getInjector().injectMembers(myTarget);
        if (myInjectorProvider instanceof IRegistryConfigurator) {
            IRegistryConfigurator registryConfigurator = (IRegistryConfigurator) myInjectorProvider;
            registryConfigurator.setupRegistry();
        }
    }

    @Override
    protected void after()
    {
        if (myInjectorProvider instanceof IRegistryConfigurator) {
            IRegistryConfigurator registryConfigurator = (IRegistryConfigurator) myInjectorProvider;
            registryConfigurator.restoreRegistry();
        }
    }
}