package tableFilter;

import java.sql.SQLException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class GterTableFilterStrategy implements TableFilterStrategy {
	private TableFilterStoreService _tableFilterStoreService;
	private XPath xpath = XPathFactory.newInstance().newXPath();
	
    private String GOOD_FORMAT_TABLE = "Gter_Formatted_Web_Pages";
    private String BAD_FORMAT_TABLE = "Gter_Non_Formatted_Web_Pages";
    private String CRITERIA = "//div[@class='typeoption']//th[contains(.,'申请学校')]";
    
	public GterTableFilterStrategy(TableFilterStoreService tableFilterStoreService) {
		_tableFilterStoreService = tableFilterStoreService;
	}
	
	public void filterWebPageTable(String tableName) throws SQLException {
		_tableFilterStoreService.truncateTableForWebPages(GOOD_FORMAT_TABLE);
		_tableFilterStoreService.truncateTableForWebPages(BAD_FORMAT_TABLE);
		
		_tableFilterStoreService.createTableForWebPages(GOOD_FORMAT_TABLE);
		_tableFilterStoreService.createTableForWebPages(BAD_FORMAT_TABLE);
		
		int currentIndex = 1;
		try {
			while(_tableFilterStoreService.getHTML(tableName, currentIndex).isPresent()) {
				System.out.println("index: " + currentIndex);
				
				String html = _tableFilterStoreService.getHTML(tableName, currentIndex).get();
				String url = _tableFilterStoreService.getURL(tableName, currentIndex).get();
				TagNode tagNode = new HtmlCleaner().clean(html);
				Document doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
				NodeList nodeList = (NodeList) xpath.compile(CRITERIA).evaluate(doc, XPathConstants.NODESET);
				System.out.println(nodeList.getLength());

							
				if (nodeList.getLength() > 0) {
					_tableFilterStoreService.storeWebPage(GOOD_FORMAT_TABLE, url, html);
				} else {
					_tableFilterStoreService.storeWebPage(BAD_FORMAT_TABLE, url, html);
				}
				currentIndex++;
			}
		} catch (Exception e) {
			System.out.println(String.format("Throw exception for web page index: %d %s", currentIndex, e.toString()));
		}
	}
}
