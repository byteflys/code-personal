package com.easing.commons.android.greendao.cache;

import com.easing.commons.android.cache.FileCache;
import com.easing.commons.android.cache.FileCacheDao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

public class FileCacheGreenDaoSession extends AbstractDaoSession {

    private final DaoConfig config;
    private final FileCacheDao dao;

    public FileCacheGreenDaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
        super(db);
        config = daoConfigMap.get(FileCacheDao.class).clone();
        config.initIdentityScope(type);
        dao = new FileCacheDao(config, this);
        registerDao(FileCache.class, dao);
    }

    public void clear() {
        config.clearIdentityScope();
    }

    public FileCacheDao getFileCacheDao() {
        return dao;
    }
}

