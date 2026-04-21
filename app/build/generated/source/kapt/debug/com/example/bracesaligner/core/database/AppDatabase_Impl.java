package com.example.bracesaligner.core.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.bracesaligner.core.database.dao.AlignerPlanDao;
import com.example.bracesaligner.core.database.dao.AlignerPlanDao_Impl;
import com.example.bracesaligner.core.database.dao.AuthSessionDao;
import com.example.bracesaligner.core.database.dao.AuthSessionDao_Impl;
import com.example.bracesaligner.core.database.dao.NonWearTimerDao;
import com.example.bracesaligner.core.database.dao.NonWearTimerDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile AuthSessionDao _authSessionDao;

  private volatile AlignerPlanDao _alignerPlanDao;

  private volatile NonWearTimerDao _nonWearTimerDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `auth_session` (`id` INTEGER NOT NULL, `accessToken` TEXT NOT NULL, `refreshToken` TEXT, `userId` TEXT NOT NULL, `isLoggedIn` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `aligner_plan` (`planId` TEXT NOT NULL, `userId` TEXT NOT NULL, `alignerCount` INTEGER NOT NULL, `daysPerAligner` INTEGER NOT NULL, `startDateEpochDay` INTEGER NOT NULL, `createdAtEpochMillis` INTEGER NOT NULL, PRIMARY KEY(`planId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `aligner_schedule` (`id` TEXT NOT NULL, `planId` TEXT NOT NULL, `alignerNumber` INTEGER NOT NULL, `startEpochDay` INTEGER NOT NULL, `endEpochDay` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `non_wear_session` (`sessionId` TEXT NOT NULL, `alignerNumber` INTEGER NOT NULL, `startEpochMillis` INTEGER NOT NULL, `endEpochMillis` INTEGER, `dateEpochDay` INTEGER NOT NULL, `synced` INTEGER NOT NULL, PRIMARY KEY(`sessionId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `daily_non_wear_summary` (`dateEpochDay` INTEGER NOT NULL, `totalMinutes` INTEGER NOT NULL, `warningSent` INTEGER NOT NULL, `exceededSent` INTEGER NOT NULL, PRIMARY KEY(`dateEpochDay`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'ffba8cfd2bd92d521227368816c12f80')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `auth_session`");
        db.execSQL("DROP TABLE IF EXISTS `aligner_plan`");
        db.execSQL("DROP TABLE IF EXISTS `aligner_schedule`");
        db.execSQL("DROP TABLE IF EXISTS `non_wear_session`");
        db.execSQL("DROP TABLE IF EXISTS `daily_non_wear_summary`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsAuthSession = new HashMap<String, TableInfo.Column>(5);
        _columnsAuthSession.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuthSession.put("accessToken", new TableInfo.Column("accessToken", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuthSession.put("refreshToken", new TableInfo.Column("refreshToken", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuthSession.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuthSession.put("isLoggedIn", new TableInfo.Column("isLoggedIn", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAuthSession = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAuthSession = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAuthSession = new TableInfo("auth_session", _columnsAuthSession, _foreignKeysAuthSession, _indicesAuthSession);
        final TableInfo _existingAuthSession = TableInfo.read(db, "auth_session");
        if (!_infoAuthSession.equals(_existingAuthSession)) {
          return new RoomOpenHelper.ValidationResult(false, "auth_session(com.example.bracesaligner.core.database.entity.AuthSessionEntity).\n"
                  + " Expected:\n" + _infoAuthSession + "\n"
                  + " Found:\n" + _existingAuthSession);
        }
        final HashMap<String, TableInfo.Column> _columnsAlignerPlan = new HashMap<String, TableInfo.Column>(6);
        _columnsAlignerPlan.put("planId", new TableInfo.Column("planId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlignerPlan.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlignerPlan.put("alignerCount", new TableInfo.Column("alignerCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlignerPlan.put("daysPerAligner", new TableInfo.Column("daysPerAligner", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlignerPlan.put("startDateEpochDay", new TableInfo.Column("startDateEpochDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlignerPlan.put("createdAtEpochMillis", new TableInfo.Column("createdAtEpochMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAlignerPlan = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAlignerPlan = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAlignerPlan = new TableInfo("aligner_plan", _columnsAlignerPlan, _foreignKeysAlignerPlan, _indicesAlignerPlan);
        final TableInfo _existingAlignerPlan = TableInfo.read(db, "aligner_plan");
        if (!_infoAlignerPlan.equals(_existingAlignerPlan)) {
          return new RoomOpenHelper.ValidationResult(false, "aligner_plan(com.example.bracesaligner.core.database.entity.AlignerPlanEntity).\n"
                  + " Expected:\n" + _infoAlignerPlan + "\n"
                  + " Found:\n" + _existingAlignerPlan);
        }
        final HashMap<String, TableInfo.Column> _columnsAlignerSchedule = new HashMap<String, TableInfo.Column>(5);
        _columnsAlignerSchedule.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlignerSchedule.put("planId", new TableInfo.Column("planId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlignerSchedule.put("alignerNumber", new TableInfo.Column("alignerNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlignerSchedule.put("startEpochDay", new TableInfo.Column("startEpochDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlignerSchedule.put("endEpochDay", new TableInfo.Column("endEpochDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAlignerSchedule = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAlignerSchedule = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAlignerSchedule = new TableInfo("aligner_schedule", _columnsAlignerSchedule, _foreignKeysAlignerSchedule, _indicesAlignerSchedule);
        final TableInfo _existingAlignerSchedule = TableInfo.read(db, "aligner_schedule");
        if (!_infoAlignerSchedule.equals(_existingAlignerSchedule)) {
          return new RoomOpenHelper.ValidationResult(false, "aligner_schedule(com.example.bracesaligner.core.database.entity.AlignerScheduleItemEntity).\n"
                  + " Expected:\n" + _infoAlignerSchedule + "\n"
                  + " Found:\n" + _existingAlignerSchedule);
        }
        final HashMap<String, TableInfo.Column> _columnsNonWearSession = new HashMap<String, TableInfo.Column>(6);
        _columnsNonWearSession.put("sessionId", new TableInfo.Column("sessionId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNonWearSession.put("alignerNumber", new TableInfo.Column("alignerNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNonWearSession.put("startEpochMillis", new TableInfo.Column("startEpochMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNonWearSession.put("endEpochMillis", new TableInfo.Column("endEpochMillis", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNonWearSession.put("dateEpochDay", new TableInfo.Column("dateEpochDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNonWearSession.put("synced", new TableInfo.Column("synced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNonWearSession = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNonWearSession = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNonWearSession = new TableInfo("non_wear_session", _columnsNonWearSession, _foreignKeysNonWearSession, _indicesNonWearSession);
        final TableInfo _existingNonWearSession = TableInfo.read(db, "non_wear_session");
        if (!_infoNonWearSession.equals(_existingNonWearSession)) {
          return new RoomOpenHelper.ValidationResult(false, "non_wear_session(com.example.bracesaligner.core.database.entity.NonWearSessionEntity).\n"
                  + " Expected:\n" + _infoNonWearSession + "\n"
                  + " Found:\n" + _existingNonWearSession);
        }
        final HashMap<String, TableInfo.Column> _columnsDailyNonWearSummary = new HashMap<String, TableInfo.Column>(4);
        _columnsDailyNonWearSummary.put("dateEpochDay", new TableInfo.Column("dateEpochDay", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyNonWearSummary.put("totalMinutes", new TableInfo.Column("totalMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyNonWearSummary.put("warningSent", new TableInfo.Column("warningSent", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyNonWearSummary.put("exceededSent", new TableInfo.Column("exceededSent", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDailyNonWearSummary = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDailyNonWearSummary = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDailyNonWearSummary = new TableInfo("daily_non_wear_summary", _columnsDailyNonWearSummary, _foreignKeysDailyNonWearSummary, _indicesDailyNonWearSummary);
        final TableInfo _existingDailyNonWearSummary = TableInfo.read(db, "daily_non_wear_summary");
        if (!_infoDailyNonWearSummary.equals(_existingDailyNonWearSummary)) {
          return new RoomOpenHelper.ValidationResult(false, "daily_non_wear_summary(com.example.bracesaligner.core.database.entity.DailyNonWearSummaryEntity).\n"
                  + " Expected:\n" + _infoDailyNonWearSummary + "\n"
                  + " Found:\n" + _existingDailyNonWearSummary);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "ffba8cfd2bd92d521227368816c12f80", "f1af7e6b29553a254a9504e98b945955");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "auth_session","aligner_plan","aligner_schedule","non_wear_session","daily_non_wear_summary");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `auth_session`");
      _db.execSQL("DELETE FROM `aligner_plan`");
      _db.execSQL("DELETE FROM `aligner_schedule`");
      _db.execSQL("DELETE FROM `non_wear_session`");
      _db.execSQL("DELETE FROM `daily_non_wear_summary`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AuthSessionDao.class, AuthSessionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AlignerPlanDao.class, AlignerPlanDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(NonWearTimerDao.class, NonWearTimerDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public AuthSessionDao authSessionDao() {
    if (_authSessionDao != null) {
      return _authSessionDao;
    } else {
      synchronized(this) {
        if(_authSessionDao == null) {
          _authSessionDao = new AuthSessionDao_Impl(this);
        }
        return _authSessionDao;
      }
    }
  }

  @Override
  public AlignerPlanDao alignerPlanDao() {
    if (_alignerPlanDao != null) {
      return _alignerPlanDao;
    } else {
      synchronized(this) {
        if(_alignerPlanDao == null) {
          _alignerPlanDao = new AlignerPlanDao_Impl(this);
        }
        return _alignerPlanDao;
      }
    }
  }

  @Override
  public NonWearTimerDao nonWearTimerDao() {
    if (_nonWearTimerDao != null) {
      return _nonWearTimerDao;
    } else {
      synchronized(this) {
        if(_nonWearTimerDao == null) {
          _nonWearTimerDao = new NonWearTimerDao_Impl(this);
        }
        return _nonWearTimerDao;
      }
    }
  }
}
