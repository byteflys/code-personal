package com.easing.commons.android.greendao.journal;

import com.easing.commons.android.journal.Journal;
import com.easing.commons.android.journal.JournalDao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

public class JournalGreenDaoSession extends AbstractDaoSession {

    private final DaoConfig journalDaoConfig;
    private final JournalDao journalDao;

    public JournalGreenDaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
        super(db);

        journalDaoConfig = daoConfigMap.get(JournalDao.class).clone();
        journalDaoConfig.initIdentityScope(type);
        journalDao = new JournalDao(journalDaoConfig, this);
        registerDao(Journal.class, journalDao);
    }

    public void clear() {
        journalDaoConfig.clearIdentityScope();
    }

    public JournalDao getJournalDao() {
        return journalDao;
    }

}
