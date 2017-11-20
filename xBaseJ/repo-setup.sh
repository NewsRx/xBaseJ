#!/bin/bash

set -e
set -o pipefail

trap 'echo ERROR!; read a' ERR

cd "$(dirname "$0")"

if [ ! -d .git ]; then
	echo "FATAL. Missing .git folder!"
	exit -1
fi

export REPO="$(basename "$(pwd)")"

echo "Running: 'cd ~; git git init --bare --share '${REPO}'; exit'"
ssh git@www.newsrx.com 'cd ~; git init --bare --share '${REPO}'; exit'

git remote add origin "ssh://git@www.newsrx.com/home/git/${REPO}/" || true

touch .gitignore

cat > .gitignore << EOT
/build/
*.pydevproject
.metadata
.gradle
bin/
tmp/
*.tmp
*.bak
*.swp
*~.nib
local.properties
.settings/
.loadpath
.project
.externalToolBuilders/
*.launch
.cproject
.classpath
.buildpath
.target
.texlipse
.DS_store
/war/
EOT
sort .gitignore |uniq > .gitignore.tmp
mv -v .gitignore.tmp .gitignore

if [ -f "build.gradle" ]; then
	gradle :wrapper
	gradle :create-dirs
	find src | while read folder; do
		if [ ! -d "$folder" ]; then continue; fi
		touch "$folder"/.gitignore
	done
fi

touch README.md || true
git add '*.sh' || true
git add 'pom.xml' || true
git add '.gitignore' || true
git add '*.md' || true
git add '*.java' || true
git add '*?.gradle' || true
git add 'gradle.properties' || true
git add 'gradle/' || true
git add 'gradlew' || true
git add 'gradlew.*' || true
git add 'src' || true
git commit -a -m "initial repo setup" || true

git push -u origin master

printf "DONE: "
read a;

exit 0

