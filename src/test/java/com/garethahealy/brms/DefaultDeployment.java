/*
 * #%L
 * brms-helloworld
 * %%
 * Copyright (C) 2013 - 2015 Gareth Healy
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.garethahealy.brms;

import java.io.File;

import com.garethahealy.brms.factories.KieSessionFactory;
import com.garethahealy.brms.facts.Person;
import com.garethahealy.brms.services.HelloWorldService;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public final class DefaultDeployment {

    private DefaultDeployment() {

    }

    public static JavaArchive deployment() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
            .addClass(KieSessionFactory.class)
            .addClass(Person.class)
            .addClass(HelloWorldService.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsResource(new File("src/test/resources/WEB-INF/jboss-deployment-structure.xml"));

        JavaArchive[] libs = Maven.resolver()
            .loadPomFromFile("pom.xml")
            .importCompileAndRuntimeDependencies()
            .importTestDependencies()
            .resolve()
            .withTransitivity()
            .as(JavaArchive.class);

        for (JavaArchive lib : libs) {
            jar = jar.merge(lib);
        }

        return jar;
    }
}
