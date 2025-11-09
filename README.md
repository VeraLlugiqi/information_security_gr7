# QR Code me Nënshkrime Digjitale EdDSA

Një aplikacion Java që gjeneron QR kode që përmbajnë të dhëna të nënshkruara digjitalisht duke përdorur algoritmin kriptografik EdDSA (Ed25519).

## Përmbledhje e Projektit

Ky projekt implementon një sistem të plotë për gjenerimin e QR kodeve me nënshkrime digjitale duke përdorur algoritmin EdDSA (Edwards-curve Digital Signature Algorithm), specifikisht Ed25519. Sistemi lejon nënshkrimin e të dhënave, gjenerimin e QR kodeve që përmbajnë të dhënat e nënshkruara, dhe verifikimin e autenticitetit të tyre.

## Karakteristikat Kryesore

- **Gjenerimi i Çifteve të Çelësave EdDSA**: Gjeneron çifte çelësash Ed25519 për nënshkrim
- **Nënshkrime Digjitale**: Nënshkron të dhëna duke përdorur algoritmin EdDSA
- **Gjenerimi i QR Kodeve**: Krijon QR kode që përmbajnë të dhëna të nënshkruara
- **Verifikimi i Nënshkrimit**: Verifikon autenticitetin e të dhënave të nënshkruara
- **Skanimi i QR Kodeve**: Lexon dhe verifikon QR kode nga imazhe
- **Shfaqja Grafike**: Shfaq QR kode në një dritare grafike për demonstrim

## Teknologjitë e Përdorura

- **Java 11+**: Gjuha programuese kryesore
- **ZXing**: Bibliotekë për gjenerimin dhe leximin e QR kodeve
- **Bouncy Castle**: Provider kriptografik për mbështetjen e EdDSA
- **Gson**: Bibliotekë për serializimin dhe deserializimin e JSON

## Struktura e Projektit

```
src/main/java/com/infosec/qreddsa/
├── Main.java            # Aplikacioni demo që demonstron funksionalitetin
├── EdDSAUtil.java       # Utility klasa për operacionet kriptografike EdDSA
├── QRCodeGenerator.java # Klasa për gjenerimin e QR kodeve
├── QRCodeScanner.java   # Klasa për skanimin dhe leximin e QR kodeve
├── QRCodeViewer.java    # Klasa për shfaqjen grafike të QR kodeve
└── SignedQRCode.java    # Modeli i të dhënave për QR kode të nënshkruara
```

## Përshkrimi i Komponentëve

### 1. EdDSAUtil.java
Klasa kryesore për operacionet kriptografike:
- **generateKeyPair()**: Gjeneron një çift çelësash Ed25519
- **sign()**: Nënshkron të dhëna duke përdorur çelësin privat
- **verify()**: Verifikon nënshkrimin duke përdorur çelësin publik
- **publicKeyToBase64()**: Konverton çelësin publik në string Base64
- **signatureToBase64()**: Konverton nënshkrimin në string Base64
- **publicKeyFromBase64()**: Rikonstrukton çelësin publik nga string Base64

### 2. SignedQRCode.java
Modeli i të dhënave që përfaqëson një QR kod të nënshkruar:
- **create()**: Krijon një QR kod të nënshkruar duke nënshkruar të dhënat
- **toJSON()**: Konverton objektin në format JSON për kodim në QR kod
- **fromJSON()**: Parse një objekt SignedQRCode nga një string JSON
- **verify()**: Verifikon nënshkrimin e të dhënave
- **generateQRCode()**: Gjeneron imazhin e QR kodit

### 3. QRCodeGenerator.java
Klasa për gjenerimin e QR kodeve:
- **generateQRCode()**: Gjeneron një QR kod nga tekst me nivele të ndryshme korrigjimi gabimesh
- Mbështet nivele korrigjimi gabimesh: L (~7%), M (~15%), Q (~25%), H (~30%)

