# Case-Insensitive Authentication Bypass PoC

This repository contains a proof-of-concept (PoC) exploit for a case-insensitive authentication bypass vulnerability in an Express.js web application. The vulnerability arises due to improper handling of route validation in middleware.

## Vulnerability Details
- The application has a middleware that checks if a request URL starts with `/api` and then verifies authentication.
- However, Express.js routing is case-insensitive by default, meaning `/api` and `/API` will be treated the same.
- The middleware uses a case-sensitive check (`req.url.startsWith('/api')`), allowing an attacker to bypass authentication by sending a request to `/API`.

## Exploit Scenario
An attacker can:
1. Submit a crafted request to `/API/secret_data` instead of `/api/secret_data`.
2. The middleware does not validate authentication since it checks only lowercase `/api`.
3. Express.js still routes the request correctly, exposing protected resources.

## Exploit Usage
1. Run the vulnerable Express.js application:
   ```sh
   node server.js
   ```
2. Send a bypass request:
   ```sh
   curl -X GET "http://localhost:1337/API/secret_data"
   ```
3. If the exploit is successful, authentication is bypassed, and sensitive data is exposed! 🚀

## References
https://www.sonarsource.com/knowledge/code-challenges/advent-calendar-2022/
https://expressjs.com/en/api.html#:~:text=case%20sensitive%20routing
