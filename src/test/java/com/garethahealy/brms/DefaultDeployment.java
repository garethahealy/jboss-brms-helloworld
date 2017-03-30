/*
 * #%L
 * GarethHealy :: JBoss BRMS HelloWorld
 * %%
 * Copyright (C) 2013 - 2017 Gareth Healy
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

import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public final class DefaultDeployment {

    private DefaultDeployment() {

    }

    public static WebArchive deployment() {
        JavaArchive[] libs = Maven.resolver()
            .loadPomFromFile("pom.xml")
            .importCompileAndRuntimeDependencies()
            .importTestDependencies()
            .resolve()
            .withTransitivity()
            .as(JavaArchive.class);

        //Required by BRMS to start up Kie
        ArchivePath pomPropsPath = ArchivePaths.create("maven/" + System.getProperty("groupId") + "/" + System.getProperty("artifactId") + "/pom.properties");

        ArchivePath rulesPath = ArchivePaths.create("com/garethahealy/brms/rules/HelloWorldRule.drl");

        WebArchive jar = ShrinkWrap.create(WebArchive.class)
            .addPackage(KieSessionFactory.class.getPackage())
            .addPackage(Person.class.getPackage())
            .addPackage(HelloWorldService.class.getPackage())
            .addAsLibraries(libs)
            .addAsResource(new File("src/main/resources/com/garethahealy/brms/rules/HelloWorldRule.drl"), rulesPath)
            .addAsManifestResource(new File("target/classes/META-INF/maven/dependencies.properties"), pomPropsPath)
            .addAsManifestResource(new File("src/main/resources/META-INF/kmodule.xml"))
            .addAsWebInfResource(new File("src/test/webapp/WEB-INF/beans.xml"))
            .addAsWebInfResource(new File("src/test/webapp/WEB-INF/jboss-deployment-structure.xml"));

        return jar;
    }
}
