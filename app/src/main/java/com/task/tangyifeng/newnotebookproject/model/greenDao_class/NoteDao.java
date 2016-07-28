package com.task.tangyifeng.newnotebookproject.model.greenDao_class;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NOTE".
*/
public class NoteDao extends AbstractDao<Note, Long> {

    public static final String TABLENAME = "NOTE";

    /**
     * Properties of entity Note.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Html = new Property(2, String.class, "html", false, "HTML");
        public final static Property NotebookName = new Property(3, String.class, "notebookName", false, "NOTEBOOK_NAME");
        public final static Property Account = new Property(4, String.class, "account", false, "ACCOUNT");
        public final static Property IsSync = new Property(5, boolean.class, "isSync", false, "IS_SYNC");
        public final static Property IsAlarm = new Property(6, boolean.class, "isAlarm", false, "IS_ALARM");
        public final static Property IsDel = new Property(7, Boolean.class, "isDel", false, "IS_DEL");
    };


    public NoteDao(DaoConfig config) {
        super(config);
    }
    
    public NoteDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NOTE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"TITLE\" TEXT," + // 1: title
                "\"HTML\" TEXT," + // 2: html
                "\"NOTEBOOK_NAME\" TEXT," + // 3: notebookName
                "\"ACCOUNT\" TEXT," + // 4: account
                "\"IS_SYNC\" INTEGER NOT NULL ," + // 5: isSync
                "\"IS_ALARM\" INTEGER NOT NULL ," + // 6: isAlarm
                "\"IS_DEL\" INTEGER);"); // 7: isDel
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NOTE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Note entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String html = entity.getHtml();
        if (html != null) {
            stmt.bindString(3, html);
        }
 
        String notebookName = entity.getNotebookName();
        if (notebookName != null) {
            stmt.bindString(4, notebookName);
        }
 
        String account = entity.getAccount();
        if (account != null) {
            stmt.bindString(5, account);
        }
        stmt.bindLong(6, entity.getIsSync() ? 1L: 0L);
        stmt.bindLong(7, entity.getIsAlarm() ? 1L: 0L);
 
        Boolean isDel = entity.getIsDel();
        if (isDel != null) {
            stmt.bindLong(8, isDel ? 1L: 0L);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Note entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String html = entity.getHtml();
        if (html != null) {
            stmt.bindString(3, html);
        }
 
        String notebookName = entity.getNotebookName();
        if (notebookName != null) {
            stmt.bindString(4, notebookName);
        }
 
        String account = entity.getAccount();
        if (account != null) {
            stmt.bindString(5, account);
        }
        stmt.bindLong(6, entity.getIsSync() ? 1L: 0L);
        stmt.bindLong(7, entity.getIsAlarm() ? 1L: 0L);
 
        Boolean isDel = entity.getIsDel();
        if (isDel != null) {
            stmt.bindLong(8, isDel ? 1L: 0L);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Note readEntity(Cursor cursor, int offset) {
        Note entity = new Note( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // html
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // notebookName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // account
            cursor.getShort(offset + 5) != 0, // isSync
            cursor.getShort(offset + 6) != 0, // isAlarm
            cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0 // isDel
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Note entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setHtml(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNotebookName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAccount(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setIsSync(cursor.getShort(offset + 5) != 0);
        entity.setIsAlarm(cursor.getShort(offset + 6) != 0);
        entity.setIsDel(cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Note entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Note entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}