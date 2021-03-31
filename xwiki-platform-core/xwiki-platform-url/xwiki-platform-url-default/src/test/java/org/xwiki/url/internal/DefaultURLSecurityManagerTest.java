/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.url.internal;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.xwiki.test.junit5.mockito.ComponentTest;
import org.xwiki.test.junit5.mockito.InjectMockComponents;
import org.xwiki.test.junit5.mockito.MockComponent;
import org.xwiki.url.URLConfiguration;
import org.xwiki.wiki.descriptor.WikiDescriptor;
import org.xwiki.wiki.descriptor.WikiDescriptorManager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link DefaultURLSecurityManager}.
 *
 * @version $Id$
 * @since 13.3RC1
 * @since 12.10.7
 */
@ComponentTest
class DefaultURLSecurityManagerTest
{
    @InjectMockComponents
    private DefaultURLSecurityManager urlSecurityManager;

    @MockComponent
    private URLConfiguration urlConfiguration;

    @MockComponent
    private WikiDescriptorManager wikiDescriptorManager;

    @Test
    void isDomainTrusted() throws Exception
    {
        when(urlConfiguration.getTrustedDomains()).thenReturn(Arrays.asList(
            "foo.acme.org",
            "com" // this should not be taken into account
        ));

        WikiDescriptor wikiDescriptor1 = mock(WikiDescriptor.class);
        when(wikiDescriptor1.getAliases()).thenReturn(Arrays.asList(
            "www.xwiki.org",
            "something.bar.com"
        ));

        WikiDescriptor wikiDescriptor2 = mock(WikiDescriptor.class);
        when(wikiDescriptor2.getAliases()).thenReturn(Collections.singletonList(
            "enterprise.eu"
        ));

        when(this.wikiDescriptorManager.getAll()).thenReturn(Arrays.asList(wikiDescriptor1, wikiDescriptor2));

        assertTrue(this.urlSecurityManager
            .isDomainTrusted(new URL("http://www.xwiki.org/xwiki/bin/view/XWiki/Login")));
        assertTrue(this.urlSecurityManager
            .isDomainTrusted(new URL("https://www.xwiki.org/xwiki/bin/view/XWiki/Login")));
        assertFalse(this.urlSecurityManager
            .isDomainTrusted(new URL("https://www.xwiki.com/xwiki/bin/view/XWiki/Login")));
        assertFalse(this.urlSecurityManager
            .isDomainTrusted(new URL("https://xwiki.org/xwiki/bin/view/XWiki/Login")));
        assertTrue(this.urlSecurityManager
            .isDomainTrusted(new URL("https://foo.acme.org/something/else")));
        assertTrue(this.urlSecurityManager
            .isDomainTrusted(new URL("https://bar.foo.acme.org/something/else")));
        assertTrue(this.urlSecurityManager
            .isDomainTrusted(new URL("https://buz.bar.foo.acme.org/something/else")));
        assertFalse(this.urlSecurityManager
            .isDomainTrusted(new URL("https://acme.org/something/else")));
        assertFalse(this.urlSecurityManager
            .isDomainTrusted(new URL("https://www.acme.org/something/else")));
        assertFalse(this.urlSecurityManager
            .isDomainTrusted(new URL("https://something.bar.thing.com")));
        assertFalse(this.urlSecurityManager
            .isDomainTrusted(new URL("https://bar.thing.com")));
        assertTrue(this.urlSecurityManager
            .isDomainTrusted(new URL("https://something.bar.com")));
        assertTrue(this.urlSecurityManager
            .isDomainTrusted(new URL("https://enterprise.eu/xwiki/")));
    }

    @Test
    void invalidateCache() throws Exception
    {
        when(urlConfiguration.getTrustedDomains()).thenReturn(Collections.singletonList(
            "xwiki.org"
        ));
        assertTrue(this.urlSecurityManager
            .isDomainTrusted(new URL("http://www.xwiki.org")));
        assertFalse(this.urlSecurityManager
            .isDomainTrusted(new URL("https://foo.acme.org/something/else")));

        when(urlConfiguration.getTrustedDomains()).thenReturn(Collections.singletonList(
            "foo.acme.org"
        ));

        // the asserts are still the same because we rely on cached values
        assertTrue(this.urlSecurityManager
            .isDomainTrusted(new URL("http://www.xwiki.org")));
        assertFalse(this.urlSecurityManager
            .isDomainTrusted(new URL("https://foo.acme.org/something/else")));

        // after invalidation the cache has been recomputed.
        this.urlSecurityManager.invalidateCache();
        assertFalse(this.urlSecurityManager
            .isDomainTrusted(new URL("http://www.xwiki.org")));
        assertTrue(this.urlSecurityManager
            .isDomainTrusted(new URL("https://foo.acme.org/something/else")));
    }
}
