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
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;
import org.xwiki.url.URLConfiguration;
import org.xwiki.url.URLSecurityManager;
import org.xwiki.wiki.descriptor.WikiDescriptor;
import org.xwiki.wiki.descriptor.WikiDescriptorManager;
import org.xwiki.wiki.manager.WikiManagerException;

/**
 * Default implementation of {@link URLSecurityManager}.
 * This implementation keeps a HashSet in memory containing the trusted domains defined in the configuration and
 * for all subwikis. Use {@link #invalidateCache()} to compute back this hashset.
 *
 * @version $Id$
 * @since 13.3RC1
 * @since 12.10.7
 */
@Component
@Singleton
public class DefaultURLSecurityManager implements URLSecurityManager
{
    private static final Pattern ACCEPTED_DOMAIN_PATTERN = Pattern.compile("([^.]+\\.[^.]+)+");
    private static final String DOT = ".";

    @Inject
    private URLConfiguration urlConfiguration;

    @Inject
    private WikiDescriptorManager wikiDescriptorManager;

    @Inject
    private Logger logger;

    private Set<String> trustedDomains;

    private void computeTrustedDomains()
    {
        Set<String> domains;
        domains = new HashSet<>(this.urlConfiguration.getTrustedDomains());

        try {
            for (WikiDescriptor wikiDescriptor : wikiDescriptorManager.getAll()) {
                domains.addAll(wikiDescriptor.getAliases());
            }
        } catch (WikiManagerException e) {
            logger.warn("Error while getting wiki descriptor to fill list of trusted domains.", e);
        }
        this.trustedDomains = new HashSet<>();

        for (String domain : domains) {
            if (ACCEPTED_DOMAIN_PATTERN.matcher(domain).matches()) {
                this.trustedDomains.add(domain);
            }
        }
    }

    @Override
    public boolean isDomainTrusted(URL urlToCheck)
    {
        if (this.trustedDomains == null) {
            this.computeTrustedDomains();
        }

        String host = urlToCheck.getHost();

        while (host.contains(DOT)) {
            if (trustedDomains.contains(host)) {
                return true;
            } else {
                host = host.substring(host.indexOf(DOT) + 1);
            }
        }
        return false;
    }

    @Override
    public void invalidateCache()
    {
        this.trustedDomains = null;
    }
}
