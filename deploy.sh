#!/bin/sh

cf delete quotes -f
cf delete accounts -f
cf delete portfolio -f
cf delete webtrader -f


cf push -f quotes/manifest.yml
cf push -f accounts/manifest.yml
cf push -f portfolio/manifest.yml
cf push -f web/manifest.yml
