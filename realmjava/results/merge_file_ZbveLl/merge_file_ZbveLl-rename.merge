/*
 * Copyright 2016 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.realm;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import io.realm.internal.InvalidRow;
import io.realm.internal.PendingRow;
import io.realm.internal.Row;
import io.realm.internal.UncheckedRow;

/**
 * This implements {@code RealmObjectProxy} interface, to eliminate copying logic between
 * {@link RealmObject} and {@link DynamicRealmObject}.
 */
public final class ProxyState<E extends RealmModel> implements PendingRow.FrontEnd {

    private E model;

    // true only while executing the constructor of the enclosing proxy object
    private boolean underConstruction = true;

    private Row row;

    private BaseRealm realm;

    private boolean acceptDefaultValue;

    private List<String> excludeFields;

    private final List<RealmChangeListener<E>> listeners = new CopyOnWriteArrayList<RealmChangeListener<E>>();

    protected long currentTableVersion = -1;

    public ProxyState() {
    }

    public ProxyState(E model) {
        this.model = model;
    }

    /**
     * Sets the Future instance returned by the worker thread, we need this instance to force {@link RealmObject#load()} an async
     * query, we use it to determine if the current RealmResults is a sync or async one.
     *
     * @param pendingQuery pending query.
     */
    public void setPendingQuery$realm(Future<Long> pendingQuery) {
        this.pendingQuery = pendingQuery;
        if (isLoaded()) {
            // The query completed before RealmQuery
            // had a chance to call setPendingQuery to register the pendingQuery (used btw
            // to determine isLoaded behaviour).
            onCompleted$realm();
        }
    // Else, it will be handled by the Realm#handler.
    }

    public BaseRealm getRealm$realm() {
        return realm;
    }

    public void setRealm$realm(BaseRealm realm) {
        this.realm = realm;
    }

    public Row getRow$realm() {
        return row;
    }

    public void setRow$realm(Row row) {
        this.row = row;
    }

    public boolean getAcceptDefaultValue$realm() {
        return acceptDefaultValue;
    }

    public void setAcceptDefaultValue$realm(boolean acceptDefaultValue) {
        this.acceptDefaultValue = acceptDefaultValue;
    }

    public List<String> getExcludeFields$realm() {
        return excludeFields;
    }

    public void setExcludeFields$realm(List<String> excludeFields) {
        this.excludeFields = excludeFields;
    }

    /**
     * Notifies all registered listeners.
     */
    private void notifyChangeListeners() {
        if (!listeners.isEmpty()) {
            for (RealmChangeListener<E> listener : listeners) {
                if (realm.sharedRealm == null || realm.sharedRealm.isClosed()) {
                    return;
                }
                listener.onChange(model);
            }
        }
    }

    public void addChangeListener(RealmChangeListener<E> listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
        // this might be called after query returns. So it is still necessary to register.
        if (row instanceof UncheckedRow) {
            registerToRealmNotifier();
        }
    }

    public void removeChangeListener(RealmChangeListener<E> listener) {
        listeners.remove(listener);
        if (listeners.isEmpty() && row instanceof UncheckedRow) {
            realm.sharedRealm.realmNotifier.removeChangeListeners(this);
        }
    }

    public void removeAllChangeListeners() {
        listeners.clear();
        if (row instanceof UncheckedRow) {
            realm.sharedRealm.realmNotifier.removeChangeListeners(this);
        }
    }

    /**
     * Called to import the handover row pointer and notify listeners.
     *
     * @return {@code true} if it successfully completed the query, {@code false} otherwise.
     */
    public boolean onCompleted$realm() {
        try {
            // make the query blocking
            Long handoverResult = pendingQuery.get();
            if (handoverResult != 0) {
                // This may fail with BadVersionException if the caller and/or the worker thread
                // are not in sync (same shared_group version).
                // COMPLETED_ASYNC_REALM_OBJECT will be fired by the worker thread.
                // This should handle more complex use cases like retry, ignore etc.
                onCompleted$realm(handoverResult);
                notifyChangeListeners$realm();
            } else {
                isCompleted = true;
            }
        } catch (Exception e) {
            RealmLog.debug(e);
            return false;
        }
        return true;
    }

    public void onCompleted$realm(long handoverRowPointer) {
        if (handoverRowPointer == 0) {
            // We'll retry later to update the row pointer, but we consider
            // the query done.
            isCompleted = true;
        } else if (!isCompleted || row == Row.EMPTY_ROW) {
            isCompleted = true;
            long nativeRowPointer = TableQuery.importHandoverRow(handoverRowPointer, realm.sharedRealm);
            Table table = getTable();
            this.row = table.getUncheckedRowByPointer(nativeRowPointer);
        }
    // else: Already loaded query no need to import again the pointer.
    }

    public void setTableVersion$realm() {
        if (row.getTable() != null) {
            currentTableVersion = row.getTable().getVersion();
        }
    }

    public boolean isUnderConstruction() {
        return underConstruction;
    }

    public void setConstructionFinished() {
        underConstruction = false;
        // Only used while construction.
        excludeFields = null;
    }

    private void registerToRealmNotifier() {
        if (realm.sharedRealm == null || realm.sharedRealm.isClosed()) {
            return;
        }
        realm.sharedRealm.realmNotifier.addChangeListener(this, new RealmChangeListener<ProxyState<E>>() {

            @Override
            public void onChange(ProxyState<E> element) {
                long tableVersion = -1;
                if (row.isAttached()) {
                    // If the Row gets detached, table version will be -1 and it is different from current value.
                    tableVersion = row.getTable().getVersion();
                }
                if (currentTableVersion != tableVersion) {
                    currentTableVersion = tableVersion;
                    notifyChangeListeners();
                }
            }
        });
    }

    public boolean isLoaded() {
        return !(row instanceof PendingRow);
    }

    public void load() {
        if (row instanceof PendingRow) {
            row = ((PendingRow) row).executeQuery();
            if (!(row instanceof InvalidRow)) {
                registerToRealmNotifier();
            }
            notifyChangeListeners();
        }
    }

    @Override
    public void onQueryFinished(Row row) {
        this.row = row;
        // getTable should return a non-null table since the row should always be valid here.
        currentTableVersion = row.getTable().getVersion();
        notifyChangeListeners();
        registerToRealmNotifier();
    }
}