### 4. QRCodeScanner.java
Klasa për skanimin dhe leximin e QR kodeve:
- **scanQRCode()**: Lexon përmbajtjen e një QR kodi nga një imazh
- **scanSignedQRCode()**: Skanon dhe parse një QR kod të nënshkruar
- **verifyScannedQRCode()**: Skanon dhe verifikon automatikisht nënshkrimin

### 5. QRCodeViewer.java
Klasa për shfaqjen grafike të QR kodeve:
- **displayQRCode()**: Shfaq QR kodin në një dritare Swing për demonstrim

### 6. Main.java
Aplikacioni demo që demonstron të gjithë funksionalitetin:
- Gjeneron çift çelësash
- Nënshkron të dhëna
- Krijon QR kod
- Verifikon nënshkrimin
- Demonstron skanimin dhe verifikimin

## Instalimi

### Parakushtet

- Java 11 ose më i lartë
- Maven 3.6 ose më i lartë

### Ndërtimi i Projektit

```bash
cd information_security_gr7
mvn clean install
```


## Përdorimi


### Si Funksionon

1. **Gjenerimi i Çiftit të Çelësave**: Krijon një çift çelësash EdDSA (Ed25519)
2. **Nënshkrimi i të Dhënave**: Nënshkron mesazhin tuaj me çelësin privat
3. **Krijimi i QR Kodit**: Gjeneron një QR kod që përmban:
   - Të dhënat origjinale
   - Nënshkrimin digjital
   - Çelësin publik për verifikim
4. **Verifikimi**: Demonstron verifikimin e nënshkrimit
5. **Skanimi**: Skanon QR kodin dhe verifikon nënshkrimin

### Rezultati i Programit

Programi do të:
- Gjenerojë një QR kod të nënshkruar të ruajtur si `signed_qr_code.png`
- Shfaqë rezultatin e verifikimit të nënshkrimit
- Tregojë përmbajtjen JSON të koduar në QR kod
- Hapë një dritare grafike që shfaq QR kodin

## Formati i QR Kodit

QR kodi përmban një objekt JSON me tre fusha:

```json
{
  "data": "Mesazhi juaj origjinal",
  "signature": "Nënshkrimi EdDSA i koduar në Base64",
  "publicKey": "Çelësi publik i koduar në Base64"
}
```

### Shembull i Përmbajtjes JSON

```json
{
  "data": "Secure message: Hello from Information Security Group 7!",
  "signature": "ABC123...XYZ789",
  "publicKey": "DEF456...UVW012"
}
```

## Siguria

### Algoritmi EdDSA (Ed25519)

- **Siguri 256-bit**: Ofron siguri të lartë kriptografike
- **Rezistent ndaj Timing Attacks**: Algoritmi është i sigurt kundër sulmeve të kohës
- **Nënshkrime Deterministe**: Të njëjtat të dhëna prodhojnë të njëjtin nënshkrim
- **Shpejtësi**: Ed25519 është shumë më i shpejtë se algoritmet tradicionale si RSA
- **Madhësi të Vogla**: Çelësat dhe nënshkrimet janë relativisht të vogla

### Karakteristikat e Sigurisë

1. **Autenticiteti**: Nënshkrimi garanton që të dhënat nuk janë modifikuar
2. **Integriteti**: Çdo ndryshim në të dhëna do të shkaktojë dështim të verifikimit
3. **Non-repudiation**: Nënshkrimi provon që pronari i çelësit privat ka nënshkruar të dhënat


## Testimi

Për të testuar aplikacionin:

1. Ekzekutoni `Main.java` për të parë demonstrimin e plotë
2. Skanoni QR kodin e gjeneruar me një aplikacion skanimi QR kod
3. Verifikoni që përmbajtja JSON është e saktë
4. Testoni verifikimin e nënshkrimit

**Projekti për lëndën Siguria e Informacionit - Grupi 7**
**11.2025**
