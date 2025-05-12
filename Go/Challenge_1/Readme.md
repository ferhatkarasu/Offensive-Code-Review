# Broken Authentication Spoofing

This application uses a role cookie to authenticate users. The server only allows access to the /admin endpoint if the role=admin cookie is present and the client’s IP address is either 127.0.0.1 or localhost. However, the X-Forwarded-For header, which is provided by the client, is used to check the client’s IP address. This header can be manipulated, allowing an attacker to bypass the authentication check and access the admin panel by submitting a forged role=admin cookie along with a manipulated IP address.

## Exploit Scenario
- An attacker can exploit this vulnerability by performing the following steps:
- The attacker manipulates the X-Forwarded-For header to present their own IP address as 127.0.0.1 (localhost).
- The attacker sends a request with the role=admin cookie.
- The server, seeing the X-Forwarded-For header as 127.0.0.1 and the role=admin cookie, grants access to the admin dashboard, even though the attacker is not actually an administrator.
- This results in a broken authentication scenario, where an attacker can spoof the authentication check and access the admin panel.

# Exploit Usage
- Run the Vulnerable Go Application:
```bash
go run vuln_app.go
```
- The server will be listening at http://localhost:1337.

# Launch the Attack:
Use the following curl command to send a crafted request that will bypass the authentication check and access the admin panel:
```bash
curl -i -H "X-Forwarded-For: 127.0.0.1" -H "Cookie: role=admin" http://localhost:1337/admin
```
This request includes the manipulated X-Forwarded-For header and the role=admin cookie. The server will incorrectly identify the client’s IP as 127.0.0.1 and allow access to the admin dashboard.

## Impact
- This vulnerability allows attackers to bypass authentication by manipulating the X-Forwarded-For header. It can lead to unauthorized access to the admin panel, allowing attackers to view and modify sensitive information.

## Mitigation
- Header Validation: Properly validate the X-Forwarded-For header and only accept requests from trusted proxy servers.
- Cookie Validation: Ensure that cookies, especially those related to authentication like role=admin, are validated securely and cannot be easily manipulated by the client.
- IP Address Verification: Implement additional checks to validate that the client's IP address is actually 127.0.0.1 or localhost before allowing access to sensitive areas like the admin panel.

# References
https://github.com/yeswehack/vulnerable-code-snippets/tree/main
