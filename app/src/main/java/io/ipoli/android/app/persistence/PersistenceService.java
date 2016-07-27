package io.ipoli.android.app.persistence;

import java.util.List;

import io.ipoli.android.quest.persistence.OnDatabaseChangedListener;

/**
 * Created by Venelin Valkov <venelin@curiousily.com>
 * on 3/30/16.
 */
public interface PersistenceService<T extends PersistedObject> {

    void save(T obj);

    void save(List<T> objects);

    void findById(String id, OnDatabaseChangedListener<T> listener);

    void delete(List<T> objects);

    void removeAllListeners();
}
