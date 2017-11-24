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



