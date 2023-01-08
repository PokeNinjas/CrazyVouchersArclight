# Vouchers
The legacy source for Vouchers ( 1.8 -> 1.16.5 )

## Modrinth:
All versions labeled "Alpha" are legacy versions.
https://modrinth.com/plugin/crazyvouchers/versions

## Repository:
https://repo.crazycrew.us/#/releases

# Developer API

## Groovy
<details>
 <summary>
   Gradle (Groovy)
 </summary>

```gradle
repositories {
    maven {
        url = "https://repo.crazycrew.us/releases"
    }
}
```

```gradle
dependencies {
    compileOnly "me.badbones69.vouchers:vouchers:1.9.10"
}
```
</details>

## Kotlin
<details>
 <summary>
   Gradle (Kotlin)
 </summary>

```gradle
repositories {
    maven("https://repo.crazycrew.us/releases")
}
```

```gradle
dependencies {
    compileOnly("me.badbones69.vouchers", "vouchers", "1.9.10")
}
```
</details>

## Maven
<details>
 <summary>
   Maven
 </summary>

```xml
<repository>
  <id>crazycrew</id>
  <url>https://repo.crazycrew.us/releases</url>
</repository>
```

```xml
<dependency>
  <groupId>me.badbones69.vouchers</groupId>
  <artifactId>vouchers</artifactId>
  <version>1.9.10</version>
 </dependency>
```
</details>