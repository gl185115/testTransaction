package ncr.res.mobilepos.authentication.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * class used for corpstore details.
 * @author AP185142
 *
 */
@XmlRootElement(name = "ViewCorpStore")
public class ViewCorpStore extends ResultBase {

    /**
     * the corp store to view.
     */
    private CorpStore corpstore;

    /**
     * sets the viewed corp store.
     * @param corpstoreToSet the corp store to view
     */
    public final void setCorpstore(final CorpStore corpstoreToSet) {
        this.corpstore = corpstoreToSet;
    }

    /**
     * gets the corp store.
     * @return Store
     */
    @XmlElement(name = "Store")
    public final CorpStore getCorpstore() {
        return corpstore;
    }
}
