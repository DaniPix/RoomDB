# RoomDB

DATABASE

```
@Database(entities = {MonthlyRecap.class}, version = 16)
public abstract class RebtelDB extends RoomDatabase {
  
private static volatile RebtelDB instance;
  
public abstract MonthlyRecapDAO monthlyRecapDAO();

  public static RebtelDB getInstance(Context context) {
     if (instance == null) {
        synchronized (RebtelDB.class) {
           if (instance == null) {
              instance =Room.databaseBuilder(context.getApplicationContext(),
                    RebtelDB.class, "rebtel.db")
                    .addMigrations(MIGRATION_15_16, MIGRATION_16_17)
                    .build();
           }
        }
     }
     return instance;
  }
```

ENTITY

```
@Entity(tableName = "MonthlyRecaps")
public class MonthlyRecap {
 
 @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "_id") 
  private int id;

  @ColumnInfo(name = "date") 
  private String date;

  @ColumnInfo(name = "monthAndYear") 
  private String monthYear;

  @ColumnInfo(name = "totalCallsCount") 
  private int totalCallsCount;

  @ColumnInfo(name = "calledNumbersCount") 
  private int calledNumbersCount;

  @ColumnInfo(name = "destinationCountriesCount") 
  private int destinationCountriesCount;

  @ColumnInfo(name = "contactsCalled") 
  private String contactsCalled;
}
```

DATABASE ACCESS OBJECT

```
@Dao
public interface MonthlyRecapDAO {

  @Nullable
  @Query("SELECT * FROM MonthlyRecaps WHERE monthAndYear=:monthYear")
  LiveData<MonthlyRecap> getMonthlyRecap(String monthYear);

  @Nullable
  @Query("SELECT * FROM MonthlyRecaps")
  LiveData<List<MonthlyRecap>> getMonthlyRecaps();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertMonthlyRecaps(MonthlyRecap... monthlyRecap);
}
```

TYPE CONVERTERS
```

public enum MessageDirection {
	INCOMING(0),
	OUTGOING(1);

	private int direction;

	MessageDirection(int direction) {
		this.direction = direction;
	}

	public int getDirection() {
		return direction;
	}
}


public class MessageDirectionConverter {

	@TypeConverter
	public static int toInt(MessageDirection value) {
		return value.getDirection();
	}

	@TypeConverter
	public static MessageDirection toValue(int value){
		return MessageDirection.values()[value];
	}
}

@ColumnInfo(name = "direction")
	@TypeConverters(MessageDirectionConverter.class)
	private MessageDirection direction;


```



MIGRATION FROM VERSION 15 TO 16
```
private static final Migration MIGRATION_15_16 = new Migration(15, 16) {
  @Override
  public void migrate(@NonNull SupportSQLiteDatabase database) {
  // We can't leave the first migration empty because MonthlyRecap table 
  // and MonthlyRecap entity are not compatible
    database.execSQL(CREATE MonthlyRecap_temp …");
    
    database.execSQL("INSERT INTO MonthlyRecap_temp … FROM MonthlyRecap");
    
    database.execSQL("DROP TABLE MonthlyRecap");
    
    database.execSQL("ALTER TABLE MonthlyRecaps_new RENAME TO MonthlyRecaps");
  }
};
```

MIGRATION FROM VERSION 16 TO 17

```
private static final Migration MIGRATION_16_17 = new Migration(16, 17) {
  @Override
  public void migrate(@NonNull SupportSQLiteDatabase database) {
   // If you let this empty it will attempt to copy everything from MonthlyRecaps (newly created table) into 
  // another newly created table called MonthlyRecaps (basically creates a copy retaining the data inside the table)
  }
};
```

TESTING MIGRATIONS

```
@Rule
public MigrationTestHelper migrationTestHelper =
	New MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
        RebtelDB.class.getCanonicalName(), 
        new FrameworkSQLiteOpenHelperFactory());
       
SupportSQLiteDatabase db = migrationTestHelper.createDatabase(DATABASE_NAME, 15);
db.runMigrationsAndValidate(DATABASE_NAME, 17, MIGRATION_15_16, MIGRATION_16_17)      
```
```
@RunWith(AndroidJUnit4.class)
public class Migration15To17Test {

	private static final String TEST_REBTEL_DB = "test.rebtel.db";

	private SQLiteTestOpenHelper helper;

	@Rule
	public MigrationTestHelper migrationTestHelper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(), RebtelDB.class.getCanonicalName());

	@Before
	public void setUp() throws Exception {
		// Create the database with version 15 (original SQLite version)
		migrationTestHelper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(), RebtelDB.class.getCanonicalName());
		helper = new SQLiteTestOpenHelper(InstrumentationRegistry.getTargetContext(), TEST_REBTEL_DB);
		// Create table for each test to ensure consistency
		SQLiteTestHelper.createTable(helper);
	}

	@After
	public void tearDown() throws Exception {
		SQLiteTestHelper.clearDatabase(helper);
	}

	@Test
	public void migrationFrom15To17_containsCorrectData() throws IOException {
		insertMonthlyRecap();
		migrationTestHelper.runMigrationsAndValidate(TEST_REBTEL_DB, 17, true, MIGRATION_15_16, MIGRATION_16_17);
	}


	private static final Migration MIGRATION_15_16 = new Migration(15, 16) {
		@Override
		public void migrate(@NonNull SupportSQLiteDatabase database) {
			database.execSQL("CREATE TABLE MonthlyRecaps_new (_id integer not null primary key autoincrement, " +
					"date text, " +
					"monthAndYear text, " +
					"totalCallsCount integer not null," +
					"calledNumbersCount integer not null, " +
					"destinationCountriesCount integer not null," + "" +
					"contactsCalled text)");


			database.execSQL("INSERT INTO MonthlyRecaps_new (_id, date, monthAndYear, totalCallsCount, calledNumbersCount, destinationCountriesCount, contactsCalled) " +
					"SELECT _id, date, monthAndYear, totalCallsCount, calledNumbersCount, destinationCountriesCount, contactsCalled FROM MonthlyRecap");

			database.execSQL("DROP TABLE MonthlyRecap");

			database.execSQL("ALTER TABLE MonthlyRecaps_new RENAME TO MonthlyRecaps");
		}
	};

	private static final Migration MIGRATION_16_17 = new Migration(16, 17) {
		@Override
		public void migrate(@NonNull SupportSQLiteDatabase database) {

		}
	};

	private void insertMonthlyRecap() {
		//Create the database with the initial version 1 schema and insert a monthly recap
		MonthlyRecap monthlyRecap = new MonthlyRecap();

		monthlyRecap.setDate("2017-06-01");
		monthlyRecap.setCalledNumberCount(20);
		monthlyRecap.setTotalCallCount(36);
		monthlyRecap.setDestinationCountryCount(2);

		List<Contact> calledContacts = new ArrayList<>();
		calledContacts.add(new Contact("+46702691245", 35, 4353));
		calledContacts.add(new Contact("+46702691245", 35, 4353));
		calledContacts.add(new Contact("+46702691245", 35, 4353));
		calledContacts.add(new Contact("+46702691245", 35, 4353));
		monthlyRecap.setCalls(calledContacts);

		SQLiteTestHelper.insertMonthlyRecap(monthlyRecap, helper);
	}

}
```
