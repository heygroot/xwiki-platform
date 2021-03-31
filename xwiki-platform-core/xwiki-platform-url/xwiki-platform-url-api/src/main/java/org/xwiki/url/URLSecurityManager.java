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
package org.xwiki.url;

import java.net.URL;

import org.xwiki.component.annotation.Role;
import org.xwiki.stability.Unstable;

/**
 * Dedicated component to perform security checks on URLs.
 *
 * @version $Id$
 * @since 13.3RC1
 * @since 12.10.7
 */
@Role
@Unstable
public interface URLSecurityManager
{
    /**
     * Check if the given {@link URL} can be trusted based on the trusted domains of the wiki.
     * This method check on both the list of trusted domains given by the configuration
     * (see {@link URLConfiguration#getTrustedDomains()}) and the list of aliases used by the wiki descriptors.
     *
     * @param urlToCheck the URL for which we want to know if the domain is trusted or not.
     * @return {@code true} if the URL domain can be trusted, {@code false} otherwise
     */
    boolean isDomainTrusted(URL urlToCheck);

    /**
     * For performance reasons the URLSecurityManager might use a cache, this method allows to invalidate it.
     */
    void invalidateCache();
}
