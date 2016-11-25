#!/bin/bash

M2_SETTINGS=travis/settings.xml

if [ $TRAVIS_BRANCH == 'travis-develop' ] ; then 
  if [ $TRAVIS_PULL_REQUEST == 'false' ] ; then 
    mvn clean deploy --settings $M2_SETTINGS 
  else
    mvn clean verify --settings $M2_SETTINGS 
  fi
elif [ $TRAVIS_BRANCH = 'travis-master' ] ; then
  if [ $TRAVIS_PULL_REQUEST == 'false' ] ; then
    mvn clean 
  else
    mvn -B -DdryRun=true release:prepare --settings $M2_SETTINGS 
  fi
fi

