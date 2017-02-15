# xBaseJ
xBaseJ

-

This fork of xBaseJ contains explicit code to truncate the DBF file on close to the correct size.
It also contains an explicit api call to set the codepage marker value for the DBF.

These are required to be able to create DBFs that can be read by Visual Foxpro 9/Sedna without having to jump through special hoops.

Additionally several of the field setters have been made "null safe".

-

This project can easily be used with Gradle, Maven, SBT, and Leiningen: https://jitpack.io/#NewsRxTech/xBaseJ/

Example Gradle config:

```
repositories {
  jcenter()
  maven { url "https://jitpack.io" }
}
dependencies {
  compile ('com.github.NewsRxTech:xBaseJ:20170201') {
		exclude group: 'org.eclipse.swt'
	}
}
```



