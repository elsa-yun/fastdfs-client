package com.elsa.dfsutils.filter;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Key;

import org.apache.commons.codec.binary.Hex;
import org.apache.shiro.crypto.AesCipherService;

import com.elsa.dfsutils.file.BaseFile;
import com.elsa.dfsutils.file.TxtFile;

/**
 * AES加密过滤器
 * 
 * @describe
 * @author 叶礼锋
 * 
 *         2014-12-23 上午10:48:36
 */
@SuppressWarnings(value = { "unused", "deprecation", "serial", "unchecked", "static-access", "rawtypes" })
public class TxtEncryptionFilter extends FileFilter<String> {

	@Override
	public void doFilter(BaseFile baseFile, String adjunct) throws Exception {
		TxtFile txtFile = TxtFile.class.cast(baseFile);
		boolean encryptionFlag = txtFile.isEncryptionFlag();
		if (encryptionFlag) {
			File inFile = txtFile.getFile();
			File outFile = File.createTempFile("TXT", ".tmp", inFile.getParentFile());
			AesCipherService aes = new AesCipherService();
			byte key[] = aes.generateNewKey(128).getEncoded();
			aes.encrypt(new FileInputStream(inFile), new FileOutputStream(outFile), key);
			txtFile.setSecretKey(Hex.encodeHexString(key));
			txtFile.setOriginalFile(inFile);
			txtFile.setFile(outFile);
		}
	}

}
