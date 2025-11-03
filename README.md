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

Kjo komandë do të:
- Shkarkojë të gjitha varësitë e nevojshme
- Kompilojë kodin burimor
- Krijojë skedarët e klasave në `target/classes`

## Përdorimi

### Ekzekutimi i Aplikacionit Demo

```bash
mvn compile exec:java -Dexec.mainClass="com.infosec.qreddsa.Main"
```

Ose ndërtoni dhe ekzekutoni manualisht:

```bash
mvn clean package
java -cp "target/classes:target/dependency/*" com.infosec.qreddsa.Main
```

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

## Varësitë

Të gjitha varësitë menaxhohen përmes Maven (shih `pom.xml`):

- **ZXing Core 3.5.2**: Bibliotekë bazë për gjenerimin dhe leximin e QR kodeve
- **ZXing JavaSE 3.5.2**: Komponentë për Java SE për trajtimin e imazheve
- **Bouncy Castle Provider 1.70**: Provider kriptografik që ofron mbështetje për EdDSA/Ed25519
- **Bouncy Castle PKIX 1.70**: Komponentë PKIX për trajtimin e çelësave
- **Gson 2.10.1**: Bibliotekë për serializimin dhe deserializimin e besueshëm të JSON

## Analiza e Kodit

### Përmbushja e Kërkesave

✅ **Gjenerimi i QR Kodit**: Implementuar plotësisht me `QRCodeGenerator.java`
✅ **Nënshkrime Digjitale me EdDSA**: Implementuar plotësisht me `EdDSAUtil.java` duke përdorur Ed25519
✅ **Në Java**: I gjithë kodi është shkruar në Java 11+

### Komponentët dhe Nevojshmëria e Tyre

Të gjitha komponentët janë të nevojshëm dhe plotësojnë një qëllim specifik:

1. **EdDSAUtil.java** - **E nevojshme**: Operacionet bazë kriptografike
2. **SignedQRCode.java** - **E nevojshme**: Modeli i të dhënave dhe logjika e nënshkrimit
3. **QRCodeGenerator.java** - **E nevojshme**: Gjenerimi i QR kodeve
4. **QRCodeScanner.java** - **E nevojshme**: Skanimi dhe verifikimi i QR kodeve (për zgjidhje të plotë)
5. **QRCodeViewer.java** - **Opsionale por e dobishme**: Përmirëson demonstrimin
6. **Main.java** - **E nevojshme**: Aplikacioni demo që tregon funksionalitetin

### Cilësia e Kodit

- ✅ Validim i plotë i inputeve në të gjitha metodat
- ✅ Dokumentim i plotë me JavaDoc
- ✅ Trajtim i gabimeve i përmirësuar
- ✅ Strukturë e organizuar dhe e lexueshme
- ✅ Përdorim i bibliotekave standarde dhe të sigurta

## Shembuj Përdorimi

### Shembull 1: Krijimi i një QR Kodi të Nënshkruar

```java
// Gjenero çift çelësash
KeyPair keyPair = EdDSAUtil.generateKeyPair();

// Krijoni QR kod të nënshkruar
String data = "Mesazhi im i sigurt";
SignedQRCode signedQRCode = SignedQRCode.create(data, keyPair);

// Gjenero imazhin e QR kodit
signedQRCode.generateQRCode("qr_code.png");
```

### Shembull 2: Verifikimi i një QR Kodi

```java
// Skano QR kodin
SignedQRCode scanned = QRCodeScanner.scanSignedQRCode("qr_code.png");

// Verifiko nënshkrimin
boolean isValid = scanned.verify();
if (isValid) {
    System.out.println("Nënshkrimi është i vlefshëm!");
    System.out.println("Të dhënat: " + scanned.getData());
} else {
    System.out.println("Nënshkrimi nuk është i vlefshëm!");
}
```

### Shembull 3: Verifikim i Direkt

```java
// Verifiko direkt nga skedari
boolean isValid = QRCodeScanner.verifyScannedQRCode("qr_code.png");
```

## Testimi

Për të testuar aplikacionin:

1. Ekzekutoni `Main.java` për të parë demonstrimin e plotë
2. Skanoni QR kodin e gjeneruar me një aplikacion skanimi QR kod
3. Verifikoni që përmbajtja JSON është e saktë
4. Testoni verifikimin e nënshkrimit

## Përmirësime të Mundshme

- [ ] Ruajtja e çelësave në skedarë për përdorim të përsëritur
- [ ] Mbështetje për çelësa të shumtë (key rotation)
- [ ] Enkriptim i të dhënave para nënshkrimit
- [ ] Mbështetje për QR kode me madhësi të ndryshme
- [ ] API REST për gjenerimin dhe verifikimin e QR kodeve

## Licenca

Ky projekt është për qëllime edukative si pjesë e punës së kursit të Sigurisë së Informacionit.

**Projekti për lëndën Siguria e Informacionit - Grupi 7**

## Kontakt dhe Mbështetje

Për pyetje ose probleme, ju lutemi kontaktoni ekipin e projektit.

---

**Shënim**: Ky projekt demonstron përdorimin praktik të kriptografisë moderne (EdDSA) në kombinim me teknologjinë e QR kodeve për të siguruar autenticitetin dhe integritetin e të dhënave.
