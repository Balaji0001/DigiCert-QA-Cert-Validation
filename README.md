# DigiCert Senior QA Technical Challenge

##  Project Contents:
- `TestStrategy pdf file` — Test strategy document outlining the testing approach and scenarios.
- `generate_cert.sh` or manual OpenSSL instructions — for generating certificates.
- `src/test/resources/` — contains the test certificates:
  - `cert.pem` — Valid certificate
  - `expired_cert.pem` — Expired certificate
  - `malformed_cert.pem` — Malformed/corrupted certificate
  - `revoked_cert.pem` — Simulated revoked certificate
- `CertificateValidationTest.java` — Main TestNG test class that performs validation.

##  How to Execute the Tests

###  Prerequisites:
- Java JDK 8 or higher
- Maven installed
- OpenSSL installed and added to the system PATH
- TestNG (added via Maven dependency)

###  Generate a valid certificate (if not already present):
Use this OpenSSL command:
```
openssl req -x509 -newkey rsa:2048 -keyout key.pem -out cert.pem -days 365 -nodes -subj "/C=US/ST=California/L=San Francisco/O=MyCompany/OU=QA/CN=mydomain.com"
```
> Place the generated `cert.pem` in `src/test/resources/`.

###  Run the tests:
In your project root directory, execute:
```
mvn test
```

###  Test Output Details:
- The console will display detailed validation:
  - Actual values vs. expected conditions for each attribute (CN, Issuer, Expiration, Signature Algorithm).
  - Clear pass/fail indication for each check.
  - An overall summary of pass/fail for each certificate.
- Revoked certificates are simulated and will be flagged without failing the entire suite.

##  Optional:
You can integrate these tests into a CI/CD pipeline (GitHub Actions or Jenkins) for automated certificate validation.


##  Sample output :
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.

---------------------------------------------
--- Certificate Validation for: cert.pem

--- Common Name (CN):
   Actual   : CN=mydomain.com, OU=QA, O=MyCompany, L=San Francisco, ST=California, C=US
   Expected : CN should contain 'mydomain.com'
    Pass

--- Issuer:
   Actual   : CN=mydomain.com, OU=QA, O=MyCompany, L=San Francisco, ST=California, C=US
   Expected : Should not be null or empty
    Pass

--- Expiration Date:
   Actual   : 2026-03-22
   Expected : Not expired as of today
    Not expired

 Signature Algorithm:
   Actual   : SHA256withRSA
   Expected : Contains 'SHA256'
    Pass

 Overall Result: cert.pem passed all validations.


---------------------------------------------
--- Certificate Validation for: expired_cert.pem

--- Common Name (CN):
   Actual   : CN=expired.mydomain.com, OU=QA, O=MyCompany, L=San Francisco, ST=California, C=US
   Expected : CN should contain 'mydomain.com'
    Pass

--- Issuer:
   Actual   : CN=expired.mydomain.com, OU=QA, O=MyCompany, L=San Francisco, ST=California, C=US
   Expected : Should not be null or empty
    Pass

--- Expiration Date:
   Actual   : 2025-03-23
   Expected : Not expired as of today
    Not expired---- Here I am facing restriction to generate past dates hence short expiry provided.

 Signature Algorithm:
   Actual   : SHA256withRSA
   Expected : Contains 'SHA256'
    Pass

 Overall Result: expired_cert.pem passed all validations.


---------------------------------------------
--- Certificate Validation for: malformed_cert.pem
 malformed_cert.pem could not be parsed: Could not parse certificate: java.io.IOException: java.lang.IllegalArgumentException: Illegal base64 character 5b

---------------------------------------------
--- Certificate Validation for: revoked_cert.pem

--- Common Name (CN):
   Actual   : CN=mydomain.com, OU=QA, O=MyCompany, L=San Francisco, ST=California, C=US
   Expected : CN should contain 'mydomain.com'
    Pass

--- Issuer:
   Actual   : CN=mydomain.com, OU=QA, O=MyCompany, L=San Francisco, ST=California, C=US
   Expected : Should not be null or empty
    Pass

--- Expiration Date:
   Actual   : 2026-03-22
   Expected : Not expired as of today
    Not expired

 Signature Algorithm:
   Actual   : SHA256withRSA
   Expected : Contains 'SHA256'
    Pass

 Revocation Check:
    This certificate is marked as revoked (simulated). Treated as detection.

 Overall Result: revoked_cert.pem passed all validations.


===============================================
Default Suite
Total tests run: 4, Passes: 4, Failures: 0, Skips: 0
===============================================

---

> For any queries, feel free to reach out.
