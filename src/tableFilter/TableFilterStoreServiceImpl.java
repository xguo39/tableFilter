package tableFilter;

import java.io.IOException;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class TableFilterStoreServiceImpl implements TableFilterStoreService {
	private TableFilterDatabaseExecutor _tableFilterDatabaseExecutor;
	
	public TableFilterStoreServiceImpl(TableFilterDatabaseExecutor tableFilterDatabaseExecutor) {
		_tableFilterDatabaseExecutor = tableFilterDatabaseExecutor;
	}
	
	public void createTableForWebPages(String tableName) throws SQLException {
		  String sql = String.format("CREATE TABLE IF NOT EXISTS `%s` ("
			  		+ "`RecordID` INT(11) NOT NULL AUTO_INCREMENT,"
			  		+ "`URL` text NOT NULL,"
			  		+ "`HTML` longtext,"
			  		+ "PRIMARY KEY (`RecordID`)"
			  		+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;", tableName);
		  _tableFilterDatabaseExecutor.runSql2(sql);
	}
	
	public void truncateTableForWebPages(String tableName) throws SQLException {
		_tableFilterDatabaseExecutor.runSql2(String.format("TRUNCATE %s", tableName));
	}
	
	public void storeWebPage(String tableName, String url, String webPageHtml) throws SQLException {
		//store the URL and HTML to database
		  String sql = String.format("INSERT INTO  `%s`.`%s` (`URL`, `HTML`) VALUES (?, ?);", 
				  TableFilterDatabaseConfig.DATABASE_NAME,
				  tableName);
			PreparedStatement stmtHTML = _tableFilterDatabaseExecutor.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmtHTML.setString(1, url);
			stmtHTML.setString(2, webPageHtml);
			stmtHTML.execute();
	}
	
	public Optional<String> getHTML(String tableName, String url) throws SQLException, IOException {
		  String sql = String.format("select * from %s where URL = '%s'", tableName, url);
		  ResultSet rs = _tableFilterDatabaseExecutor.crawler_runSql(sql);

		  if(rs.next()) {
			  StringBuilder sb = new StringBuilder();
			  Reader in = rs.getCharacterStream("HTML");
			  int buf = -1;
			  while((buf = in.read()) > -1) {
			        sb.append((char)buf);
			  }
			  in.close();
			  return Optional.of(sb.toString());
		  } else {
			  return Optional.empty();
		  }	
	}
	
	public Optional<String> getHTML(String tableName, int index) throws SQLException, IOException {
		  String sql = String.format("select * from %s where RecordId = '%s'", tableName, new Integer(index).toString());
		  ResultSet rs = _tableFilterDatabaseExecutor.crawler_runSql(sql);

		  if(rs.next()) {
			  StringBuilder sb = new StringBuilder();
			  Reader in = rs.getCharacterStream("HTML");
			  int buf = -1;
			  while((buf = in.read()) > -1) {
			        sb.append((char)buf);
			  }
			  in.close();
			  return Optional.of(sb.toString());
		  } else {
			  return Optional.empty();
		  }			
	}
	
	public Optional<String> getURL(String tableName, int indexInWebPageTable) throws SQLException, IOException {
		  String sql = String.format("select * from %s where RecordID = %s", tableName, 
				  indexInWebPageTable);
		  ResultSet rs = _tableFilterDatabaseExecutor.crawler_runSql(sql);
		  if(rs.next()) {
			  StringBuilder sb = new StringBuilder();
			  Reader in = rs.getCharacterStream("URL");
			  int buf = -1;
			  while((buf = in.read()) > -1) {
			        sb.append((char)buf);
			  }
			  in.close();
			  return Optional.of(sb.toString());
		  } else {
			  return Optional.empty();
		  }
	}
}
