// Copyright(c) 2005 NCR Japan Ltd.
//
// $Log: MultiSnap.java,v $
// Revision 1.6  2013/04/02 05:04:45  ushi
// �t�@�C���|�C���^�N���[�Y�R��A���\�[�X���[�N�Ή��i�Q�Ƃ��O�ꂽ���ɃN���[���A�b�v����j�B
//
// Revision 1.5  2006/01/12 05:56:09  art
// Snap#close�̏�����MultiSnap�͖������ł���悤�ɕʃ��\�b�h�ւ̃��_�C���N�g�Ƃ����B
//
// Revision 1.4  2005/07/22 03:30:09  art
// �X���b�h����N���[�Y��ǉ�
//
// Revision 1.3  2005/07/19 03:10:48  art
// �X���b�h���̃G���R�[�h�����s����悤�ɏC��
//
// Revision 1.2  2005/07/05 07:11:11  art
// Snap�̃��\�b�h�V�O�l�`���C���ɒǐ�
//
// Revision 1.1  2005/06/10 04:56:39  art
// �}���`�X���b�h�ł̗��p��O��Ƃ��ĒP��C���X�^���X�}���`�t�@�C������������Snap
//
// $Id: MultiSnap.java,v 1.6 2013/04/02 05:04:45 ushi Exp $
//
package ncr.realgate.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;

import ncr.realgate.util.WeakHashSet.GarbageListener;

/**
 * ����C���X�^���X�ŃX���b�h�P�ʂɃt�@�C�����쐬�^�Ǘ�����Snap�B<br>
 * ���N���X�𗘗p���č쐬�����X�i�b�v�t�@�C���̖����ɂ͋�؂蕶���Ƃ���#�ɑ����ăX���b�hID���t�������B
 * �X���b�hID�Ƃ��ẮAThread#getName���\�b�h�̌��ʂ𗘗p���邪�Anull�̏ꍇ�ɂ�4���̃����_���Ȑ�����
 * �ݒ肷��B
 *
 * @version $Revision: 1.6 $ $Date: 2013/04/02 05:04:45 $
 */
public class MultiSnap extends Snap {

    Set allSnaps = newHashSet();
    ThreadLocal snapHolder = new ThreadLocal();
    
    WeakHashSet newHashSet() {
        WeakHashSet set = new WeakHashSet();
        set.setGarbageListener(new GarbageListener () {
            public void garbage(Object obj) {
                if (obj != null && obj instanceof SnapFile) {
                    SnapFile snpf = (SnapFile) obj;
                    snpf.close();
                }
            }
        });
        return set;
    }

    /**
     * ��̃C���X�^���X�𐶐�����B<br>
     * �ŏ��̏������ݑO��{@link #setPathName}���\�b�h���Ăяo���ăX�i�b�v�t�@�C����
     * �ݒ肵�Ȃ���΂Ȃ�Ȃ��B
     */
    public MultiSnap() {
    }

    /**
     * �w�肵���p�X���ɃX���b�hID��t�������X�i�b�v�t�@�C�����쐬����B���ɊY���t�@�C�������݂���ꍇ�́A
     * �ǋL���[�h�ŃI�[�v������B<br>
     * �X�i�b�v�t�@�C���̃}�[�N�ł���t�@�C�����擪��'S'�͌Ăяo�����ł��炩����
     * �ݒ肵�Ă����K�v������B
     * @param pathName �X�i�b�v���쐬����p�X��
     */
    public MultiSnap(CharSequence pathName) {
        super();
        setPathName(pathName);
    }

    /**
     * �w�肵���p�X���ɃX���b�hID��t�������X�i�b�v�t�@�C�����쐬����B���ɊY���t�@�C�������݂���ꍇ�́A
     * �ǋL���[�h�ŃI�[�v������B�t�@�C�����擪��'S'�͓��R���X�g���N�^���Ő�������B
     * @param path �X�i�b�v���쐬����f�B���N�g����(����'/'�͕s�v)
     * @param name 12�����ȓ��Ńt�@�C�������w�肷��B�����������\�b�h�Œ����̃`�F�b�N�͍s��Ȃ��B
     */
    public MultiSnap(CharSequence path, CharSequence name) {
        this(createPathName(path, name));
    }

    class SnapFile {
        String name;
        FileOutputStream out;
        SnapFile(FileOutputStream o, CharSequence s) {
            out = o;
            name = s.toString();
            synchronized (allSnaps) {
                allSnaps.add(this);
            }
            snapHolder.set(this);
        }
        void close() {
            try {
                out.close();
                File f = new File(name);
                if (f.exists() && f.length() == 0) {
                    f.delete();
                }
            } catch (IOException e) {
                // �X�i�b�v�̏������݂��G���[�ɂȂ����ꍇ�̓X�i�b�v�����Ȃ��̂ŉ������Ȃ��B
            }
            // close���Ăяo������Iterator�𗘗p���Ĉꊇclose���s�����߁AHashSet�ɑ΂���
            // ����͍s��Ȃ��B
            snapHolder.set(null);
        }
        public int hashCode() {
            return name.hashCode();
        }
        public boolean equals(Object o) {
            if (o instanceof SnapFile) {
                return ((SnapFile)o).name.equals(name);
            }
            return false;
        }
    }

    protected OutputStream getOut() {
        if (getInternalPathName() == null) {
            return null;
        }
        SnapFile sf = (SnapFile)snapHolder.get();
        if (sf == null) {
            setPathName(getInternalPathName());
            sf = (SnapFile)snapHolder.get();
        }
        return sf.out;
    }

    protected void setOut(OutputStream o, CharSequence s) {
        SnapFile sf = (SnapFile)snapHolder.get();
        if (sf != null) {
            sf.close();
            synchronized (allSnaps) {
                allSnaps.remove(sf);
            }
        }
        if (o != null) {
            assert s != null : "no name, but valid stream";
            newSnapFile((FileOutputStream)o, s);
        }
    }
    
    protected SnapFile newSnapFile(FileOutputStream fs, CharSequence s) {
        return new SnapFile(fs, s);
    }

    protected void internalClose() {
        // no-op
    }

    /**
     * ���݃I�[�v������Ă��邷�ׂĂ�Snap�t�@�C�����N���[�Y����B
     */
    public void close() {
        synchronized (allSnaps) {
            for (Iterator i = allSnaps.iterator(); i.hasNext();) {
                SnapFile sf = (SnapFile)i.next();
                sf.close();
            }
            allSnaps.clear();
        }
    }

    /**
     * ���̃X���b�h�����p���Ă���Snap�t�@�C���݂̂��N���[�Y����B
     */
    public void closeThis() {
        super.close();
    }

    /**
     * ���̃X���b�h���g�p����X�i�b�v�̃t���p�X����ԑ�����B
     * @return �X�i�b�v�t�@�C���̃t���p�X��
     */
    public String getPathName() {
        SnapFile sf = (SnapFile)snapHolder.get();
        if (sf != null) {
            return sf.name;
        }
        return "";
    }

    public void setPathName(CharSequence newPathName) {
        setInternalPathName(newPathName);
        super.setPathName(newPathName + "#" + UniqueName.newThreadName());
    }

}

