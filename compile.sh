#!/bin/sh
set -eu

mkdir -p bin
javac -cp "lib/slick.jar:lib/jar/*" -d bin $(find src -name "*.java")
