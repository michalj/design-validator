#!/bin/bash

sbt doc

cd target
git clone git@github.com:michalj/design-validator.git --branch gh-pages

cd design-validator
git rm -r scaladoc
mkdir scaladoc
cd ..

cp -R scala-2.9.2/api/* design-validator/scaladoc

cd design-validator
git add scaladoc
git commit -m "Automatic documentation commit"
git push

cd ..
rm -R -f design-validator
cd ..
