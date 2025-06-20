/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation.
 * Copyright (c) 2017, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package ee.jakarta.tck.faces.test.composite;

import static java.lang.System.getProperty;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlHeading1;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlSubmitInput;

/**
 * Tests if composite component that use resourceBundleMap .properties reflects
 * locale changes.
 * 
 * @see https://github.com/eclipse-ee4j/mojarra/issues/5160
 * @see https://issues.apache.org/jira/browse/MYFACES-4491
 * 
 */
@RunWith(Arquillian.class)
public class Issue5160IT {

    @ArquillianResource
    private URL webUrl;
    private WebClient webClient;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return create(ZipImporter.class, getProperty("finalName") + ".war")
                .importFrom(new File("target/" + getProperty("finalName") + ".war"))
                .as(WebArchive.class);
    }

    @Before
    public void setUp() {
        webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setJavaScriptTimeout(120000);
    }

    @Test
    public void testLocalizedCompositeEn() throws Exception {
        assertLocalizedComposite("en-US", "Application", "My precious button", "Button");
    }

    @Test
    public void testLocalizedCompositeEs() throws Exception {
        assertLocalizedComposite("es-ES", "Aplicación", "Mi precioso botón", "Botón");
    }

    @Test
    public void testLocalizedCompositePt() throws Exception {
        assertLocalizedComposite("pt", "Application", "My precious button", "Accionador");
    }

    @Test
    public void testLocalizedCompositePtBr() throws Exception {
        assertLocalizedComposite("pt-BR", "Application", "My precious button", "Botão");
    }

    @Test
    public void testLocalizedCompositePtBrPb() throws Exception {
        assertLocalizedComposite("pt-BR-PB", "Application", "My precious button", "Pitoco");
    }

    @Test
    public void testLocalizedCompositePtBrXx() throws Exception {
        assertLocalizedComposite("pt-BR-XX", "Application", "My precious button", "Botão");
    }

    @Test
    public void testLocalizedCompositePtBrXxYy() throws Exception {
        assertLocalizedComposite("pt-BR-XX-YY", "Application", "My precious button", "Botão");
    }

    @Test
    public void testLocalizedCompositePtXxYy() throws Exception {
        assertLocalizedComposite("pt-XX-YY", "Application", "My precious button", "Accionador");
    }

    @Test
    public void testLocalizedCompositePtXx() throws Exception {
        assertLocalizedComposite("pt-XX", "Application", "My precious button", "Accionador");
    }

    private void assertLocalizedComposite(String acceptLanguage, String headerText, String buttonText, String compositeButtonText) throws Exception {
        webClient.addRequestHeader("Accept-Language", acceptLanguage);
        HtmlPage page = webClient.getPage(webUrl + "issue5160.xhtml");

        HtmlHeading1 h1 = (HtmlHeading1) page.getHtmlElementById("header");
        assertEquals(headerText, h1.getTextContent());

        HtmlSubmitInput btn1 = (HtmlSubmitInput) page.getHtmlElementById("frm:btn");
        assertEquals(buttonText, btn1.getAttribute("value"));

        HtmlSubmitInput btn2 = (HtmlSubmitInput) page.getHtmlElementById("frm:btn1:btn");
        assertEquals(compositeButtonText, btn2.getAttribute("value"));
    }

    @After
    public void tearDown() {
        webClient.close();
    }

}
