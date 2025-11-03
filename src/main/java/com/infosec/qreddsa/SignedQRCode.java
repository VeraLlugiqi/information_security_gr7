package com.infosec.qreddsa;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Base64;
import com.google.gson.Gson;

/**
 * Përfaqëson një QR kod me nënshkrim digjital që përfshin të dhënat origjinale,
 * nënshkrimin EdDSA dhe çelësin publik për verifikim
 */
public class SignedQRCode {
    private final String data;
    private final String signature;
    private final String publicKey;

    public SignedQRCode(String data, String signature, String publicKey) {
        this.data = data;
        this.signature = signature;
        this.publicKey = publicKey;
    }

    /**
     * Krijon një QR kod të nënshkruar duke nënshkruar të dhënat me çelësin privat
     */
    public static SignedQRCode create(String data, KeyPair keyPair) throws Exception {
        if (data == null || data.trim().isEmpty()) {
            throw new IllegalArgumentException("Të dhënat për nënshkrim nuk mund të jenë null ose bosh");
        }
        if (data.length() > 2900) {
            throw new IllegalArgumentException("Të dhënat janë shumë të gjata për kodim në QR kod (maksimumi ~2900 karaktere)");
        }
        if (keyPair == null) {
            throw new IllegalArgumentException("Çifti i çelësave nuk mund të jetë null");
        }
        if (keyPair.getPrivate() == null || keyPair.getPublic() == null) {
            throw new IllegalArgumentException("Çifti i çelësave duhet të ketë të dy çelësat (privat dhe publik)");
        }
        
        byte[] signatureBytes = EdDSAUtil.sign(data, keyPair.getPrivate());
        String signatureBase64 = EdDSAUtil.signatureToBase64(signatureBytes);
        String publicKeyBase64 = EdDSAUtil.publicKeyToBase64(keyPair.getPublic());
        
        return new SignedQRCode(data, signatureBase64, publicKeyBase64);
    }

    /**
     * Konverton QR kodin e nënshkruar në një string JSON që mund të kodohet në një QR kod
     */
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    
    /**
     * Verifikon nënshkrimin duke përdorur çelësin publik të integruar
     */
    public boolean verify() throws Exception {
        byte[] signatureBytes = Base64.getDecoder().decode(this.signature);
        PublicKey publicKey = EdDSAUtil.publicKeyFromBase64(this.publicKey);
        return EdDSAUtil.verify(this.data, signatureBytes, publicKey);
    }

    /**
     * Parse një SignedQRCode nga një string JSON
     */
    public static SignedQRCode fromJSON(String json) {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("Stringu JSON nuk mund të jetë null ose bosh");
        }
        
        try {
            Gson gson = new Gson();
            SignedQRCode result = gson.fromJson(json, SignedQRCode.class);
            
            if (result == null) {
                throw new IllegalArgumentException("Dështoi parsing i JSON: rezultati është null");
            }
            if (result.data == null || result.signature == null || result.publicKey == null) {
                throw new IllegalArgumentException("SignedQRCode i pavlefshëm: mungojnë fushat e nevojshme (data, signature, ose publicKey)");
            }
            if (result.data.trim().isEmpty()) {
                throw new IllegalArgumentException("SignedQRCode i pavlefshëm: fusha data është bosh");
            }
            
            return result;
        } catch (com.google.gson.JsonSyntaxException e) {
            throw new IllegalArgumentException("Format JSON i pavlefshëm: " + e.getMessage(), e);
        }
    }

    /**
     * Gjeneron një imazh QR kod që përmban të dhënat e nënshkruara me korrigjim gabimesh parazgjedhje
     */
    public void generateQRCode(String filePath) throws Exception {
        generateQRCode(filePath, QRCodeGenerator.ErrorCorrection.M);
    }
    
    /**
     * Gjeneron një imazh QR kod që përmban të dhënat e nënshkruara me nivel të specifikuar korrigjimi gabimesh
     */
    public void generateQRCode(String filePath, QRCodeGenerator.ErrorCorrection errorCorrection) throws Exception {
        QRCodeGenerator.generateQRCode(toJSON(), filePath, errorCorrection);
    }

    public String getData() {
        return data;
    }

    public String getSignature() {
        return signature;
    }
}
