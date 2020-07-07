#!/bin/bash

set -e
set -o pipefail

trap 'echo ERROR!; read a' ERR

cd "$(dirname "$0")"

if [ ! -d .git ]; then
	echo "FATAL. Missing .git folder!"
	exit -1
fi

export repo="$(basename "$(pwd)")"

ORG=NewSRX-Tech-LLC
GITBUCKET_HTTP_HOST="www.newsrx.com"
GITBUCKET_SSH_HOST="tomcat-0002.newsrx.com"
PORT=29418

if [ ! -f ~/git/git-scripts/gitbucket-info.sh ]; then
	echo "create the file git-scripts/gitbucket-info.sh - place into the file: export gb_userpass='username:password'"
	exit -1
fi

. ~/git/git-scripts/gitbucket-info.sh

# create repo
curl -k -X POST --user "$gb_userpass" \
	"https://$GITBUCKET_HTTP_HOST/gitbucket/api/v3/orgs/$ORG/repos" \
	-d "{\"name\":\"$repo\", \"private\":true}"

# assign collaborators
for u in jason michael; do
	curl -k -X PUT --user "$gb_userpass" \
		"https://$GITBUCKET_HTTP_HOST/gitbucket/api/v3/repos/$ORG/$repo/collaborators/$u" \
		-d "{\"permission\":\"admin\"}" || true
done

git remote add origin "ssh://migrate@$GITBUCKET_SSH_HOST:$PORT/$ORG/$repo.git" 2> /dev/null || true
git remote set-url origin "ssh://git@$GITBUCKET_SSH_HOST:$PORT/$ORG/$repo.git"

touch .gitignore

cat >> .gitignore << EOT
*~
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
    mv -v "build.gradle" "build.gradle.tmp"
	gradle :wrapper --distribution-type all
	mv -v "build.gradle.tmp" "build.gradle"
	./gradlew :create-dirs || true
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


