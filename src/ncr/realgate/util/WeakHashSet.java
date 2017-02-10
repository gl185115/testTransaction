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
 * �n�b�V���e�[�u���Ɋ�Â����u��L�[�v�ɂ�� Set �����B<br>
 * Cleaner�N���X���Z�b�g���邱�ƂŁA�Q�Ƃ��O�ꂽ�I�u�W�F�N�g��
 * �򉻏�����g�ݍ��ނ��Ƃ��ł���B
 *
 */
public class WeakHashSet extends AbstractSet {
    
    static final Object PRESENT = new Object();
    
    Map map;
    ReferenceQueue que;
    GarbageListener listener;
    
    /**
     * �I�u�W�F�N�g�K�x�[�W���ɌĂ΂�郊�X�i�[��K�v�ɉ����Đݒ肷��B
     * @param cl GarbageListener�N���X�B
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
     * Reference�L���[��poll���ĎQ�Ƃ��O�ꂽ�I�u�W�F�N�g���擾����B
     * GarbageListener���ݒ肳��Ă���΁A�I�u�W�F�N�g��Listener�ɒʒm����B
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
     * �Q�Ƃ��O�ꂽ�I�u�W�F�N�g�����������ꍇ�ɃR�[������郊�X�i�[�B
     */
    public static interface GarbageListener  {
        /**
         * �I�u�W�F�N�g���K�x�[�W�����ۂɌĂ΂��C�x���g���\�b�h�B
         * @param obj �K�x�[�W�ΏۃI�u�W�F�N�g�B
         */
        void garbage(Object obj);
    }

}
