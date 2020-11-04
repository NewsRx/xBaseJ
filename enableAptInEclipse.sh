#!/bin/bash
#*******************************************************************************
# Copyright (c) 2020 NewSRX Tech LLC.
#
# This program and the accompanying materials, except where otherwise noted,
# are the property of NewSRX Tech LLC.
#
# All rights reserved.
#*******************************************************************************

#AFTER importing project into eclipse with the buildship plugin run this script
set -e
set -o pipefail

cd "$(dirname "$0")"

rm .settings/org.eclipse.jdt.apt.core.prefs || true
rm .settings/org.eclipse.jdt.core.prefs || true
./gradlew eclipseJdtApt

rm .factorypath || true
./gradlew eclipseFactorypath

./gradlew eclipseJdt

