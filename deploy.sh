#!/bin/sh

cf delete quotes -f
cf delete accounts -f
cf delete portfolio -f
cf delete webtrader -f

cd quotes
cf push
cd ..
cf accounts
cf push
cd ..
cd portfolio
cf push 
cd ..
cd web
cf push
cd ..

