# Predictable Password Reset Token Exploit

This repository contains a proof-of-concept (PoC) exploit for a vulnerability in a PHP-based password reset system.  
The vulnerability is due to weak randomness in token generation, allowing attackers to brute-force the password reset token.

## Vulnerability Details
- The password reset token is generated using `md5(mt_rand(1, 100) . $user . time() . session_id());`
- The entropy is low because:
  - `mt_rand(1, 100)` has only 100 possible values.
  - `$user` (username) is known.
  - `session_id()` is predictable under some conditions.
  - `time()` value can be estimated from response timing.

## Exploit Scenario

An attacker can:
1. Trigger a password reset request for the target user.
2. Brute-force the reset token.
3. Change the password and take over the account.

## Exploit Usage

1. Run the vulnerable PHP server:
```sh
   cd /home/test
   php -S MACHINE_IP:8000 vuln.php
```
   
2. Add a new user for testing
  ```sh
    sqlite3 users.sqlite "INSERT INTO users (name, password) VALUES ('admin', '1234');"
```

4. Verify that the user has been added
```sh
   sqlite3 users.sqlite "SELECT * FROM users;"
   ```
   
## If the page is working, we can now use the exploit script.

5. Run the exploit script
```sh
   python3 exploit.py
   ```

7. Verify the database again
```sh
  sqlite3 users.sqlite "SELECT * FROM users;"
  ```
## If the password has changed from 1234 to hacked1234, the exploit was successful! 🚀

## References
https://sonarcloud.io/project/security_hotspots?id=SonarSourceResearch_2022_calendar_1


