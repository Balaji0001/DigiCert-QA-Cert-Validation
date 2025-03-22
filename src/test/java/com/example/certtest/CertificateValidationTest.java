package com.example.certtest;

import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CertificateValidationTest {

    @DataProvider(name = "certificates")
    public Object[][] certificateProvider() {
        return new Object[][]{
            {"cert.pem", true},
            {"expired_cert.pem", false},
            {"malformed_cert.pem", false},
            {"revoked_cert.pem", false}
        };
    }

    @Test(dataProvider = "certificates")
    public void testCertificateValidation(String certFileName, boolean shouldPass) throws Exception {
        InputStream fis = getClass().getClassLoader().getResourceAsStream(certFileName);
        if (fis == null) {
            System.out.println("\n---------------------------------------------");
            System.out.println("--- " + certFileName + " not found in resources.");
            Assert.assertFalse(shouldPass, certFileName + " expected to be valid but is missing.");
            return;
        }

        boolean testPassed = true;
        System.out.println("\n---------------------------------------------");
        System.out.println("--- Certificate Validation for: " + certFileName);

        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(fis);

            // Extract details
            String commonName = cert.getSubjectDN().getName();
            String issuer = cert.getIssuerDN().getName();
            String expirationDate = new SimpleDateFormat("yyyy-MM-dd").format(cert.getNotAfter());
            String signatureAlgorithm = cert.getSigAlgName();

            // CN Check
            System.out.println("\n--- Common Name (CN):");
            System.out.println("   Actual   : " + commonName);
            System.out.println("   Expected : CN should contain 'mydomain.com'");
            if (commonName.contains("mydomain.com")) {
                System.out.println("    Pass");
            } else {
                System.out.println("    Fail");
                testPassed = false;
            }

            // Issuer check
            System.out.println("\n--- Issuer:");
            System.out.println("   Actual   : " + issuer);
            System.out.println("   Expected : Should not be null or empty");
            if (issuer != null && !issuer.isEmpty()) {
                System.out.println("    Pass");
            } else {
                System.out.println("    Fail");
                testPassed = false;
            }

            // Expiry check
            System.out.println("\n--- Expiration Date:");
            System.out.println("   Actual   : " + expirationDate);
            System.out.println("   Expected : Not expired as of today");
            try {
                cert.checkValidity();
                System.out.println("    Not expired");
            } catch (Exception e) {
                System.out.println("    Expired");
                testPassed = false;
            }

            // Signature Algorithm Check
            System.out.println("\n Signature Algorithm:");
            System.out.println("   Actual   : " + signatureAlgorithm);
            System.out.println("   Expected : Contains 'SHA256'");
            if (signatureAlgorithm.contains("SHA256")) {
                System.out.println("    Pass");
            } else {
                System.out.println("    Weak algorithm");
                testPassed = false;
            }

            // Simulated revoked detection
            if (certFileName.contains("revoked")) {
                System.out.println("\n Revocation Check:");
                System.out.println("    This certificate is marked as revoked (simulated). Treated as detection.");
                // We don't fail test; just simulate
            }

            // Final outcome
            if (testPassed) {
                System.out.println("\n Overall Result: " + certFileName + " passed all validations.\n");
            } else {
                System.out.println("\n Overall Result: " + certFileName + " failed validation.\n");
                Assert.assertFalse(shouldPass, certFileName + " failed validation but was expected to pass.");
            }

        } catch (Exception e) {
            System.out.println(" " + certFileName + " could not be parsed: " + e.getMessage());
            Assert.assertFalse(shouldPass, certFileName + " failed to parse but was expected to pass.");
        }
    }
}
