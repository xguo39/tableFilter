package tableFilter;

public class Main {
	public static void main(String[] args) throws Exception {
		TableFilterDatabaseExecutor tableFilterDatabaseExecutor = new TableFilterDatabaseExecutor();
		TableFilterStoreService tableFilterStoreService = new TableFilterStoreServiceImpl(tableFilterDatabaseExecutor);
        TableFilterStrategy tableFilterStrategy = new GterTableFilterStrategy(tableFilterStoreService);
        tableFilterStrategy.filterWebPageTable("Gter_new_Web_Pages");
	}
}
