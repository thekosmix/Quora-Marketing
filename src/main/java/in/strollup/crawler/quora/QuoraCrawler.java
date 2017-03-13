package in.strollup.crawler.quora;

import in.strollup.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class QuoraCrawler {

	private static Set<String> uniqueQuoraLinks = new HashSet<>();
	private static int count = 1;

	public Document getDocument(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).userAgent("Mozilla").timeout(10000).get();
		} catch (IOException e) {
			// log(e.getMessage());
		}

		return doc;
	}

	private void scrapLinksForSearchJSoup(String keyWord, int cityId) {
		// log("crawling for keyword : " + keyWord + " and city: " +cityId);
		String url = QuoraConstants.getUrl(keyWord, cityId);
		Document doc = getDocument(url);

		Elements els = doc.select("div.g");

		for (Element el : els) {
			String quoraUrl = el.getElementsByTag("cite").text();
			quoraUrl = StringUtils.replace(quoraUrl, " ", "");
			if (!StringUtils.startsWith(quoraUrl, "http")) {
				quoraUrl = "http://" + quoraUrl;
			}
			if (!uniqueQuoraLinks.contains(quoraUrl)) {
				crawlQuoraPage(quoraUrl, true);
			}
		}
	}

	private void crawlQuoraPage(String url, boolean crawlRelated) {
		// log("crawling for Quora url : " + url);
		uniqueQuoraLinks.add(url);
		Document doc = getDocument(url);
		if (doc == null) {
			// log("document is null for quora url: " + url);
			return;
		}

		Elements statsElements = doc.select("div.QuestionStats");
		for (Element statsElement : statsElements) {
			System.out.print(count++ + "\t" + url);
			Elements statsElementValues = statsElement.getElementsByTag("strong");
			int i = 1;
			for (Element statsElementValue : statsElementValues) {
				if (i > 2) {
					break;
				}
				System.out.print("\t" + statsElementValue.getElementsByTag("strong").text());
				i++;
			}
		}

		Elements activityElements = doc.getElementsByClass("QuestionLastActivityTime");
		if (activityElements.size() > 0) {
			for (Element activityElement : activityElements) {
				String lastAsked = StringUtils.remove(activityElement.text(), "Last asked: ");
				if (!lastAsked.contains("201")) {
					lastAsked = lastAsked + ", 2016";
				}
				System.out.println("\t" + lastAsked);
			}
		} else {
			System.out.println();
		}

		if (crawlRelated) {
			// log("crawlRelated is true for : " + url);
			Elements relatedQuestions = doc.getElementsByClass("question_related");
			List<Element> relatedQuestionList = new ArrayList<>();
			for (Element relatedQuestion : relatedQuestions) {
				Elements relatedQuestionHrefs = relatedQuestion.select("a[href]");
				for (Element element : relatedQuestionHrefs) {
					relatedQuestionList.add(element);
				}
			}

			relatedQuestionList = Utils.getSublist(relatedQuestionList, QuoraConstants.numReLatedQuestion);
			for (Element relatedQuestionHref : relatedQuestionList) {
				String relatedUrl = relatedQuestionHref.absUrl("href");
				if (!uniqueQuoraLinks.contains(relatedUrl)) {
					crawlQuoraPage(relatedUrl, false);
				}
			}

		}
	}

	public static void main(String[] args) {
		QuoraCrawler c = new QuoraCrawler();

		for (String keyWord : QuoraConstants.searches) {
			count = 1;
			System.out.println(keyWord);
			for (int i = 1; i <= 2; i++) {
				int cityId = i;
				// c.log("started for cityId : " + cityId);
				c.scrapLinksForSearchJSoup(keyWord, cityId);
			}
		}
	}

}