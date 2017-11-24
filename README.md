# RoomDB

Database

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

Entity

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

Database Access Object

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
