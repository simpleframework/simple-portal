package net.simpleframework.mvc.component.portal.module;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import net.simpleframework.common.Convert;
import net.simpleframework.common.DateUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.common.web.html.HtmlUtils;
import net.simpleframework.ctx.common.xml.XmlDocument;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.portal.PageletBean;
import net.simpleframework.mvc.component.portal.PortalUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class RssUtils {
	public static class RssChannelItem {
		private String title;

		private String link;

		private String description;

		private Date pubDate; // 文章或日志发表时间

		private String comments; // 文章或日志评论地址

		public String getComments() {
			return comments;
		}

		public String getDescription() {
			return description;
		}

		public String getLink() {
			return link;
		}

		public Date getPubDate() {
			return pubDate;
		}

		public String getTitle() {
			return title;
		}
	}

	public static class RssChannel {
		private String title;

		private String link;

		private Collection<RssChannelItem> channelItems;

		public Collection<RssChannelItem> getChannelItems() {
			if (channelItems == null) {
				channelItems = new ArrayList<>();
			}
			return channelItems;
		}

		public String getLink() {
			return link;
		}

		public String getTitle() {
			return title;
		}
	}

	static final String[] RFC822_MASKS = { "EEE, dd MMM yyyy HH:mm:ss z",
			"EEE, dd MMM yyyy hh:mm a z", "yyyy-MM-dd HH:mm:ss" };

	public static RssChannel getRssChannel(final String rssUrl) {
		return getRssChannel(rssUrl, true);
	}

	public static RssChannel getRssChannel(final String rssUrl, final boolean parseItems) {
		if (!StringUtils.hasText(rssUrl)) {
			return null;
		}
		final RssChannel channel = new RssChannel();
		try {
			final URL url = new URL(rssUrl);

			final XmlDocument doc = new XmlDocument(url);
			final XmlElement ele = doc.getRoot().element("channel");
			if (ele != null) {
				channel.title = ele.elementText("title");
				channel.link = ele.elementText("link");
				if (StringUtils.hasText(channel.link)) {
					final String link = channel.link.toLowerCase();
					if ((link.charAt(0) != '/') && !link.startsWith("http://")) {
						channel.link = "http://" + link;
					}
				}
				if (parseItems) {
					final Iterator<?> it = ele.elementIterator("item");
					while (it.hasNext()) {
						final XmlElement iele = (XmlElement) it.next();
						final RssChannelItem item = new RssChannelItem();
						channel.getChannelItems().add(item);
						item.title = iele.elementText("title");
						item.link = iele.elementText("link");
						item.description = iele.elementText("description");
						final String dateString = iele.elementText("pubDate");
						for (final String mask : RFC822_MASKS) {
							final SimpleDateFormat sdf = new SimpleDateFormat(mask, Locale.ENGLISH);
							try {
								item.pubDate = sdf.parse(dateString);
								break;
							} catch (final ParseException e) {
							}
						}
						item.comments = iele.elementText("comments");
					}
				}
			}
		} catch (final IOException ex) {
		}
		return channel;
	}

	public static String rssRender(final PageRequestResponse rRequest) {
		final StringBuilder sb = new StringBuilder();
		try {
			final PageletBean pagelet = PortalUtils.getPageletBean(rRequest);
			final RssModuleHandler rssModule = (RssModuleHandler) PortalModuleRegistryFactory.get()
					.getModuleHandler(pagelet);
			final RssChannel channel = rssModule.getRssChannel();
			if (channel == null) {
			} else {
				final Collection<RssChannelItem> items = channel.getChannelItems();
				final int l = Math.min(rssModule.getRows(), items.size());
				final Iterator<RssChannelItem> it = items.iterator();
				int j = 0;
				sb.append("<ul class=\"rss\">");
				while (j++ < l) {
					final RssChannelItem item = it.next();
					sb.append("<li>");
					sb.append("<a target=\"_blank\" href=\"").append(item.getLink()).append("\">");
					sb.append(item.getTitle()).append("</a>");
					final String desc = item.getDescription();
					if (rssModule.isShowTip() && StringUtils.hasText(desc)) {
						sb.append("<div style=\"display: none;\">");
						sb.append(HtmlUtils.convertHtmlLines(desc)).append("</div>");
					}
					final String pubDate = DateUtils.toDifferenceDate(item.getPubDate());
					if (StringUtils.hasText(pubDate)) {
						sb.append("<span> - ").append(pubDate).append("</span>");
					}
					sb.append("</li>");
				}
				sb.append("</ul><div class=\"rss_more\">");
				sb.append(
						"<a onclick=\"$Actions['rssContentWindow'](_lo_getPagelet(this).params);\">#(RssUtils.0)&raquo;</a>");
				sb.append("</div>");
			}
		} catch (final Exception e) {
			return HtmlUtils.convertHtmlLines(e.toString());
		}
		return sb.toString();
	}

	public static String rssAll(final PageRequestResponse rRequest) {
		final PageletBean pagelet = PortalUtils.getPageletBean(rRequest);
		final RssModuleHandler rssModule = (RssModuleHandler) PortalModuleRegistryFactory.get()
				.getModuleHandler(pagelet);
		final StringBuilder sb = new StringBuilder();
		final RssChannel channel = rssModule.getRssChannel();
		if (channel != null) {
			for (final RssChannelItem item : channel.getChannelItems()) {
				sb.append("<div class='portal_rss'>");
				sb.append("<a target='_blank' href='").append(item.getLink()).append("'>");
				sb.append(item.getTitle()).append("</a>");
				final Date pubDate = item.getPubDate();
				if (pubDate != null) {
					sb.append("<span class='pubDate'> - ")
							.append(Convert.toDateTimeString(item.getPubDate())).append("</span>");
				}
				sb.append("<div class='desc'>").append(item.getDescription());
				sb.append("</div>");
				sb.append("<div class='line'></div>");
				sb.append("</div>");
				sb.append("<div class='portal_rss_line'></div>");
			}
			sb.append(
					JavascriptUtils.wrapScriptTag("$Actions['rssContentWindow'].window.setHeader('RSS - "
							+ channel.getTitle() + "');"));
		}
		return sb.toString();
	}
}
