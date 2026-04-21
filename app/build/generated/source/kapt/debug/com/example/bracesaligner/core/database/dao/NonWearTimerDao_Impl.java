package com.example.bracesaligner.core.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.bracesaligner.core.database.entity.DailyNonWearSummaryEntity;
import com.example.bracesaligner.core.database.entity.NonWearSessionEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NonWearTimerDao_Impl implements NonWearTimerDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<NonWearSessionEntity> __insertionAdapterOfNonWearSessionEntity;

  private final EntityInsertionAdapter<DailyNonWearSummaryEntity> __insertionAdapterOfDailyNonWearSummaryEntity;

  private final SharedSQLiteStatement __preparedStmtOfStopSession;

  public NonWearTimerDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNonWearSessionEntity = new EntityInsertionAdapter<NonWearSessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `non_wear_session` (`sessionId`,`alignerNumber`,`startEpochMillis`,`endEpochMillis`,`dateEpochDay`,`synced`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NonWearSessionEntity entity) {
        if (entity.getSessionId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getSessionId());
        }
        statement.bindLong(2, entity.getAlignerNumber());
        statement.bindLong(3, entity.getStartEpochMillis());
        if (entity.getEndEpochMillis() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getEndEpochMillis());
        }
        statement.bindLong(5, entity.getDateEpochDay());
        final int _tmp = entity.getSynced() ? 1 : 0;
        statement.bindLong(6, _tmp);
      }
    };
    this.__insertionAdapterOfDailyNonWearSummaryEntity = new EntityInsertionAdapter<DailyNonWearSummaryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `daily_non_wear_summary` (`dateEpochDay`,`totalMinutes`,`warningSent`,`exceededSent`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DailyNonWearSummaryEntity entity) {
        statement.bindLong(1, entity.getDateEpochDay());
        statement.bindLong(2, entity.getTotalMinutes());
        final int _tmp = entity.getWarningSent() ? 1 : 0;
        statement.bindLong(3, _tmp);
        final int _tmp_1 = entity.getExceededSent() ? 1 : 0;
        statement.bindLong(4, _tmp_1);
      }
    };
    this.__preparedStmtOfStopSession = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE non_wear_session SET endEpochMillis = ? WHERE sessionId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsertSession(final NonWearSessionEntity session,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfNonWearSessionEntity.insert(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object upsertDailySummary(final DailyNonWearSummaryEntity summary,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDailyNonWearSummaryEntity.insert(summary);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object stopSession(final String sessionId, final long endMillis,
      final Continuation<? super Unit> arg2) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfStopSession.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, endMillis);
        _argIndex = 2;
        if (sessionId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, sessionId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfStopSession.release(_stmt);
        }
      }
    }, arg2);
  }

  @Override
  public Flow<NonWearSessionEntity> observeActiveSession() {
    final String _sql = "SELECT * FROM non_wear_session WHERE endEpochMillis IS NULL LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"non_wear_session"}, new Callable<NonWearSessionEntity>() {
      @Override
      @Nullable
      public NonWearSessionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfAlignerNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "alignerNumber");
          final int _cursorIndexOfStartEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "startEpochMillis");
          final int _cursorIndexOfEndEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "endEpochMillis");
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final NonWearSessionEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpSessionId;
            if (_cursor.isNull(_cursorIndexOfSessionId)) {
              _tmpSessionId = null;
            } else {
              _tmpSessionId = _cursor.getString(_cursorIndexOfSessionId);
            }
            final int _tmpAlignerNumber;
            _tmpAlignerNumber = _cursor.getInt(_cursorIndexOfAlignerNumber);
            final long _tmpStartEpochMillis;
            _tmpStartEpochMillis = _cursor.getLong(_cursorIndexOfStartEpochMillis);
            final Long _tmpEndEpochMillis;
            if (_cursor.isNull(_cursorIndexOfEndEpochMillis)) {
              _tmpEndEpochMillis = null;
            } else {
              _tmpEndEpochMillis = _cursor.getLong(_cursorIndexOfEndEpochMillis);
            }
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final boolean _tmpSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp != 0;
            _result = new NonWearSessionEntity(_tmpSessionId,_tmpAlignerNumber,_tmpStartEpochMillis,_tmpEndEpochMillis,_tmpDateEpochDay,_tmpSynced);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getActiveSession(final Continuation<? super NonWearSessionEntity> arg0) {
    final String _sql = "SELECT * FROM non_wear_session WHERE endEpochMillis IS NULL LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<NonWearSessionEntity>() {
      @Override
      @Nullable
      public NonWearSessionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfAlignerNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "alignerNumber");
          final int _cursorIndexOfStartEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "startEpochMillis");
          final int _cursorIndexOfEndEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "endEpochMillis");
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final NonWearSessionEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpSessionId;
            if (_cursor.isNull(_cursorIndexOfSessionId)) {
              _tmpSessionId = null;
            } else {
              _tmpSessionId = _cursor.getString(_cursorIndexOfSessionId);
            }
            final int _tmpAlignerNumber;
            _tmpAlignerNumber = _cursor.getInt(_cursorIndexOfAlignerNumber);
            final long _tmpStartEpochMillis;
            _tmpStartEpochMillis = _cursor.getLong(_cursorIndexOfStartEpochMillis);
            final Long _tmpEndEpochMillis;
            if (_cursor.isNull(_cursorIndexOfEndEpochMillis)) {
              _tmpEndEpochMillis = null;
            } else {
              _tmpEndEpochMillis = _cursor.getLong(_cursorIndexOfEndEpochMillis);
            }
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final boolean _tmpSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp != 0;
            _result = new NonWearSessionEntity(_tmpSessionId,_tmpAlignerNumber,_tmpStartEpochMillis,_tmpEndEpochMillis,_tmpDateEpochDay,_tmpSynced);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public Flow<Long> observeDayTotalMillis(final long epochDay) {
    final String _sql = "SELECT COALESCE(SUM(endEpochMillis - startEpochMillis), 0) FROM non_wear_session WHERE dateEpochDay = ? AND endEpochMillis IS NOT NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, epochDay);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"non_wear_session"}, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getDayTotalMillis(final long epochDay, final Continuation<? super Long> arg1) {
    final String _sql = "SELECT COALESCE(SUM(endEpochMillis - startEpochMillis), 0) FROM non_wear_session WHERE dateEpochDay = ? AND endEpochMillis IS NOT NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, epochDay);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @Override
  public Object getDailySummary(final long epochDay,
      final Continuation<? super DailyNonWearSummaryEntity> arg1) {
    final String _sql = "SELECT * FROM daily_non_wear_summary WHERE dateEpochDay = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, epochDay);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DailyNonWearSummaryEntity>() {
      @Override
      @Nullable
      public DailyNonWearSummaryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfTotalMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalMinutes");
          final int _cursorIndexOfWarningSent = CursorUtil.getColumnIndexOrThrow(_cursor, "warningSent");
          final int _cursorIndexOfExceededSent = CursorUtil.getColumnIndexOrThrow(_cursor, "exceededSent");
          final DailyNonWearSummaryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final int _tmpTotalMinutes;
            _tmpTotalMinutes = _cursor.getInt(_cursorIndexOfTotalMinutes);
            final boolean _tmpWarningSent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWarningSent);
            _tmpWarningSent = _tmp != 0;
            final boolean _tmpExceededSent;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfExceededSent);
            _tmpExceededSent = _tmp_1 != 0;
            _result = new DailyNonWearSummaryEntity(_tmpDateEpochDay,_tmpTotalMinutes,_tmpWarningSent,_tmpExceededSent);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @Override
  public Flow<List<DailyNonWearSummaryEntity>> observeRecentSummary(final int limit) {
    final String _sql = "SELECT * FROM daily_non_wear_summary ORDER BY dateEpochDay DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"daily_non_wear_summary"}, new Callable<List<DailyNonWearSummaryEntity>>() {
      @Override
      @NonNull
      public List<DailyNonWearSummaryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfTotalMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalMinutes");
          final int _cursorIndexOfWarningSent = CursorUtil.getColumnIndexOrThrow(_cursor, "warningSent");
          final int _cursorIndexOfExceededSent = CursorUtil.getColumnIndexOrThrow(_cursor, "exceededSent");
          final List<DailyNonWearSummaryEntity> _result = new ArrayList<DailyNonWearSummaryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailyNonWearSummaryEntity _item;
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final int _tmpTotalMinutes;
            _tmpTotalMinutes = _cursor.getInt(_cursorIndexOfTotalMinutes);
            final boolean _tmpWarningSent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWarningSent);
            _tmpWarningSent = _tmp != 0;
            final boolean _tmpExceededSent;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfExceededSent);
            _tmpExceededSent = _tmp_1 != 0;
            _item = new DailyNonWearSummaryEntity(_tmpDateEpochDay,_tmpTotalMinutes,_tmpWarningSent,_tmpExceededSent);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getUnsyncedSessions(final Continuation<? super List<NonWearSessionEntity>> arg0) {
    final String _sql = "SELECT * FROM non_wear_session WHERE synced = 0 AND endEpochMillis IS NOT NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<NonWearSessionEntity>>() {
      @Override
      @NonNull
      public List<NonWearSessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfAlignerNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "alignerNumber");
          final int _cursorIndexOfStartEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "startEpochMillis");
          final int _cursorIndexOfEndEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "endEpochMillis");
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<NonWearSessionEntity> _result = new ArrayList<NonWearSessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NonWearSessionEntity _item;
            final String _tmpSessionId;
            if (_cursor.isNull(_cursorIndexOfSessionId)) {
              _tmpSessionId = null;
            } else {
              _tmpSessionId = _cursor.getString(_cursorIndexOfSessionId);
            }
            final int _tmpAlignerNumber;
            _tmpAlignerNumber = _cursor.getInt(_cursorIndexOfAlignerNumber);
            final long _tmpStartEpochMillis;
            _tmpStartEpochMillis = _cursor.getLong(_cursorIndexOfStartEpochMillis);
            final Long _tmpEndEpochMillis;
            if (_cursor.isNull(_cursorIndexOfEndEpochMillis)) {
              _tmpEndEpochMillis = null;
            } else {
              _tmpEndEpochMillis = _cursor.getLong(_cursorIndexOfEndEpochMillis);
            }
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final boolean _tmpSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp != 0;
            _item = new NonWearSessionEntity(_tmpSessionId,_tmpAlignerNumber,_tmpStartEpochMillis,_tmpEndEpochMillis,_tmpDateEpochDay,_tmpSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public Object markAsSynced(final List<String> sessionIds, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("UPDATE non_wear_session SET synced = 1 WHERE sessionId IN (");
        final int _inputSize = sessionIds.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        for (String _item : sessionIds) {
          if (_item == null) {
            _stmt.bindNull(_argIndex);
          } else {
            _stmt.bindString(_argIndex, _item);
          }
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
