/*
 * Copyright (c) 2021 Contributors to Eclipse Foundation.
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

package ee.jakarta.tck.faces.test.faces40.javapagewithmetadata;

import static java.lang.System.getProperty;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertTrue;

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
import org.htmlunit.html.HtmlPage;
import ee.jakarta.tck.faces.test.util.htmlunit.DebugOptions;

import jakarta.faces.annotation.View;
import jakarta.faces.view.ViewDeclarationLanguage;
import jakarta.faces.view.facelets.Facelet;

@RunWith(Arquillian.class)
public class Spec1581IT {

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
        DebugOptions.setDebugOptions(webClient);
    }

    @After
    public void tearDown() {
        webClient.close();
    }

    /**
     * @see Facelet
     * @see View
     * @see ViewDeclarationLanguage#getViewMetadata(jakarta.faces.context.FacesContext, String)
     * @see https://github.com/jakartaee/faces/issues/1581
     */
    @Test
    public void test() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "hello.xhtml?id=foo");

        assertTrue(page.asXml().contains("Id is:foo"));
    }

}
