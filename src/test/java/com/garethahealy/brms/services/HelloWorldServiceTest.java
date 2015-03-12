/*
 * #%L
 * HelloWorld
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
package com.garethahealy.brms.services;

import javax.inject.Inject;

import com.garethahealy.brms.DefaultDeployment;
import com.garethahealy.brms.factories.KieSessionFactory;
import com.garethahealy.brms.facts.Person;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;

@RunWith(Arquillian.class)
public class HelloWorldServiceTest {

    @Inject
    private HelloWorldService helloWorldService;

    @Deployment
    public static JavaArchive deployment() {
        return DefaultDeployment.deployment();
    }

    @Test
    public void checkForAdultUsingStateful() {
        Person person = new Person();
        person.setName("Adult Gareth");
        person.setIsOver18(null);
        person.setDob(LocalDate.now().minusYears(21));

        Person answer = helloWorldService.doSomething(person);

        Assert.assertNotNull(answer.getIsOver18());
        Assert.assertTrue(answer.getIsOver18());
        Assert.assertEquals("Adult Gareth", answer.getName());
    }

    @Test
    public void checkForChildUsingStateful() {
        KieSessionFactory factory = new KieSessionFactory();
        factory.start();

        KieSession session = factory.getStatefulSession();

        try {
            Person person = new Person();
            person.setName("Child Gareth");
            person.setIsOver18(null);
            person.setDob(LocalDate.now().minusYears(5));

            session.insert(LocalDate.now());
            session.insert(person);
            session.fireAllRules();

            Assert.assertNotNull(person.getIsOver18());
            Assert.assertFalse(person.getIsOver18());
            Assert.assertEquals("Child Gareth", person.getName());
        } finally {
            factory.disposeOf(session);
        }
    }
}
