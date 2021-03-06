package cz.jiripinkas.jsitemapgenerator.generator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.jiripinkas.jsitemapgenerator.AbstractGenerator;
import cz.jiripinkas.jsitemapgenerator.WebPage;

public class RssGenerator extends AbstractGenerator {

	private String webTitle;

	private String webDescription;

	/**
	 * Create RssGenerator
	 * 
	 * @param baseUrl
	 *            Base URL
	 * @param root
	 *            If Base URL is root (for example http://www.javavids.com or if
	 *            it's some path like http://www.javalibs.com/blog)
	 * @param webTitle
	 *            Web title
	 * @param webDescription
	 *            Web description
	 */
	public RssGenerator(String baseUrl, boolean root, String webTitle, String webDescription) {
		super(baseUrl, root);
		this.webTitle = webTitle;
		this.webDescription = webDescription;
	}

	/**
	 * Create RssGenerator. Root = true.
	 * 
	 * @param baseUrl
	 *            Base URL
	 * @param webTitle
	 *            Web title
	 * @param webDescription
	 *            Web description
	 */
	public RssGenerator(String baseUrl, String webTitle, String webDescription) {
		super(baseUrl);
		this.webTitle = webTitle;
		this.webDescription = webDescription;
	}

	/**
	 * This will construct RSS from web pages. Web pages are sorted using
	 * lastMod in descending order (latest is first)
	 * 
	 * @return
	 */
	public String constructRss() {
		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "\n");
		builder.append("<rss version=\"2.0\">" + "\n");
		builder.append("<channel>" + "\n");
		builder.append("<title>" + webTitle + "</title>" + "\n");
		builder.append("<link>" + baseUrl + "</link>" + "\n");
		builder.append("<description>" + webDescription + "</description>" + "\n");

		List<WebPage> webPages = new ArrayList<WebPage>(urls.values());

		Collections.sort(webPages, new Comparator<WebPage>() {

			public int compare(WebPage o1, WebPage o2) {
				return -o1.getLastMod().compareTo(o2.getLastMod());
			}
		});

		Date latestDate = new Date();
		if (webPages.size() > 0) {
			latestDate = webPages.get(0).getLastMod();
		}

		builder.append("<pubDate>" + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH).format(latestDate) + " +0000</pubDate>" + "\n");
		builder.append("<lastBuildDate>" + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH).format(latestDate) + " +0000</lastBuildDate>" + "\n");
		builder.append("<ttl>1800</ttl>" + "\n");

		for (WebPage webPage : webPages) {
			builder.append("<item>" + "\n");

			builder.append("<title>");
			builder.append(webPage.getName());
			builder.append("</title>" + "\n");

			builder.append("<description>");
			builder.append(webPage.getShortDescription());
			builder.append("</description>" + "\n");

			builder.append("<link>");
			builder.append(baseUrl + webPage.getShortName());
			builder.append("</link>" + "\n");

			builder.append("<pubDate>");
			builder.append(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH).format(webPage.getLastMod()) + " +0000");
			builder.append("</pubDate>" + "\n");

			builder.append("</item>" + "\n");
		}
		builder.append("</channel>" + "\n");
		builder.append("</rss>" + "\n");
		return builder.toString();
	}

}
