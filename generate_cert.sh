#!/bin/bash
openssl req -x509 -newkey rsa:2048 -keyout key.pem -out cert.pem -days 365 -nodes   -subj "/C=US/ST=California/L=San Francisco/O=MyCompany/OU=QA/CN=mydomain.com"
echo "Certificate and key generated: cert.pem, key.pem"

#!expiry short 1 day validity
openssl req -x509 -newkey rsa:2048 -keyout expired_key.pem -out expired_cert.pem -days 1 -nodes -subj "/C=US/ST=California/L=San Francisco/O=MyCompany/OU=QA/CN=expired.mydomain.com"

