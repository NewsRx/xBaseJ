# xBaseJ
xBaseJ

-

This fork of xBaseJ contains explicit code to truncate the DBF file on close to the correct size.
It also contains an explicit api call to set the codepage marker value for the DBF.

These are required to be able to create DBFs that can be read by Visual Foxpro 9/Sedna without having to jump through special hoops.

-

To use this project as a Gradle dependency:
```
repositories {
  jcenter()
  maven { url "https://jitpack.io" }
}
dependencies {
  compile ('com.github.michael-newsrx:xBaseJ:20160715') {
		exclude group: 'org.eclipse.swt'
	}
}
```

To use this project as a Maven dependency:
```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
.
.
.
	<dependency>
	    <groupId>com.github.michael-newsrx</groupId>
	    <artifactId>xBaseJ</artifactId>
	    <version>20160715</version>
	</dependency>
```

or if you prefer the most recent code:

(Gradle)
```
repositories {
  jcenter()
  maven { url "https://jitpack.io" }
}
dependencies {
  compile ('com.github.michael-newsrx:xBaseJ:master-SNAPSHOT') {
		exclude group: 'org.eclipse.swt'
	}
}
```
(Maven)
```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
.
.
.
	<dependency>
	    <groupId>com.github.michael-newsrx</groupId>
	    <artifactId>xBaseJ</artifactId>
	    <version>master-SNAPSHOT</version>
	</dependency>
```