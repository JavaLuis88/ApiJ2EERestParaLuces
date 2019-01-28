/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esmeralda.proyectos.logica;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author lingmoy
 */
public class HMACSHA {

    public HMACSHA() {

    }

   public String encriptHMACSHA256(String plaintext, String key) throws Exception {

        Mac sha256_HMAC;
        SecretKeySpec secret_key;
        String hash;

        sha256_HMAC = Mac.getInstance("HmacSHA256");
        secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        hash = Base64.encodeBase64String(sha256_HMAC.doFinal(plaintext.getBytes()));
        return hash;

    }

}
