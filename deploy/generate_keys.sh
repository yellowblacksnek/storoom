#!/bin/sh
openssl genrsa -out temp.key 2048
openssl rsa -in temp.key -pubout -out public.key
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in temp.key -out private.key
rm temp.key