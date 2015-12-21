package ncr.res.mobilepos.creditauthorization.helper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.crypto.spec.SecretKeySpec;

import ncr.res.mobilepos.exception.PrivateKeyException;
import ncr.res.mobilepos.helper.Logger;

/**
 * The Class PrivateKey.
 */
public class PrivateKey {
	
	/**
     * logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /** The key store. */
    private KeyStore keyStore = null;

    /** The key store path. */
    private String keyStorePath = "";

    /** The key password. */
    private String keyPass = "";

    /** The key algorithm. */
    private static final String KEY_ALGO = "AES";

    /** The keystore type. */
    private static final String KEYSTORE_TYPE = "JCEKS";

    /** The master key. */
    private static final String AESKEY = "AESKey";

    /** The master key value. */
    private static final String AESKEY_VALUE = "ExampleRES30";
    
    private static final String PROG_NAME = "PrivateKey";

    /**
     * * Constructor.
     *
     * @param ksPath
     *            the key store path
     * @param ksPassword
     *            the key store password
     * @throws PrivateKeyException
     *             thrown when process fails.
     */
    public PrivateKey(final String ksPath, final String ksPassword)
             throws PrivateKeyException{
    	
    	
        java.io.FileInputStream fis = null;

        try {
            this.setKeyStorePath(ksPath);
            this.setKeyPass(ksPassword);

            char[] password = ksPassword.toCharArray();

            keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
            fis = new java.io.FileInputStream(ksPath);
            keyStore.load(fis, password);

            // overwrite key value of AESKey to "EXAMPLERES30"
            overrideAESKey();

        } catch (Exception e) {
            throw new PrivateKeyException(e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                	this.LOGGER.logAlert(PROG_NAME, "PrivateKey.PrivateKey", 
                			Logger.RES_EXCEP_IO, 
                			"IOException: Error in closing FileInputStream "
                			+ "object.\n" + e.getMessage());
                }
            }
        }
    }

    /**
     * get AesKey's key value.
     *
     * @return the aes key
     */
    public final String getAesKey() {
        return getKey(AESKEY);
    }

    /**
     * set AesKey's key value.
     *
     * @param value
     *            the new aes key
     */
    public final void setAesKey(final String value) {
        setKey(AESKEY, value);
    }

    /**
     * Override AesKey's keyvalue.
     */
    private void overrideAESKey() {
        if (getKey(AESKEY).isEmpty()) {
            setKey(AESKEY, AESKEY_VALUE);
        }
    }

    /**
     * Gets the key value of given alias or key.
     *
     * @param alias
     *            the alias
     * @return the key
     */
    public final String getKey(final String alias) {
        String secretKey = "";
        try {
            Key key = keyStore.getKey(alias, keyPass.toCharArray());
            secretKey = new String(key.getEncoded());
        } catch (Exception e) {
        	
			LOGGER.logAlert(PROG_NAME,
                    "PrivateKey.getKey",
                    Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
        }
        return secretKey;
    }

    /**
     * Sets the keyValue of given alias or key.
     *
     * @param alias
     *            the alias
     * @param key
     *            the key
     */
    public final void setKey(final String alias, final String key) {
        FileOutputStream fos = null;
        String functionName = "PrivateKey.setKey";
		try {
            byte[] raw = key.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGO);
            KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(
                    skeySpec);
            keyStore.setEntry(alias, skEntry, new KeyStore.PasswordProtection(
                    keyPass.toCharArray()));

            fos = new FileOutputStream(getKeyStorePath());
            keyStore.store(fos, keyPass.toCharArray());
            fos.close();
        } catch (Exception e) {
        	LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (final IOException e) {
                	LOGGER.logAlert(PROG_NAME,
                            functionName,
                            Logger.RES_EXCEP_GENERAL,
                            e.getMessage());
                }
            }
        }
    }

    /**
     * Deletes key of given alias or key.
     *
     * @param alias
     *            the alias
     * @return true, if successful
     */
    public final boolean deleteKey(final String alias) {
        boolean isDeleted = false;
        FileOutputStream fos = null;
        String functionName = "PrivateKey.deleteKey";
		try {
            keyStore.deleteEntry(alias);
            fos = new FileOutputStream(getKeyStorePath());
            keyStore.store(fos, keyPass.toCharArray());
            isDeleted = true;
        } catch (KeyStoreException e) {
        	LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
        } catch (NoSuchAlgorithmException e) {
        	LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
        } catch (CertificateException e) {
        	LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
        } catch (FileNotFoundException e) {
        	LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
        } catch (IOException e) {
        	LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (final IOException e) {
                	LOGGER.logAlert(PROG_NAME,
                            functionName,
                            Logger.RES_EXCEP_GENERAL,
                            e.getMessage());
                }
            }
        }
        return isDeleted;
    }

    /**
     * Gets the keystore.
     *
     * @return the key store
     */
    public final KeyStore getKeyStore() {
        return keyStore;
    }

    /**
     * Sets the keystore.
     *
     * @param ks
     *            the new key store
     */
    public final void setKeyStore(final KeyStore ks) {
        this.keyStore = ks;
    }

    /**
     * Gets the keystore's password.
     *
     * @return the key pass
     */
    public final String getKeyPass() {
        return keyPass;
    }

    /**
     * Sets the keystore's password.
     *
     * @param ksPassword
     *            the new key pass
     */
    public final void setKeyPass(final String ksPassword) {
        this.keyPass = ksPassword;
    }

    /**
     * Sets the keystore's filepath.
     *
     * @param ksPath
     *            the new key store path
     */
    private void setKeyStorePath(final String ksPath) {
        this.keyStorePath = ksPath;
    }

    /**
     * Gets the keystore's filepath.
     *
     * @return filepath
     */
    private String getKeyStorePath() {
        return keyStorePath;
    }
}
