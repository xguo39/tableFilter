package tableFilter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public interface TableFilterStoreService {
	void createTableForWebPages(String tableName) throws SQLException;
	void truncateTableForWebPages(String tableName) throws SQLException;
	void storeWebPage(String tableName, String url, String webPageHtml) throws SQLException;
	Optional<String> getHTML(String tableName, String url) throws SQLException, IOException;
	Optional<String> getHTML(String tableName, int index) throws SQLException, IOException;
	Optional<String> getURL(String tableName, int indexInWebPageTable) throws SQLException, IOException;
}
