# Progressive

[![Build Status](https://app.travis-ci.com/CrissNamon/progressive.svg?branch=main)](https://app.travis-ci.com/CrissNamon/progressive) ![release](https://img.shields.io/github/v/release/crissnamon/progressive?include_prereleases) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/tech.hiddenproject/progressive/badge.svg)](https://maven-badges.herokuapp.com/maven-central/tech.hiddenproject/progressive)
<p>Progressive is a simple game framework, which provides you predefined way to develop your game logic.</p>

#### About project

___
<p>
Progressive gives you an IoC/DI container with auto injections feature through annotations, game objects, game scripts and much more! It's is like a constructor. You can add game objects and attach game scripts to them.

Main features:

- Extendable IoC/DI containers with auto injections through annotations, frame updates
- GameObjects and GameScripts
- State machine
- Storage (Persistence api)
- The global observer for components connection
- Proxy classes and proxy objects creation through annotations with custom method interceptors

</p> 

#### Usage

##### Maven

```xml
<dependency>
  <groupId>tech.hiddenproject</groupId>
  <artifactId>progressive-all</artifactId>
  <version>0.7.11</version>
</dependency>
```

<details>
  <summary>If you don't need all components</summary>

- progressive-api required for each component

```xml

<dependency>
  <groupId>tech.hiddenproject</groupId>
  <artifactId>progressive-api</artifactId>
  <version>0.7.11</version>
</dependency>
```

- progressive-game contains Game and related classes

```xml

<dependency>
  <groupId>tech.hiddenproject</groupId>
  <artifactId>progressive-game</artifactId>
  <version>0.7.11</version>
</dependency>
```

- progressive-injection contains DIContainer and related classes

```xml

<dependency>
  <groupId>tech.hiddenproject</groupId>
  <artifactId>progressive-injection</artifactId>
  <version>0.7.11</version>
</dependency>
```

- progressive-persistence contains Storage and related classes

```xml

<dependency>
  <groupId>tech.hiddenproject</groupId>
  <artifactId>progressive-persistence</artifactId>
  <version>0.7.11</version>
</dependency>
```

- progressive-proxy contains ProxyCreator and related classes

```xml

<dependency>
  <groupId>tech.hiddenproject</groupId>
  <artifactId>progressive-proxy</artifactId>
  <version>0.7.11</version>
</dependency>
```

</details>

##### Gradle

````groovy
implementation 'tech.hiddenproject:progressive-all:0.7.11'
````

<details>
  <summary>If you don't need all components</summary>

Use same artifacts from maven

</details>

#### Resources

___

* Learn more at Progressive [Wiki](https://github.com/CrissNamon/progressive/wiki)
* Look at some examples
  in [example](https://github.com/CrissNamon/progressive/blob/main/src/main/java/tech/hiddenproject/example/) package
* See javadoc [here](https://hiddenproject.tech/progressive/javadoc)

#### Dependencies and source

___

[BasicProxyCreator](https://github.com/CrissNamon/progressive/blob/main/src/main/java/ru/hiddenproject/progressive/basic/BasicProxyCreator.java)
uses [ByteBuddy](https://bytebuddy.net/) for proxy creation. You need to add byte-buddy lib to be able to use proxy
classes in your project. For android development you also need to
add [byte-buddy-android](https://github.com/raphw/byte-buddy/tree/master/byte-buddy-android) lib.
<p>All other parts of Progressive have no dependencies and use only Java 8.</p> 

#### Repository info

___

* The main branch contains stable release
* Development branch contains WIP code
* Progressive is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0)

#### Authors

___

* [Danila Rassokhin](https://gihub.com/crissnamon) [![Twitter](https://img.shields.io/twitter/follow/kpekepsalt_en?style=social)](https://twitter.com/kpekepsalt_en)
