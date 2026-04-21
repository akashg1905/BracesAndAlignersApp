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
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.bracesaligner.core.database.entity.AlignerPlanEntity;
import com.example.bracesaligner.core.database.entity.AlignerScheduleItemEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
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
public final class AlignerPlanDao_Impl implements AlignerPlanDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AlignerPlanEntity> __insertionAdapterOfAlignerPlanEntity;

  private final EntityInsertionAdapter<AlignerScheduleItemEntity> __insertionAdapterOfAlignerScheduleItemEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearSchedule;

  public AlignerPlanDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAlignerPlanEntity = new EntityInsertionAdapter<AlignerPlanEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `aligner_plan` (`planId`,`userId`,`alignerCount`,`daysPerAligner`,`startDateEpochDay`,`createdAtEpochMillis`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AlignerPlanEntity entity) {
        if (entity.getPlanId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getPlanId());
        }
        if (entity.getUserId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getUserId());
        }
        statement.bindLong(3, entity.getAlignerCount());
        statement.bindLong(4, entity.getDaysPerAligner());
        statement.bindLong(5, entity.getStartDateEpochDay());
        statement.bindLong(6, entity.getCreatedAtEpochMillis());
      }
    };
    this.__insertionAdapterOfAlignerScheduleItemEntity = new EntityInsertionAdapter<AlignerScheduleItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `aligner_schedule` (`id`,`planId`,`alignerNumber`,`startEpochDay`,`endEpochDay`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AlignerScheduleItemEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getPlanId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPlanId());
        }
        statement.bindLong(3, entity.getAlignerNumber());
        statement.bindLong(4, entity.getStartEpochDay());
        statement.bindLong(5, entity.getEndEpochDay());
      }
    };
    this.__preparedStmtOfClearSchedule = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM aligner_schedule WHERE planId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsertPlan(final AlignerPlanEntity plan, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAlignerPlanEntity.insert(plan);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object insertSchedule(final List<AlignerScheduleItemEntity> items,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAlignerScheduleItemEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object clearSchedule(final String planId, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearSchedule.acquire();
        int _argIndex = 1;
        if (planId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, planId);
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
          __preparedStmtOfClearSchedule.release(_stmt);
        }
      }
    }, arg1);
  }

  @Override
  public Flow<AlignerPlanEntity> observePlan() {
    final String _sql = "SELECT * FROM aligner_plan LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"aligner_plan"}, new Callable<AlignerPlanEntity>() {
      @Override
      @Nullable
      public AlignerPlanEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "planId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfAlignerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "alignerCount");
          final int _cursorIndexOfDaysPerAligner = CursorUtil.getColumnIndexOrThrow(_cursor, "daysPerAligner");
          final int _cursorIndexOfStartDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "startDateEpochDay");
          final int _cursorIndexOfCreatedAtEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochMillis");
          final AlignerPlanEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpPlanId;
            if (_cursor.isNull(_cursorIndexOfPlanId)) {
              _tmpPlanId = null;
            } else {
              _tmpPlanId = _cursor.getString(_cursorIndexOfPlanId);
            }
            final String _tmpUserId;
            if (_cursor.isNull(_cursorIndexOfUserId)) {
              _tmpUserId = null;
            } else {
              _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            }
            final int _tmpAlignerCount;
            _tmpAlignerCount = _cursor.getInt(_cursorIndexOfAlignerCount);
            final int _tmpDaysPerAligner;
            _tmpDaysPerAligner = _cursor.getInt(_cursorIndexOfDaysPerAligner);
            final long _tmpStartDateEpochDay;
            _tmpStartDateEpochDay = _cursor.getLong(_cursorIndexOfStartDateEpochDay);
            final long _tmpCreatedAtEpochMillis;
            _tmpCreatedAtEpochMillis = _cursor.getLong(_cursorIndexOfCreatedAtEpochMillis);
            _result = new AlignerPlanEntity(_tmpPlanId,_tmpUserId,_tmpAlignerCount,_tmpDaysPerAligner,_tmpStartDateEpochDay,_tmpCreatedAtEpochMillis);
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
  public Object getPlan(final Continuation<? super AlignerPlanEntity> arg0) {
    final String _sql = "SELECT * FROM aligner_plan LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AlignerPlanEntity>() {
      @Override
      @Nullable
      public AlignerPlanEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "planId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfAlignerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "alignerCount");
          final int _cursorIndexOfDaysPerAligner = CursorUtil.getColumnIndexOrThrow(_cursor, "daysPerAligner");
          final int _cursorIndexOfStartDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "startDateEpochDay");
          final int _cursorIndexOfCreatedAtEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochMillis");
          final AlignerPlanEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpPlanId;
            if (_cursor.isNull(_cursorIndexOfPlanId)) {
              _tmpPlanId = null;
            } else {
              _tmpPlanId = _cursor.getString(_cursorIndexOfPlanId);
            }
            final String _tmpUserId;
            if (_cursor.isNull(_cursorIndexOfUserId)) {
              _tmpUserId = null;
            } else {
              _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            }
            final int _tmpAlignerCount;
            _tmpAlignerCount = _cursor.getInt(_cursorIndexOfAlignerCount);
            final int _tmpDaysPerAligner;
            _tmpDaysPerAligner = _cursor.getInt(_cursorIndexOfDaysPerAligner);
            final long _tmpStartDateEpochDay;
            _tmpStartDateEpochDay = _cursor.getLong(_cursorIndexOfStartDateEpochDay);
            final long _tmpCreatedAtEpochMillis;
            _tmpCreatedAtEpochMillis = _cursor.getLong(_cursorIndexOfCreatedAtEpochMillis);
            _result = new AlignerPlanEntity(_tmpPlanId,_tmpUserId,_tmpAlignerCount,_tmpDaysPerAligner,_tmpStartDateEpochDay,_tmpCreatedAtEpochMillis);
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
  public Flow<List<AlignerScheduleItemEntity>> observeSchedule(final String planId) {
    final String _sql = "SELECT * FROM aligner_schedule WHERE planId = ? ORDER BY alignerNumber ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (planId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, planId);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"aligner_schedule"}, new Callable<List<AlignerScheduleItemEntity>>() {
      @Override
      @NonNull
      public List<AlignerScheduleItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "planId");
          final int _cursorIndexOfAlignerNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "alignerNumber");
          final int _cursorIndexOfStartEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "startEpochDay");
          final int _cursorIndexOfEndEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "endEpochDay");
          final List<AlignerScheduleItemEntity> _result = new ArrayList<AlignerScheduleItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AlignerScheduleItemEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPlanId;
            if (_cursor.isNull(_cursorIndexOfPlanId)) {
              _tmpPlanId = null;
            } else {
              _tmpPlanId = _cursor.getString(_cursorIndexOfPlanId);
            }
            final int _tmpAlignerNumber;
            _tmpAlignerNumber = _cursor.getInt(_cursorIndexOfAlignerNumber);
            final long _tmpStartEpochDay;
            _tmpStartEpochDay = _cursor.getLong(_cursorIndexOfStartEpochDay);
            final long _tmpEndEpochDay;
            _tmpEndEpochDay = _cursor.getLong(_cursorIndexOfEndEpochDay);
            _item = new AlignerScheduleItemEntity(_tmpId,_tmpPlanId,_tmpAlignerNumber,_tmpStartEpochDay,_tmpEndEpochDay);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
