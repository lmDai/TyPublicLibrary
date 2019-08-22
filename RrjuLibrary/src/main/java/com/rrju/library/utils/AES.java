package com.rrju.library.utils;

/**
 * LICENSE AND TRADEMARK NOTICES
 * <p>
 * Except where noted, sample source code written by Motorola Mobility Inc. and
 * provided to you is licensed as described below.
 * <p>
 * Copyright (c) 2012, Motorola, Inc.
 * All  rights reserved except as otherwise explicitly indicated.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p>
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * <p>
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <p>
 * - Neither the name of Motorola, Inc. nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * <p>
 * Other source code displayed may be licensed under Apache License, Version
 * 2.
 * <p>
 * Copyright ¬© 2012, Android Open Source Project. All rights reserved unless
 * otherwise explicitly indicated.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0.
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

// Please refer to the accompanying article at 
// http://developer.motorola.com/docs/using_the_advanced_encryption_standard_in_android/
// A tutorial guide to using AES encryption in Android
// First we generate a 256 bit secret key; then we use that secret key to AES encrypt a plaintext message.
// Finally we decrypt the ciphertext to get our original message back.
// We don't keep a copy of the secret key - we generate the secret key whenever it is needed, 
// so we must remember all the parameters needed to generate it -
// the salt, the IV, the human-friendly passphrase, all the algorithms and parameters to those algorithms.
// Peter van der Linden, April 15 2012

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private static final String KEY_GENERATION_ALG = "PBKDF2WithHmacSHA1";
    private static final int HASH_ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final String CIPHERMODEPADDING = "AES/CBC/PKCS7Padding";
    private static char[] humanPassphrase = {'P', 'e', 'r', ' ', 'v', 'a', 'l', 'l',
            'u', 'm', ' ', 'd', 'u', 'c', 'e', 's', ' ', 'L', 'a', 'b', 'a',
            'n', 't'};
    private static String ivs = "0392039203920300";
    private static String salts = "smkldospdosldaaa";//key，可自行修改
    /**
     * 加密
     * @param sSrc
     * @return
     */
    public static String encrypt(String sSrc) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(CIPHERMODEPADDING);
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(KEY_GENERATION_ALG);
            PBEKeySpec myKeyspec = new PBEKeySpec(humanPassphrase, salts.getBytes(), HASH_ITERATIONS, KEY_LENGTH);
            SecretKey sk = keyfactory.generateSecret(myKeyspec);
            byte[] skAsByteArray = sk.getEncoded();
            SecretKeySpec skforAES = new SecretKeySpec(skAsByteArray, "AES");
            IvParameterSpec IV = new IvParameterSpec(ivs.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skforAES, IV);
            byte[] bytes = cipher.doFinal(sSrc.getBytes("utf-8"));
            String base64_ciphertext = Base64Encoder.encode(bytes);
            return base64_ciphertext;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * @param sSrc
     * @return
     */
    public static String decrypt(String sSrc) {
        try {
            byte[] s = Base64Decoder.decodeToBytes(sSrc);
            Cipher c = Cipher.getInstance(CIPHERMODEPADDING);
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(KEY_GENERATION_ALG);
            PBEKeySpec myKeyspec = new PBEKeySpec(humanPassphrase, salts.getBytes(), HASH_ITERATIONS, KEY_LENGTH);
            SecretKey sk = keyfactory.generateSecret(myKeyspec);
            byte[] skAsByteArray = sk.getEncoded();
            SecretKeySpec skforAES = new SecretKeySpec(skAsByteArray, "AES");
            IvParameterSpec IV = new IvParameterSpec(ivs.getBytes());
            c.init(Cipher.DECRYPT_MODE, skforAES, IV);
            byte[] bytes = c.doFinal(s);
            String decrypted = new String(bytes,"utf-8");
            return decrypted;
        } catch (Exception e) {
            return null;
        }
    }

}