# xBaseJ
xBaseJ

-

This fork of xBaseJ contains explicit code to truncate the DBF file on close to the correct size.
It also contains an explicit api call to set the codepage marker value for the DBF.

These are required to be able to create DBFs that can be read by Visual Foxpro 9/Sedna without having to jump through special hoops.

Additionally several of the field setters have been made "null safe".

Initial implementation for the proper handling of DBFs which use the later MEMO FILE 4 byte index format instead of original 10 digit string format.

Preliminary support for reading (**ONLY**) Visual FoxPro files has been implemented. You will need to set the xBaseJ property "ignoreVersionMismatch" to "true". I.E:

```Java
Util.setxBaseJProperty("ignoreVersionMismatch", "true");
```

Attempting to write out memo fields to a visual foxpro file at this time will result in a fatal error or a corrupted DBF and FPT.

Char Fields are now by default space filled and trailing space trimmed to closer match Visual FoxPro expectations. (Visual FoxPro string trim functions don't trim chr(0)).

-

This project can easily be used with Gradle, Maven, SBT, and Leiningen: https://jitpack.io/#NewsRxTech/xBaseJ/

Example Gradle config:

```Java
repositories {
jcenter()
maven { url "https://jitpack.io" }
}
dependencies {
compile ('com.github.NewsRxTech:xBaseJ:20171206') {
		exclude group: 'org.eclipse.swt'
	}
}
```
