// Copyright(c) 2002 NCR Japan Ltd.
//
// $Log: $
// $Id: $
//
package ncr.realgate.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * ハッシュテーブルに基づいた「弱キー」による Set 実装。<br>
 * Cleanerクラスをセットすることで、参照が外れたオブジェクトの
 * 浄化処理を組み込むことができる。
 *
 */
public class WeakHashSet extends AbstractSet {
    
    static final Object PRESENT = new Object();
    
    Map map;
    ReferenceQueue que;
    GarbageListener listener;
    
    /**
     * オブジェクトガベージ時に呼ばれるリスナーを必要に応じて設定する。
     * @param cl GarbageListenerクラス。
     */
    public void setGarbageListener(GarbageListener  gl) {
        listener = gl;
    }

    public WeakHashSet() {
        map = newMap();
        que = newReferenceQueue();
    }
    
    Map newMap() {
        return new WeakHashMap();
    }
    Map newMap(int capacity) {
        return new WeakHashMap(capacity);
    }
    Map newMap(int i, float f) {
        return new WeakHashMap(i, f);
    }
    ReferenceQueue newReferenceQueue() {
        return new ReferenceQueue();
    }

    public WeakHashSet(Collection collection) {
        int capacity = Math.max((int) ((float) collection.size() / 0.75F) + 1, 16);
        map = newMap(capacity);
        que = newReferenceQueue();
        addAll(collection);
    }

    public WeakHashSet(int i, float f) {
        map = newMap(i, f);
        que = newReferenceQueue();
    }

    public WeakHashSet(int i) {
        map = newMap(i);
        que = newReferenceQueue();
    }

    public WeakHashSet(int i, float f, boolean flag) {
        map = newMap(i, f);
        que = newReferenceQueue();
    }

    public Iterator iterator() {
        polling();
        return map.keySet().iterator();
    }

    public int size() {
        polling();
        return map.size();
    }

    public boolean isEmpty() {
        polling();
        return map.isEmpty();
    }

    public boolean contains(Object obj) {
        polling();
        return map.containsKey(obj);
    }

    public boolean add(Object obj) {
        polling();
        new WeakReference(obj, que);
        return map.put(obj, PRESENT) == null;
    }

    public boolean remove(Object obj) {
        polling();
        return map.remove(obj) == PRESENT;
    }

    public void clear() {
        map.clear();
        polling();
    }
    
    /**
     * Referenceキューをpollして参照が外れたオブジェクトを取得する。
     * GarbageListenerが設定されていれば、オブジェクトをListenerに通知する。
     */
    void polling() {
        while (true) {
            Reference ref = que.poll();
            if (ref == null) {
                break;
            }
            Object o = ref.get();
            if (o != null && listener != null) {
                listener.garbage(o);
            }
        }
    }
    
    /**
     * 参照が外れたオブジェクトが発生した場合にコールされるリスナー。
     */
    public static interface GarbageListener  {
        /**
         * オブジェクトがガベージされる際に呼ばれるイベントメソッド。
         * @param obj ガベージ対象オブジェクト。
         */
        void garbage(Object obj);
    }

}
