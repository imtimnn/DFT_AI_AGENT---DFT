#!/bin/bash
mvn clean package -Dmaven.test.skip=true && java -jar target/dft_daae.jar
