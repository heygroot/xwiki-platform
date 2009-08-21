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
package com.xpn.xwiki.plugin.activitystream.api;

import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.doc.XWikiDocument;

/**
 * Manages the activity stream
 * 
 * @version $Id: $
 */
public interface ActivityStream
{
    /**
     * Creates the classes used by the activity stream when necessary
     */
    void initClasses(XWikiContext context) throws XWikiException;

    /**
     * Tranforms space name into stream name
     * 
     * @param space
     * @param context
     * @return
     */
    String getStreamName(String space, XWikiContext context);

    /**
     * Adding and activity event. The Id does not need to be filled as it will be created. Date and
     * Wiki are optional
     * 
     * @param event
     * @param context
     * @throws ActivityStreamException
     */
    void addActivityEvent(ActivityEvent event, XWikiContext context)
        throws ActivityStreamException;

    void addActivityEvent(String streamName, String type, String title, XWikiContext context)
        throws ActivityStreamException;

    void addActivityEvent(String streamName, String type, String title, List<String> params,
        XWikiContext context) throws ActivityStreamException;

    void addDocumentActivityEvent(String streamName, XWikiDocument doc, String type,
        String title, XWikiContext context) throws ActivityStreamException;

    void addDocumentActivityEvent(String streamName, XWikiDocument doc, String type,
        int priority, String title, XWikiContext context) throws ActivityStreamException;

    void addDocumentActivityEvent(String streamName, XWikiDocument doc, String type,
        String title, List<String> params, XWikiContext context) throws ActivityStreamException;

    void addDocumentActivityEvent(String streamName, XWikiDocument doc, String type,
        int priority, String title, List<String> params, XWikiContext context)
        throws ActivityStreamException;

    void deleteActivityEvent(ActivityEvent event, XWikiContext context)
        throws ActivityStreamException;

    List<ActivityEvent> searchEvents(String hql, boolean filter, int nb, int start,
        XWikiContext context) throws ActivityStreamException;

    List<ActivityEvent> searchEvents(String fromHql, String hql, boolean filter, int nb, int start,
            XWikiContext context) throws ActivityStreamException;
    
    List<ActivityEvent> searchEvents(String fromHql, String hql, boolean filter, int nb, int start,
        List<Object> parameterValues, XWikiContext context) throws ActivityStreamException;

    List<ActivityEvent> getEvents(boolean filter, int nb, int start, XWikiContext context)
        throws ActivityStreamException;

    List<ActivityEvent> getEventsForSpace(String space, boolean filter, int nb, int start,
        XWikiContext context) throws ActivityStreamException;

    List<ActivityEvent> getEventsForUser(String user, boolean filter, int nb, int start,
        XWikiContext context) throws ActivityStreamException;

    List<ActivityEvent> getEvents(String streamName, boolean filter, int nb, int start,
        XWikiContext context) throws ActivityStreamException;

    List<ActivityEvent> getEventsForSpace(String streamName, String space, boolean filter,
        int nb, int start, XWikiContext context) throws ActivityStreamException;

    List<ActivityEvent> getEventsForUser(String streamName, String user, boolean filter, int nb,
        int start, XWikiContext context) throws ActivityStreamException;

    SyndEntry getFeedEntry(ActivityEvent event, XWikiContext context);

    SyndEntry getFeedEntry(ActivityEvent event, String suffix, XWikiContext context);

    SyndFeed getFeed(List<ActivityEvent> events, XWikiContext context);

    SyndFeed getFeed(List<ActivityEvent> events, String suffix, XWikiContext context);

    SyndFeed getFeed(List<ActivityEvent> events, String author, String title, String description,
        String copyright, String encoding, String url, XWikiContext context);

    SyndFeed getFeed(List<ActivityEvent> events, String author, String title, String description,
            String copyright, String encoding, String url, String suffix, XWikiContext context);

    String getFeedOutput(List<ActivityEvent> events, String author, String title,
        String description, String copyright, String encoding, String url, String type,
        XWikiContext context);

    String getFeedOutput(List<ActivityEvent> events, String author, String title,
            String description, String copyright, String encoding, String url, String type,
            String suffix, XWikiContext context);

    
    String getFeedOutput(SyndFeed feed, String type);

}
