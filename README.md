# QR Code with EdDSA Digital Signatures

A Java application that generates QR codes containing digitally signed data using EdDSA (Ed25519) cryptographic algorithm.

## Features

- **EdDSA Key Generation**: Generate Ed25519 key pairs for signing
- **Digital Signatures**: Sign data using EdDSA algorithm
- **QR Code Generation**: Create QR codes containing signed data
- **Signature Verification**: Verify the authenticity of signed data

## Technologies

- **Java 11+**
- **ZXing**: QR code generation library
- **Bouncy Castle**: Cryptographic provider for EdDSA support

## Project Structure

```
src/main/java/com/infosec/qreddsa/
├── Main.java            # Demo application
├── EdDSAUtil.java       # EdDSA cryptographic utilities
├── QRCodeGenerator.java # QR code generation
├── QRCodeScanner.java   # QR code scanning and reading
├── QRCodeViewer.java    # GUI viewer for QR codes
└── SignedQRCode.java    # Signed QR code data model
```

## Installation

### Prerequisites

- Java 11 or higher
- Maven

### Build the project

```bash
mvn clean install
```

## Usage

### Run the demo

```bash
mvn compile exec:java -Dexec.mainClass="com.infosec.qreddsa.Main"
```

Or build and run with Maven:

```bash
mvn clean package
java -cp "target/classes:target/dependency/*" com.infosec.qreddsa.Main
```

### How it works

1. **Generate Key Pair**: Creates an EdDSA (Ed25519) key pair
2. **Sign Data**: Signs your message with the private key
3. **Create QR Code**: Generates a QR code containing:
   - Original data
   - Digital signature
   - Public key for verification
4. **Verify**: Demonstrates signature verification

### Example Output

The program will:
- Generate a signed QR code saved as `signed_qr_code.png`
- Display the signature verification result
- Show the JSON content encoded in the QR code

## QR Code Format

The QR code contains a JSON object with three fields:

```json
{
  "data": "Your original message",
  "signature": "Base64-encoded EdDSA signature",
  "publicKey": "Base64-encoded public key"
}
```

## Security

- Uses **Ed25519** (EdDSA) - a modern, fast, and secure signature algorithm
- Provides **256-bit security**
- Resistant to timing attacks
- Deterministic signatures

## Dependencies

All dependencies are managed via Maven (see `pom.xml`):

- ZXing Core 3.5.2
- ZXing JavaSE 3.5.2
- Bouncy Castle Provider 1.70
- Bouncy Castle PKIX 1.70
- Gson 2.10.1 — used for robust JSON serialization/deserialization of the signed payload


### Update 0.2
- Replaced manual JSON string building with Gson for correct escaping and reliable parsing
- Added `publicKeyFromBase64()` helper to reconstruct Ed25519 public keys from Base64 strings
- Improved verification logic: `SignedQRCode.verify()` now directly uses Gson-parsed data and EdDSAUtil utilities
- Added proper package structure (`com.infosec.qreddsa`)
- Enhanced input validation across all components
- Added QR code scanning and verification functionality
- Improved code documentation with comprehensive JavaDoc

## License

This project is for educational purposes as part of Information Security coursework.

**Project for Information Security course (Projekti për lëndën Siguria e Informacionit)**
