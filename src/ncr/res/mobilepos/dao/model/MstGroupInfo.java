package ncr.res.mobilepos.dao.model;

public class MstGroupInfo {
    private String companyId;
    private String storeId;
    private String groupId;
    private String groupName;
    private String groupKanaName;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupKanaName() {
        return groupKanaName;
    }

    public void setGroupKanaName(String groupKanaName) {
        this.groupKanaName = groupKanaName;
    }
}
