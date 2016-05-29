package tableFilter;

import java.sql.SQLException;

public interface TableFilterStrategy {
	void filterWebPageTable(String tableName) throws SQLException;
}
