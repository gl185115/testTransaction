package ncr.res.mobilepos.mastersync.model;

/**
 * �z�M�t�@�C���o�[�W�����`�F�b�N���ʂ̒�`
 */
public enum DataFileVersionMatchingResult {
    /**
     * �z�M�t�@�C���̃o�[�W��������v
     */
    DATA_FILE_VERSION_MATCH,
    /**
     * �z�M�t�@�C���̃o�[�W�������s��v
     */
    DATA_FILE_VERSION_NOT_MATCH,
    /**
     * �T�[�o���̔z�M�t�@�C�����̎擾�Ɏ��s
     */
    SERVER_DATA_FILE_GET_FAILED,
    /**
     * �z�M�t�@�C���̐����s��v
     */
    DATA_FILE_COUNT_NOT_MATCH
}
